package baseball.domain.service

import baseball.domain.entity.Baseball

interface BaseballGenerator {
    fun createBaseball(): Baseball
}

internal class RandomBaseballGenerator() : BaseballGenerator {
    override fun createBaseball(): Baseball =
        Baseball.of(
            first = (0L..9L).random(),
            second = (0L..9L).random(),
            last = (0L..9L).random(),
        )
}