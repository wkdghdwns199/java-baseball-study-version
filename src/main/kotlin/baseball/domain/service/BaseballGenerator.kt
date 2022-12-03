package baseball.domain.service

import baseball.domain.entity.Baseball

interface BaseballGenerator {
    fun createBaseball(): Baseball
}

internal class RandomBaseballGenerator(
    private val baseballPolicy: BaseballPolicy
) : BaseballGenerator {
    override fun createBaseball(): Baseball {
        baseballPolicy.totalGamePlayCount
        baseballPolicy.baseballNumberRange
        baseballPolicy.baseballNumberCount

        val randomNumbers: List<Long> = (0..baseballPolicy.baseballNumberCount)
            .map { baseballPolicy.baseballNumberRange.random() }

        return Baseball(numbers = randomNumbers)
    }
}