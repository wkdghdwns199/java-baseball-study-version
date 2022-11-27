package baseball.domain.dto

data class BaseBallResultDto(
    val gameId: String,
    val remainingPlays: Long,
    val baseballScore: BaseBallScore,
    val isGameEnd: Boolean = false
)

class BaseBallScore(
    val ball: Long,
    val strike: Long
)

