package baseball.application

import baseball.domain.dto.BaseBallResultDto
import baseball.domain.dto.GameCreationDto
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest


class BaseballService {
    fun createGame(): GameCreationDto {
        TODO("Not yet implemented")
    }

    fun playBaseball(request: BaseballPlayRequest): BaseBallResultDto {
        TODO("Not yet implemented")
    }

    fun getPreviousResult(request: BaseballResultRequest): BaseBallResultDto {
        TODO("Not yet implemented")
    }

    fun stopGame(request: GameStopRequest): GameStopRequest {
        TODO("Not yet implemented")
    }

}
