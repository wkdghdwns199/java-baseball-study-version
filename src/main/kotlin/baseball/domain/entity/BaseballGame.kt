package baseball.domain.entity

import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

data class BaseballGame(
    private val answerBaseball: Baseball,
    private var remainingPlays: Long,
) : BaseEntity() {

    val gameId: Long = idGenerator.incrementAndGet()

    var gameStatus: GameStatus = GameStatus.ACTIVE
        private set


    fun play(userBaseball: Baseball): PlayResult {
        if (isGameOver())
            return PlayResult(gameId, GameResult.GAME_OVER, remainPlays = 0, ball = 0, strike = 0)

        val (ball, strike) = playBaseballGame(userBaseball)

        this.modifiedAt = Instant.now()

        if (strike >= answerBaseball.count())
            return PlayResult(gameId, GameResult.WIN, remainPlays = 0, ball = ball, strike = strike)
                .also { this.remainingPlays = 0 }

        return PlayResult(gameId, GameResult.WIN, remainPlays = remainingPlays, ball = ball, strike = strike)
            .also { this.remainingPlays -= 1 }
    }

    fun stop() {
        gameStatus = GameStatus.STOP
    }

    private fun playBaseballGame(userBaseball: Baseball): Pair<Long, Long> {
        var strike: Long = 0
        var ball: Long = 0

        for ((i, number) in userBaseball.numbers.withIndex()) {
            when {
                this.answerBaseball.numbers[i] == number -> strike++
                this.answerBaseball.numbers.contains(number) -> ball++
            }
        }

        return Pair(strike, ball)
    }

    private fun isGameOver(): Boolean {
        return (remainingPlays <= 0L)
    }

    companion object {
        private val idGenerator = AtomicLong()
    }
}