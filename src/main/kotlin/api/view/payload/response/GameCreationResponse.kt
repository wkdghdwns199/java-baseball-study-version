package api.view.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class GameCreationResponse(
    val gameId: Long? = null
)