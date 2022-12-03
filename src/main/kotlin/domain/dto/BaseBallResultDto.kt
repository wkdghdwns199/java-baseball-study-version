package domain.dto

import domain.entity.BaseballGamePlayHistory
import domain.type.GameStatus

data class BaseBallResultDto(
    val gameId: Long,
    val remainingPlays: Long,
    val baseballScore: BaseBallScore,
    val gameStatus: GameStatus,
) {
    class BaseBallScore(
        val ball: Long,
        val strike: Long,
    )

    companion object {
        fun fromEntity(
            gamePlayResult: BaseballGamePlayHistory
        ): BaseBallResultDto =
            BaseBallResultDto(
                gameId = gamePlayResult.gameId,
                remainingPlays = gamePlayResult.remainPlays,
                baseballScore = BaseBallScore(
                    ball = gamePlayResult.ball,
                    strike = gamePlayResult.strike
                ),
                gameStatus = gamePlayResult.gameStatus
            )
    }
}
