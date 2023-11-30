package game.role

enum class Role {
    Player,
    Moderator,
    Administrator;

    private fun id(): Int {
        return 1 shl ordinal
    }

    fun match(value: Int): Boolean {
        return value and id() == id()
    }
}