package api.view.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseBallScoreResponse(
    val ball: Long? = null,
    val strike: Long? = null
)
