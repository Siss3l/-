package packets

/**
 * Enum class `PacketHandlerCapability` represents different states of a packet handler.
 * It is used to denote the current state order of packet processing.
 */
enum class PacketHandlerCapability {
    Authenticating,
    Disconnecting,
    NewQueue,
    None,
    Starting;
}

