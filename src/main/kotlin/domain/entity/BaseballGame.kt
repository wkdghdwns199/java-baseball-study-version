package domain.entity

import domain.type.GameStatus
import java.time.Instant

data class BaseballGame(
    private val answerBaseball: Baseball,
    private var remainingPlays: Long,
    private var gameStatus: GameStatus = GameStatus.ACTIVE,
) : BaseEntity() {

    val isGameOver: Boolean
        get() = this.gameStatus != GameStatus.ACTIVE

    fun play(userBaseball: Baseball): BaseballGamePlayHistory {
        this.modifiedAt = Instant.now()
        this.remainingPlays -= 1

        val (ball: Long, strike: Long) = calculateBaseballResult(userBaseball)

        when {
            isRemainPlayCount() == false -> {
                this.gameStatus = GameStatus.GAME_OVER
            }

            isStrikeOut(strike) -> {
                this.remainingPlays = 0
                this.gameStatus = GameStatus.GAME_WIN
            }
        }

        return BaseballGamePlayHistory(
            gameId = this.id,
            gameStatus = this.gameStatus,
            remainPlays = this.remainingPlays,
            ball = ball,
            strike = strike
        )
    }

    fun stop() {
        this.modifiedAt = Instant.now()
        gameStatus = GameStatus.STOP_IN_PLAYING
    }

    private fun isRemainPlayCount() = remainingPlays > 0L

    private fun isStrikeOut(strike: Long): Boolean {
        return strike >= answerBaseball.count
    }

    private fun calculateBaseballResult(userBaseball: Baseball): Pair<Long, Long> {
        var strike: Long = 0
        var ball: Long = 0

        for ((i: Int, number: Long) in userBaseball.numbers.withIndex()) {
            when {
                this.answerBaseball.numbers[i] == number -> strike++
                this.answerBaseball.numbers.contains(number) -> ball++
            }
        }
        return Pair(ball, strike)
    }

}