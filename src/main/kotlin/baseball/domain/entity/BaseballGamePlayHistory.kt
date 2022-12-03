package baseball.domain.entity

import baseball.domain.type.GameStatus

data class BaseballGamePlayHistory(
    val gameId: Long,
    val gameStatus: GameStatus,
    val remainPlays: Long = 0,
    val ball: Long = 0,
    val strike: Long = 0,
) : BaseEntity()
