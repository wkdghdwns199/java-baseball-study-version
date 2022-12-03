package api.interfaces.response

import domain.dto.GameCreationDto

data class GameCreationResponse(
    val gameId: Long?
) {
    companion object {
        fun fromDto(gameCreationDto: GameCreationDto): GameCreationResponse =
            with(gameCreationDto) {
                GameCreationResponse(
                    gameId = gameId
                )
            }
    }
}