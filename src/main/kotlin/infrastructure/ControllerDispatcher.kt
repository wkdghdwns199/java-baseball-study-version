package infrastructure

import baseball.interfaces.BaseballController
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest
import baseball.interfaces.response.Response
import baseball.interfaces.response.ResponseStatus
import view.ControllerDispatcher
import com.fasterxml.jackson.databind.ObjectMapper
import view.payload.*

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