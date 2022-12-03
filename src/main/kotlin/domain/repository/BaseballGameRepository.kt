package domain.repository

import domain.entity.BaseballGame
import java.util.concurrent.ConcurrentHashMap

interface BaseballGameRepository {
    fun save(entity: BaseballGame): BaseballGame

    fun findById(gameId: Long): BaseballGame?
}

internal class InMemoryBaseballGameRepository : BaseballGameRepository {
    private val dataMap: MutableMap<Long, BaseballGame> = ConcurrentHashMap()

    override fun save(entity: BaseballGame): BaseballGame {
        dataMap[entity.id] = entity
        return entity
    }

    override fun findById(id: Long): BaseballGame? {
        return dataMap[id]
    }

}