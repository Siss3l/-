package network

import io.netty.channel.Channel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import packets.PacketHandlerCapability
import reactor.netty.Connection


/**
 * The `SessionContext` class represents the context of a network session, including the connection,
 * channel information, and session-specific data.
 *
 * @param conn The underlying network connection associated with the session.
 */
class SessionContext(conn: Connection) {
    var account: game.account.Account? = null
    var character: game.character.Character? = null

    private val channel: Channel = conn.channel()

    /**
     * Closes the session's underlying network channel if it's open.
     *
     * @return `Unit` if the channel was already closed, or the result of closing the channel.
     */
    fun close(): Any = if (channel.isOpen) channel.close() else Unit

    /**
     * Flushes the data in the session's channel.
     */
    @Synchronized
    fun flush(): Channel = channel.flush()

    /**
     * Retrieves the host address of the remote endpoint.
     * Could be private as "XXX.XXX.XXX.Y" value.
     *
     * @return The host address string.
     */
    fun host(): String = with(channel.remoteAddress() as java.net.InetSocketAddress) { address.hostAddress }

    var key: String = ""

    /**
     * Retrieves the port of the remote endpoint.
     *
     * @return The remote port number.
     */
    fun port(): Int = with(channel.remoteAddress() as java.net.InetSocketAddress) { port }

    var uuid: String = ""

    var state = PacketHandlerCapability.Starting.name

    /**
     * Sends a message over the session's channel.
     *
     * @param message The message to send.
     */
    fun send(message: String) {
        val snd = channel.writeAndFlush(message)
        println(message)
        if (snd.isSuccess) {
            println("[Sending]::{$message}")
        } else {
            System.err.println("The sending request did not succeed")
        }
    }

    /**
     * Initiates a delayed transaction within the session.
     *
     * @param transaction The transaction to execute, represented as a suspend function.
     * @return A `Deferred` object representing the asynchronous execution of the transaction.
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun delayedTransaction(transaction: suspend SessionContext.() -> Unit): Deferred<Unit> {
        return GlobalScope.async {
            transaction(this@SessionContext)
        }
    }

    /**
     * Attaches an [account] or a [character] to the `SessionContext` class.
     *
     * @param account The game object to attach.
     */
    fun attach(account: Any) {
        if (account is game.account.Account) {
            this.account = account
        } else if (account is game.character.Character) {
            this.character = account
        }
        println("Attaching the $account to the SessionContext")
    }

    companion object {
        /**
         * Creates and initializes a `SessionContext` instance.
         *
         * @param conn The network connection associated with the session.
         * @return A new `SessionContext` instance.
         */
        fun add(conn: Connection) = SessionContext(conn)
    }
}