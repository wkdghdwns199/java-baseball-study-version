package api.infrastructure

import api.interfaces.BaseballController
import api.interfaces.request.BaseballPlayRequest
import api.interfaces.request.BaseballResultRequest
import api.interfaces.request.GameStopRequest
import api.interfaces.response.Response
import api.interfaces.response.ResponseStatus
import api.view.ControllerDispatcher
import api.view.payload.*
import com.fasterxml.jackson.databind.ObjectMapper

class BaseballControllerDispatcher(
    private val controller: BaseballController,
    private val exceptionResolver: ExceptionResolver,
    private val objectMapper: ObjectMapper
) : ControllerDispatcher {

    override fun run(input: ViewInput): String? {
        val response: Response<out Any> =
            try {
                when (input) {
                    is GamePlayInput ->
                        controller.playBaseball(BaseballPlayRequest(input.gameId, input.inputNumbers))
                    is GameStopInput ->
                        controller.stopGame(GameStopRequest(input.gameId))
                    is PreviousPlayResultInput ->
                        controller.getPreviousBaseballResult(BaseballResultRequest(input.gameId))
                    is GameCreationInput ->
                        controller.createGame()
                    else ->
                        Response.error(status = ResponseStatus.SERVICE_NOT_FOUND)
                }
            } catch (ex: Exception) {
                exceptionResolver.resolve(ex)
            }

        return objectMapper.writeValueAsString(response)
    }

}