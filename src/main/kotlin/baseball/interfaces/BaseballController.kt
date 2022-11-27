package baseball.interfaces

import baseball.application.BaseballService
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest
import baseball.interfaces.response.BaseBallResultResponse
import baseball.interfaces.response.GameCreationResponse
import baseball.interfaces.response.Response

class BaseballController (
    private val baseballService: BaseballService
) {

    /**
     * 새 야구 게임 생성
     */
    fun createGame(): Response<GameCreationResponse> {
        return baseballService.createGame()
            .let { GameCreationResponse.fromDto(it) }
            .let { Response(it) }
    }

    /**
     * 야구 게임 실행
     */
    fun playBaseball(request: BaseballPlayRequest): Response<BaseBallResultResponse> {
        return baseballService.playBaseball(request)
            .let { BaseBallResultResponse.fromDto(it) }
            .let { Response(it) }
    }

    /**
     * 이전 결과 조회
     */
    fun getPreviousBaseballResult(request: BaseballResultRequest): Response<BaseBallResultResponse> {
        return baseballService.getPreviousResult(request)
            .let { BaseBallResultResponse.fromDto(it) }
            .let { Response(it) }
    }

    /**
     * 야구 게임 종료
     */
    fun stopGame(request: GameStopRequest): Response<Any> {
        baseballService.stopGame(request)

        return Response.empty()
    }
}
