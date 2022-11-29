package baseball.domain.entity

import java.util.concurrent.atomic.AtomicLong

data class PlayResult(
    val gameId: Long,
    val gameResult: GameResult,
    val remainPlays: Long,
    val ball: Long,
    val strike: Long,
) : BaseEntity() {
    val id: Long = idGenerator.incrementAndGet()

    companion object {
        private val idGenerator = AtomicLong()
    }
}

enum class GameResult {
    PLAYING_GAME, GAME_OVER, WIN
}
