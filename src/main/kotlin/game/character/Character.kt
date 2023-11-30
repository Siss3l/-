package game.character


/**
 * The `Character` class represented with data parsed from a list of packets.
 *
 * @property id The unique identifier for the character.
 * @property accountId The account ID of the character.
 * @property level The level of the character.
 * @property name The name of the character (could be unique).
 * @property gender The gender of the character.
 * @property colors The list of five character's colors.
 * @property gfxId The graphics ID of the character (always four by default?).
 * @property hat The hat of the character (two identifier of hat 8 and 9).
 * @property date The creation date in Europe of the character (to be sorted).
 *
 * Must be dynamic in relation to the evolution of the character's stuff.
 */
data class Character(
    private val id: Int,
    private val accountId: Int?,
    private var level: Int? = null,
    private var name: String,
    private var gender: Int? = null,
    private var colors: String? = null,
    private var gfxId: Int? = null,
    private var hat: Int? = null,
    private var date: String? = null
) {

    companion object {
        operator fun invoke(s: String, accountId: Int? = null): Character? {
            val data = s.removePrefix("AA").split("|")
            val cal = java.util.Calendar.getInstance()
            cal.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"))
            if (data.size == 10) {
                val date = "%04d-%02d-%02d %02d:%02d:%02d".format(cal[1], cal[2] + 1, cal[5], cal[11], cal[12], cal[13])
                val name = data.getOrElse(0) { "" }
                val level = (data[1].toIntOrNull() ?: 1)
                val gender = (data[2].toIntOrNull() ?: 0)
                val col = listOf(data[3], data[4], data[5], data[6], data[7])
                val colors = col.joinToString(";") { v ->
                    if (v.all { it.isDigit() }) v.toIntOrNull()?.toString(16) ?: "-1"
                    else "-1"
                }
                val gfxId = if (data[8].toIntOrNull() == null) 4 else 4
                val hat = (data[9].toIntOrNull() ?: 8)
                if (!col.all { ("^[0-9A-Fa-f]{6}[0-9A-Fa-f]{0,2}$".toRegex().matches(it)) } ||
                    colors.contains("-1") || name.isEmpty()) {
                    return null
                }
                return Character(-1, accountId, level, name, gender, colors, gfxId, hat, date)
            }
            return null
        }
    }

    fun id(): Int {
        return this.id
    }

    fun accountId(): Int? {
        return this.accountId
    }

    fun name(): String {
        return this.name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun level(): Int? {
        return this.level
    }

    fun setLevel(level: Int) {
        this.level = level
    }

    fun gender(): Int? {
        return this.gender
    }

    fun setGender(gender: Int) {
        this.gender = gender
    }

    fun colors(): String? {
        return this.colors
    }

    fun setColors(colors: String?) {
        this.colors = colors
    }

    fun hat(): Int? {
        return this.hat
    }

    fun setHat(hat: Int?) {
        this.hat = hat
    }

    fun gfxId(): Int? {
        return this.gfxId
    }

    fun setGfxId(gfxId: Int?) {
        this.gfxId = gfxId
    }

    fun date(): String? {
        return this.date
    }

    fun setDate(date: String) {
        this.date = date
    }

    override fun toString(): String {
        val specie = 10
        return "$name;$level;$specie;$colors;8,$gfxId,a,a,c,7;;;;;" // TODO Change stuff
    }
}
