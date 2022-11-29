package baseball.application

import baseball.domain.dto.BaseBallResultDto
import baseball.domain.dto.GameCreationDto
import baseball.domain.entity.Baseball
import baseball.domain.service.BaseballDomainService
import baseball.exception.EntityNotFoundException
import baseball.exception.ErrorCode
import baseball.exception.ServiceException
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest

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

        return baseballDomainService.play(
            gameId = request.gameId.toLong(),
            userBaseball = request.baseballNumbers.toBaseball()
        )
    }

    fun getPreviousResult(request: BaseballResultRequest): BaseBallResultDto {
        require(request.gameId != null) { "gameId가 유효하지 않습니다." }

        return baseballDomainService.getPreviousResultOrNull(request.gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_HISTORY_NOT_FOUND)
    }

    fun stopGame(request: GameStopRequest) {
        require(request.gameId != null) { "gameId가 유효하지 않습니다." }

        baseballDomainService.stopGame(request.gameId)
    }


    private fun List<Long>?.toBaseball(): Baseball {
        if (this?.size != 3) throw ServiceException(ErrorCode.GAME_SERVICE_ERROR, "request -> baseball 변환 실패")
        return Baseball.of(this[0], this[1], this[2])
    }
}

