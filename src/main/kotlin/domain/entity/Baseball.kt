package domain.entity

@JvmInline
value class Baseball(
    val numbers: List<Long>
) {
    val count: Long
        get() = this.numbers.size.toLong()
}
