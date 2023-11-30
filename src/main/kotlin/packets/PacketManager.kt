package packets

import config.Hashing
import config.Hashing.decodePassword
import database.db.DbAccount.getAccountByUsername
import database.db.DbFactory.shut
import network.SessionContext
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible


/**
 * The `PacketManager` class that manages received packets, from SWF client.
 *
 * For Adobe Flash `loader.swf` launch, refer to the 32-bit `flashplayer_32_sa` archived version.
 * For ElectronJS PepperFlash player, do not refer to any `preloader.swf` file.
 * For Ruffle-RS/CheerpX display tool, refer to their respective website/browser extension.
 * And for Flash documentation, refer to the `open-flash.github.io/documentation` website.
 */
class PacketManager {
    /**
     * The `karray` property is a map that associates objects with their corresponding Kotlin classes.
     *
     * It is initialized by dynamically inspecting the nested classes of the `Processing` class
     * and finding those that have a method named "prefix".
     *
     * These classes are then paired with the result of calling the "prefix" method on instances
     * created from these classes and the map is structured as object instances as keys
     * and their corresponding Kotlin classes as values.
     */
    private var karray: Map<Any, KClass<*>> = Processing::class.nestedClasses.mapNotNull { i ->
        i.members.find { p ->
            p.name.contentEquals("prefix")
        }?.apply {
            isAccessible = true
        }?.call(i.createInstance())?.let {
            it to i
        }
    }.associateBy({ it.first }, { it.second })

    companion object {
        /**
         * The private companion function of the `Processing` object
         * checks account information provided in a network packet.
         *
         * @param ctx The session context associated with the connection.
         * @param packet The packet containing account information.
         */
        private fun checkAccount(ctx: SessionContext, packet: String) {
            val (user, passwd) = packet.split("\\R#[12]".toRegex()).filter { it != packet }
            val (username, u) = user.partition { it.isLetterOrDigit() }
            if (username.isNotEmpty() && passwd.isNotEmpty() && u.isEmpty()) { // Bannish
                val account = getAccountByUsername(username)
                if (account != null) {
                    val check = Hashing.check((account.password() ?: ""), decodePassword(passwd, ctx.key))
                    if (!check) {
                        exit(ctx, Processing.BadPassword().toString())
                    } else {
                        ctx.attach(account)
                        ctx.state = PacketHandlerCapability.NewQueue.name
                    }
                } else {
                    exit(ctx, Processing.InvalidInputs().toString())
                }
            } else {
                exit(ctx, Processing.InvalidInputs().toString())
            }
        }

        /**
         * The `exit` function is responsible for gracefully exiting a network session
         * and sending a final message to the client.
         *
         * @param ctx The session context associated with the connection.
         * @param data The final message to be sent to the client before disconnecting.
         */
        private fun exit(ctx: SessionContext, data: String) {
            ctx.state = PacketHandlerCapability.Disconnecting.name
            ctx.send(data)
            ctx.close()
            shut()
        }

        /**
         * The `handlePacket` function processes incoming FIFO network packets
         * based on the current state of the session context and prefix.
         *
         * @param ctx The session context associated with the connection.
         * @param packet The incoming packet to be handled.
         */
        fun handlePacket(clients: MutableMap<String, SessionContext>, ctx: SessionContext, packet: String) {
            println("[Receive]::{$packet}")
            if (ctx.state == PacketHandlerCapability.Disconnecting.name) {
                return
            }
            val karray = PacketManager().karray
            val array = karray.keys.firstOrNull { packet.startsWith(it.toString()) }.let { karray[it] }
            if (array == null) {
                when (ctx.state) {
                    PacketHandlerCapability.Authenticating.name -> {
                        if (!Processing.BadPassword("AlEf", packet).check()) {
                            exit(ctx, Processing.BadPassword().toString())
                        } else {
                            checkAccount(ctx, packet)
                        }
                    }

                    PacketHandlerCapability.Starting.name -> {
                        if (!Processing.BadVersion("AlEv", packet).check()) {
                            exit(ctx, Processing.BadVersion().toString())
                        } else {
                            ctx.state = PacketHandlerCapability.Authenticating.name
                        }
                    }

                    else -> {
                        ctx.state = PacketHandlerCapability.None.name
                    }
                }
            } else { // Todo Some hard-coded examples of packets
                clients.filter { it.key != ctx.uuid && it.value.account?.id() == ctx.account?.id() }
                    .forEach { (key, client) ->
                        ctx.send(Processing.AlreadyConnected().toString())
                        ctx.account = null
                        ctx.flush()
                        client.close()
                        clients.remove(key)
                    }
                // TODO WIP Change on different parameters
                array.members.find { it.name == "toString" }?.call(array.createInstance()).toString().split("§")
                    .forEach { i ->
                        ctx.send(i)
                    }

                if (packet == ClientHandlerCapability.CharactersList.value) {
                    ctx.state = ClientHandlerCapability.CharactersList.name
                }

                if (packet == "Af") {
                    Processing.NewQueue("Af", packet, ctx).toString().split("§").forEach {
                        ctx.send(it)
                    }
                }

                if (packet == "AL") {
                    ctx.send(Processing.OnCharactersList("AL", ctx).toString())
                }

                if ("AS" in packet) {
                    Processing.OnCharacterSelected("AS", packet, ctx).toString().split("§").forEach {
                        ctx.send(it)
                    }
                }

                if ("AA" in packet) {
                    val c = Processing.CreationCharacter("AA", packet, ctx).toString()
                    ctx.send(c)
                }

                if ("AD" in packet) {
                    val d = Processing.DeleteCharacter("AD", packet, ctx).toString()
                    ctx.send(d)
                }
            }

            if ("AP" in packet) {
                ctx.send(Processing.CharacterNameGenerated().toString())
            }

            if ("GA" in packet) {
                ctx.send("GA0;1;1;ac...") // Pathfinding
            }

            if ("BM" in packet) {
                ctx.send(Processing.Messaging("BM", packet, ctx).toString())
                val copy = clients.toMutableMap().apply { remove(ctx.uuid) }
                copy.forEach { (_, client) ->
                    client.send(Processing.Messaging("BM", packet, ctx, true).toString())
                }
            }

            
            if ("BS" in packet) {
                ctx.send(Processing.OnSmiley("BS", packet).toString())
            }

            if ("eU" in packet) {
                ctx.send(Processing.OnUseEmote("eU", packet).toString())
            }

            if ("ping" == packet) {
                ctx.send(Processing.AveragePing().toString())
            } // [...]
        }
    }
}
