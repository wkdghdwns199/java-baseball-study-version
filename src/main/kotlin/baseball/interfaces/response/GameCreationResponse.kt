package baseball.interfaces.response

import baseball.domain.dto.GameCreationDto

data class GameCreationResponse(
    val gameId: String?
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