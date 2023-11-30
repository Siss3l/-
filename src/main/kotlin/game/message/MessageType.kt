package game.message

/**
 * Different type by message channel.
 */
enum class MessageType(val type: String) {
    General("*"),
    Party("$"),
    Team("#"),
    Trade(":"),
    Guild("%"),
    Admin("@")
}