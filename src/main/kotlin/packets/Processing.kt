package packets

import config.Conf
import database.db.DbCharacter.createCharacter
import database.db.DbCharacter.deleteCharacter
import database.db.DbCharacter.getAllCharactersFromAccount
import game.character.Character
import game.message.MessageType
import network.SessionContext
import java.util.*


/**
 * The `Processing` object contains classes related to processing network packets.
 * Refactoring could be intended or `annotation` class Target.
 * Debug message as `SOS<showMessage key="trace"><![CDATA[ Displaying advice n]]></showMessage>`
 */
object Processing {
    /**
     * Represents an "Already Connected" response.
     *
     * @property prefix The prefix for the response.
     */
    class AlreadyConnected(private val prefix: String = "AlEa") {
        /**
         * Returns the string of the response.
         *
         * @return The string representation.
         */
        override fun toString(): String {
            return prefix
        }
    }

    /**
     * Represents an "Already Connected In Game Server" response.
     *
     * @property prefix The prefix for the response.
     */
    class AlreadyConnectedInGameServer(private val prefix: String = "AlEc") {
        override fun toString(): String {
            return prefix
        }
    }

    /**
     * Represents an average ping response.
     *
     * @property prefix The prefix of the packet sent by the client.
     */
    class AveragePing(private val prefix: String = "ping") {
        override fun toString(): String {
            return "p"
        }
    }

    /**
     * Represents a "Bad Password" response.
     *
     * @property prefix The prefix for the response.
     * @property packet The packet associated with the response.
     */
    class BadPassword(private val prefix: String = "AlEf", private val packet: String = "") {
        internal fun check() = packet.split("\\R#[12]".toRegex()).size == 2
        override fun toString(): String {
            return prefix
        }
    }

    /**
     * Represents a "Bad Version" from the client.
     *
     * @property prefix The prefix for the response.
     * @property packet The packet associated with the response.
     */
    class BadVersion(private val prefix: String = "AlEv", private val packet: String = "") {
        internal fun check(): Boolean = packet.matches("^\\d.\\d{2}.\\d+s\$".toRegex()).let { packet == Conf.version }
        override fun toString(): String {
            return "${prefix}${packet.ifEmpty { Conf.version }}"
        }
    }

    /**
     * Represents the date in a corresponding format.
     *
     * @property prefix The prefix for the response.
     */
    class BigDate(private val prefix: String = "BD") {
        override fun toString(): String {
            val cal = Calendar.getInstance()
            cal.setTimeZone(TimeZone.getTimeZone("Europe/Paris"))
            // val _Im = "%04d~%02d~%02d~%02d~%02d".format(cal[1], cal[2] + 1, cal[5], cal[11], cal[12])
            cal.add(Calendar.YEAR, -1370)
            return "BD%04d|%02d|%02d".format(cal[1], cal[2] + 1, cal[5]) + "§BT1..."
        }
    }

    /**
     * Generating a [Character] name for the client and
     * insults should be dealt with in the same way as racist remarks.
     * Error packet like "APE1".
     *
     * @property prefix The prefix for the response.
     */
    class CharacterNameGenerated(private val prefix: String = "AP") {
        override fun toString(): String {
            return "${prefix}K" + (0..<(3..12).random())
                .mapIndexed { index, _ ->
                    if (index % 2 == 0) "aeiouy".random() else "bcdfghjklmnpqrstvwxyz".random()
                }.joinToString("")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }


    class CreationCharacter(
        private val prefix: String = "AA",
        private val packet: String = "",
        private val ctx: SessionContext? = null
    ) {
        internal fun create(): String {
            if (packet.isNotEmpty() && ctx != null && packet.startsWith(prefix)) {
                val characterCount = ctx.account?.let { getAllCharactersFromAccount(it).size } ?: 0
                if (characterCount >= 3) {
                    return "AlEp"
                }
                val character = Character.invoke(packet, ctx.account?.id())
                val chr = character?.let { createCharacter(it) } ?: 0
                if (chr > 0) {
                    return OnCharactersList(ctx = ctx).toString()
                }
            }
            return "AlEp"
        }

        override fun toString(): String {
            return create()
        }
    }

    class DeleteCharacter(
        private val prefix: String = "AD",
        private val packet: String = "",
        private val ctx: SessionContext? = null
    ) {
        private fun check(): String {
            val p = packet.removePrefix(prefix).removeSuffix("|")
            if (p.takeIf { it.length == 1 && it.all { k -> k.isDigit() } } != null && ctx != null
                && packet.isNotEmpty()) {
                val ans = packet.split("|").let {
                    if (it.size > 1) {
                        it[1]
                    } else {
                        ""
                    }
                }
                val answer: String = ctx.account?.answer() ?: ""
                if (answer != ans && answer.isEmpty() && ans.isEmpty()) {
                    return "ADE"
                }
                when (p.toInt()) {
                    1 -> {
                        val i = ctx.account?.let { getAllCharactersFromAccount(it) }?.first?.id() ?: -1
                        val del = deleteCharacter(i)
                        if (del) {
                            return "ADK"
                        }
                    }

                    2 -> {
                        val i = ctx.account?.let { getAllCharactersFromAccount(it) }?.get(1)?.id() ?: -1
                        val del = deleteCharacter(i)
                        if (del) {
                            return "ADK"
                        }
                    }

                    3 -> {
                        val i = ctx.account?.let { getAllCharactersFromAccount(it) }?.last?.id() ?: -1
                        val del = deleteCharacter(i)
                        if (del) {
                            return "ADK"
                        }
                    }
                }
            } else {
                return "ADE"
            }
            return OnCharactersList(ctx = ctx).toString()
        }

        override fun toString(): String {
            return check()
        }
    }

    class DisconnectedUser(private val prefix: String = "AlEd") {
        override fun toString(): String {
            return prefix
        }
    }

    class GameCode(private val prefix: String = "GC") {
        override fun toString(): String {
            return "GCK|1|Test|..."
        }
    }

    class HelloConnection(private val prefix: String = "HC") {
        var key = (1..32).map { ('a'..'z').toList().random() }.shuffled().joinToString("")
        override fun toString(): String {
            return "$prefix$key"
        }
    }

    class InvalidDate(
        private val prefix: String = "AlEk",
        private val days: String = "",
        private val hours: String = "",
        private val minutes: String = "" // Todo change
    ) {
        override fun toString(): String {
            return "$prefix$days|$hours|$minutes"
        }
    }

    class InvalidInputs(private val prefix: String = "AlEp") {
        override fun toString(): String {
            return prefix
        }
    }

    class IpBanned(private val prefix: String = "AlEk") {
        override fun toString(): String {
            return prefix
        }
    }

    class IsBanned(private val prefix: String = "AlEb") {
        override fun toString(): String { // IPv4 and IPv6
            return prefix
        }
    }

    class Maintenance(private val prefix: String = "AlEm") {
        override fun toString(): String {
            return prefix
        }
    }

    class Messaging(
        private val prefix: String = "BM",
        private val packet: String = "",
        private val ctx: SessionContext? = null,
        private val recv: Boolean = false
    ) {
        private fun send(): String { // TODO Change from DB
            if (packet.isNotEmpty() && ctx != null) { // as interChatUrl
                val p = packet.removePrefix(prefix).split("|")
                val id = if (recv) 2 else 1
                when (p.first) {
                    MessageType.General.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.General.type}|$id|$name|${p[1]}|mystuff"
                    }

                    MessageType.Party.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.Party.type}|$id|$name|${p[1]}|mystuff"
                    }

                    MessageType.Team.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.Team.type}|$id|$name|${p[1]}|mystuff"
                    }

                    MessageType.Trade.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.Trade.type}|$id|$name|${p[1]}|mystuff"
                    }

                    MessageType.Guild.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.Guild.type}|$id|$name|${p[1]}|mystuff"
                    }

                    MessageType.Admin.type -> {
                        val name = ctx.character?.name() ?: "undefined"
                        return "cMK${MessageType.Admin.type}|$id|$name|${p[1]}|mystuff"
                    }
                }
            }
            return ""
        }

        override fun toString(): String {
            return send()
        }
    }

    class NameNotDefined(private val prefix: String = "AlEr") {
        override fun toString(): String {
            return prefix
        }
    }

    class NewQueue(
        private val prefix: String = "Af",
        private val packet: String = "",
        private val ctx: SessionContext? = null
    ) {
        private val second: String = "BN"
        private fun check(): String {
            if (ctx != null) {
                return if (ctx.state == PacketHandlerCapability.NewQueue.name) {
                    listOf(
                        prefix + "0|0|0|0|0",
                        "Af0|0|0|0|0",
                        "AdTest",
                        "...",
                        "AH1;1;110;1",
                        "AlK1",
                        "AQYes"
                    ).joinToString("§")
                } else {
                    second
                }
            }
            return second
        }

        override fun toString(): String {
            return check()
        }
    }

    class NotCompleted(private val prefix: String = "AlEn") {
        override fun toString(): String {
            return prefix
        }
    }

    class OldAccount(private val prefix: String = "AlEo") {
        override fun toString(): String {
            return prefix
        }
    }

    class OldAccountToNew(private val prefix: String = "AlEe") {
        override fun toString(): String {
            return prefix
        }
    }

    class OnCharacterSelected(
        private val prefix: String = "AS",
        private val packet: String = "",
        private val ctx: SessionContext? = null
    ) {
        private fun check(): String {
            val p = packet.removePrefix(prefix)
            if (p.takeIf { it.length == 1 && it.all { k -> k.isDigit() } } != null && ctx != null) {
                val ip = ctx.host()
                when (p.toInt()) {
                    1 -> {
                        val char = ctx.account?.let { getAllCharactersFromAccount(it) }?.first
                        return if (char != null) { // Todo Change SLo+, animations
                            ctx.attach(char)
                            listOf(
                                "Re-",
                                "Rx0",
                                "ASK|1|...",
                                "ZS0",
                                "cC+*#%!pi$:?^@¤",
                                "al|...", // [...]
                                "FO+",
                                "Im0165;$ip",
                                "ILS2000").joinToString("§")
                        } else {
                            "AlEp"
                        }
                    }

                    2 -> {
                        val char = ctx.account?.let { getAllCharactersFromAccount(it) }?.get(1)
                        return if (char != null) {
                            ctx.attach(char)
                            listOf(
                                "Re-",
                                "Rx0",
                                "ASK|1|...",
                                "ZS0",
                                "cC+*#%!pi$:?^@¤",
                                "al|...", // [...]
                                "FO+",
                                "Im0165;$ip",
                                "ILS2000").joinToString("§")
                        } else {
                            "AlEp"
                        }
                    }

                    3 -> {
                        val char = ctx.account?.let { getAllCharactersFromAccount(it) }?.last
                        return if (char != null) {
                            ctx.attach(char)
                            listOf(
                                "Re-",
                                "Rx0",
                                "ASK|1|...",
                                "ZS0",
                                "cC+*#%!pi$:?^@¤",
                                "al|...", // [...]
                                "FO+",
                                "Im0165;$ip",
                                "ILS2000").joinToString("§")
                        } else {
                            "AlEp"
                        }
                    }
                }
            }
            return "AlEp"
        }

        override fun toString(): String {
            return check()
        }
    }

    class OnCharactersList(private val prefix: String = "AL", private val ctx: SessionContext? = null) {
        private fun listed(): String {
            if (ctx != null) {
                val charList = ctx.account?.let { getAllCharactersFromAccount(it) } ?: listOf()
                if (charList.isNotEmpty()) {
                    val data = charList.joinToString("|") { "${charList.indexOf(it) + 1};$it" }
                    return "AC1|$data"
                }
            }
            return "ALK0"
        }

        override fun toString(): String {
            return listed()
        }
    }

    class OnCommunity(private val prefix: String = "Ac") { // Ac0
        override fun toString(): String {
            return prefix
        }
    }

    class OnPseudo(private val prefix: String = "Ad", private val user: String = "") {
        override fun toString(): String {
            return "$prefix$user"
        }
    }

    class OnSmiley(private val prefix: String = "BS", private val packet: String = "") {
        override fun toString(): String {
            val p = packet.removePrefix(prefix)
            if (p.takeIf { it.length < 3 && it.all { k -> k.isDigit() } } != null && packet.isNotEmpty()) {
                return "cS1|${p.toInt()}"
            }
            return ""
        }
    }

    class OnUseEmote(private val prefix: String = "eU", private val packet: String = "") {
        override fun toString(): String {
            val p = packet.removePrefix(prefix)
            if (p.takeIf { it.length == 1 && it.all { k -> k.isDigit() } } != null && packet.isNotEmpty()) {
                return "eUK1|${p.toInt()}"
            }
            return ""
        }
    }

    class ServerFull(private val prefix: String = "AlEw") {
        override fun toString(): String {
            return prefix
        }
    }

    class ServersList(private val prefix: String = "Ax") {
        override fun toString(): String {
            return prefix
        }
    }

    class UsernameAlreadyUsed(private val prefix: String = "AlEs") {
        override fun toString(): String {
            return prefix
        }
    }
}