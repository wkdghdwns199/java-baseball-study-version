package api.interfaces

import api.application.BaseballApplicationService
import api.interfaces.request.BaseballPlayRequest
import api.interfaces.request.BaseballResultRequest
import api.interfaces.request.GameStopRequest
import api.interfaces.response.BaseBallResultResponse
import api.interfaces.response.GameCreationResponse
import api.interfaces.response.Response

class BaseballController(
    private val baseballApplicationService: BaseballApplicationService
) {

    /** 새 야구 게임 생성 */
    fun createGame(): Response<GameCreationResponse> {
        return baseballApplicationService.createGame()
            .let { GameCreationResponse.fromDto(it) }
            .let { Response(it) }
    }

    /** 야구 게임 실행 */
    fun playBaseball(request: BaseballPlayRequest): Response<BaseBallResultResponse> {
        return baseballApplicationService.playBaseball(request)
            .let { BaseBallResultResponse.fromDto(it) }
            .let { Response(it) }
    }

    /** 이전 결과 조회 */
    fun getPreviousBaseballResult(request: BaseballResultRequest): Response<BaseBallResultResponse> {
        return baseballApplicationService.getPreviousResult(request)
            .let { BaseBallResultResponse.fromDto(it) }
            .let { Response(it) }
    }

    /** 야구 게임 종료 */
    fun stopGame(request: GameStopRequest): Response<Any> {
        baseballApplicationService.stopGame(request)

        return Response.empty()
    }
}
