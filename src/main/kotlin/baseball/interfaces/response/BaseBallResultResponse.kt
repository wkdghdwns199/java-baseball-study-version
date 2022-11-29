package baseball.interfaces.response

import baseball.domain.dto.BaseBallResultDto

data class BaseBallResultResponse(
    val gameId: Long,
    val remainingPlays: Long,
    val score: BaseBallScoreResponse,
    val isGameEnd: Boolean = false
) {
    companion object {
        fun fromDto(baseBallResultDto: BaseBallResultDto): BaseBallResultResponse =
            with(baseBallResultDto) {
                BaseBallResultResponse(
                    gameId = gameId,
                    remainingPlays = remainingPlays,
                    score = BaseBallScoreResponse(ball = baseballScore.ball, strike = baseballScore.strike)
                )
            }
    }
}


data class BaseBallScoreResponse(
    val ball: Long,
    val strike: Long
)
