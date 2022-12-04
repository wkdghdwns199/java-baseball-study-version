package api.view.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class GamePlayResponse(
    val gameId: Long? = null,
    val remainingPlays: Long? = null,
    val score: BaseBallScoreResponse? = null,
    val isGameEnd: Boolean? = null
)
