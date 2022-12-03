package api.application

import api.interfaces.request.BaseballPlayRequest
import api.interfaces.request.BaseballResultRequest
import api.interfaces.request.GameStopRequest
import common.exception.EntityNotFoundException
import common.exception.ErrorCode
import domain.dto.BaseBallResultDto
import domain.dto.GameCreationDto
import domain.service.BaseballDomainService

/**
 * 도메인 서비스를 이용하여 어플리케이션을 구현합니다. 어플리케이션에 관련된 관심사(입력 유효성 검사, 캐시, 외부 서비스 연동)등을
 * 담당합니다.
 */
class BaseballApplicationService(
    private val baseballDomainService: BaseballDomainService
) {
    fun createGame(): GameCreationDto {
        return baseballDomainService.createGame()
    }

    fun playBaseball(request: BaseballPlayRequest): BaseBallResultDto {
        require(request.gameId != null) { "gameId가 유효하지 않습니다." }
        require(!request.baseballNumbers.isNullOrEmpty()) { "게임을 할 숫자를 입력해야 합니다." }
        require(request.baseballNumbers.size == 3) { "숫자는 총 3개를 정확하게 입력해야합니다." }

        return baseballDomainService.playGame(
            gameId = request.gameId.toLong(),
            userBaseball = request.baseballNumbers
        )
    }

    fun getPreviousResult(request: BaseballResultRequest): BaseBallResultDto {
        require(request.gameId != null) { "gameId가 유효하지 않습니다." }

        return baseballDomainService.getPreviousPlayResultOrNull(request.gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_PLAY_HISTORY_NOT_FOUND)
    }

    fun stopGame(request: GameStopRequest) {
        require(request.gameId != null) { "gameId가 유효하지 않습니다." }

        baseballDomainService.stopGame(request.gameId)
    }

}

