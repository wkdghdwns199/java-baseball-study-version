package baseball.domain.repository

import baseball.domain.entity.PlayResult

interface BaseBallGameHistoryRepository {
    fun save(entity: PlayResult): PlayResult

    fun findRecentResultByGameId(gameId: Long): PlayResult?
}
