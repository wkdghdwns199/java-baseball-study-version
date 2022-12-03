package baseball.domain.repository

import baseball.domain.entity.BaseballGamePlayHistory
import java.util.concurrent.ConcurrentHashMap

interface BaseballPlayResultRepository {
    fun save(entity: BaseballGamePlayHistory): BaseballGamePlayHistory

    fun findRecentPlayHistoryByGameId(gameId: Long): BaseballGamePlayHistory?
}

internal class InMemoryBaseballPlayResultRepository : BaseballPlayResultRepository {
    private val dataMap: MutableMap<Long, BaseballGamePlayHistory> = ConcurrentHashMap()

    override fun save(entity: BaseballGamePlayHistory): BaseballGamePlayHistory {
        dataMap[entity.id] = entity
        return entity
    }

    override fun findRecentPlayHistoryByGameId(gameId: Long): BaseballGamePlayHistory? {
        return dataMap.values
            .filter { it.gameId == gameId }
            .maxByOrNull { it.modifiedAt }
    }
}

