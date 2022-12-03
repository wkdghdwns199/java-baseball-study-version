package baseball.domain.service

interface BaseballPolicy {
    val baseballNumberCount: Long
    val baseballNumberRange: LongRange
    val totalGamePlayCount: Long
}

internal class DefaultBaseballPolicy(
    override val baseballNumberCount: Long = 3,
    override val baseballNumberRange: LongRange = (1L..9L),
    override val totalGamePlayCount: Long = 5,
) : BaseballPolicy

