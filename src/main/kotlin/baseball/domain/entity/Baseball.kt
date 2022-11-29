package baseball.domain.entity

@JvmInline
value class Baseball private constructor(
    val numbers: List<Long>
) {

    fun count() = this.numbers.size.toLong()

    companion object {
        fun of(first: Long, second: Long, last: Long): Baseball =
            Baseball(listOf(first, second, last))
    }
}
