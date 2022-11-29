package baseball.domain.repository

import baseball.domain.entity.BaseballGame

interface BaseballGameRepository {
    fun save(entity: BaseballGame): BaseballGame

    fun findByGameId(gameId: Long): BaseballGame?
}