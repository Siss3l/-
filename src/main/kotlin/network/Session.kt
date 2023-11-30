package network

import config.Conf
import database.db.DbFactory.init
import database.db.DbFactory.shut
import io.netty.buffer.Unpooled.wrappedBuffer
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.string.LineEncoder
import io.netty.handler.codec.string.LineSeparator
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import network.Session.authTCP
import network.Session.gameTCP
import network.SessionContext.Companion.add
import org.reactivestreams.Publisher
import packets.PacketManager.Companion.handlePacket
import packets.Processing
import reactor.core.publisher.Flux.never
import reactor.netty.NettyInbound
import reactor.netty.NettyOutbound
import reactor.netty.tcp.TcpServer
import kotlin.text.Charsets.UTF_8


object Session {
    /**
     * A mutable map that stores `client` session contexts using
     * unique identifiers to manage `client` sessions.
     */
    private val clients: MutableMap<String, SessionContext> = HashMap()

    /**
     * Starts a `Transmission Control Protocol` server for authentication.
     *
     * Usage of meta-programming to create a reference
     * to the `sessionHandler` method then binding
     * the server and blocking it until it's disposed.
     *
     * `Ngrok` used as reverse proxy with `Nginx` as static file serving.
     * The first packet sent to client is `HelloConnection`, refer to diagram.
     *
     * @param host The host address to bind the server to.
     * @param port The port number to listen on.
     */
    fun authTCP(host: String, port: Int) {
        val server: TcpServer = TcpServer.create()
            .host(host).port(port)
            .wiretap(false)
            .doOnBound {
                println("Starting Authentication at tcp://$host:$port")
                init()
            }
            .doOnConnection { c ->
                c.addHandlerFirst(DelimiterBasedFrameDecoder(2147483647, wrappedBuffer(byteArrayOf(10, 0))))
                c.addHandlerLast(StringDecoder(UTF_8))
                c.addHandlerLast(StringEncoder(UTF_8))
                c.addHandlerLast(LineEncoder(LineSeparator(wrappedBuffer(byteArrayOf(0)).toString(UTF_8))))
                val (ctx, hc) = add(c) to Processing.HelloConnection()
                ctx.key = hc.key
                ctx.uuid = c.channel().id().asLongText()
                println("${ctx.key} &&& ${ctx.uuid}")
                clients[ctx.uuid] = ctx
                c.channel().writeAndFlush(hc.toString())
            }
            .handle(::sessionHandler)
        server.bindNow().onDispose().block()
    }

    /**
     * Handles incoming data from `clients` in a non-concurrent manner.
     *
     * Subscribing to fallback publisher with `doOnError`, `onErrorResume`.
     * @param inb The NettyInbound representing incoming data.
     * @param out The NettyOutbound representing outgoing data.
     * @return A Publisher that emits a Void when data handling is complete.
     */
    private fun sessionHandler(inb: NettyInbound, out: NettyOutbound): Publisher<Void> {
        inb.withConnection { c ->
            c.inbound().receiveObject()
                .subscribe { p ->
                    handlePacket(clients, clients[c.channel().id().asLongText()]!!, p.toString())
                }
            c.onDispose {
                println("Lost connection at ${c.outbound()}.")
                val uuid = c.channel().id().asLongText()
                println("Removed $uuid key.")
                clients.remove(uuid)
                shut()
            }
        }
        out.withConnection {
            println("Connection of $it")
            return@withConnection
        }
        return never()
    }

    /**
     * Starts a WIP TCP server for Gaming playability.
     *
     */
    fun gameTCP(host: String, port: Int) {
        val server: TcpServer = TcpServer.create()
            .host(host).port(port)
            .wiretap(false)
            .doOnBound {
                println("Starting Playability at tcp://$host:$port")
            } /*.handle(::gamingHandler)*/
        server.bindNow().onDispose().block()
    }
}

/**
 * Function that sets up and starts two asynchronous network tasks using Kotlin Coroutines.
 */
fun start() {
    runBlocking {
        val authJob = CoroutineScope(Dispatchers.Default).async {
            authTCP(Conf.host, Conf.authPort)
        }
        val gameJob = CoroutineScope(Dispatchers.Default).async {
            gameTCP(Conf.host, Conf.gamePort)
        }
        listOf(authJob.await(), gameJob.await())
    }
}

fun main() {
    start()
}
