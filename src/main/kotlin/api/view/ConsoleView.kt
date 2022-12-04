package api.view

import api.view.payload.GameCreationInput
import api.view.payload.GamePlayInput
import api.view.payload.response.GameCreationResponse
import api.view.payload.response.GamePlayResponse
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json

class ConsoleView(
    private val controller: ControllerDispatcher,
    private val objectMapper: ObjectMapper,
    private val printer: ScreenPrinter,
    private val scanner: InputScanner,
) {
    fun start() {
        // TODO API 호출 분리
        printer.printWelcome()

        var isGameStop = false
        var gameId: Long = createGameOrNull() ?: return

        while (isGameStop == false) {
            while (playGame(gameId));
            if (selectPlayNewGame())
                gameId = createGameOrNull() ?: return
            else
                isGameStop = true
        }

    }

    private fun createGameOrNull(): Long? {
        val response: GameCreationResponse? = controller
            .run(GameCreationInput())
            ?.let { Json.decodeFromString(GameCreationResponse.serializer(), it) }

        if (response?.gameId == null) {
            printer.printServiceError()
            return null
        }

        return response.gameId
    }

    private fun selectPlayNewGame(): Boolean {
        printer.printSelectKeepPlaying()
        return scanner.getBoolean()
    }

    private fun playGame(gameId: Long): Boolean {
        printer.printGameStart()
        val inputNumbers = scanner.getNumbers()

        val response: GamePlayResponse = controller
            .run(GamePlayInput(gameId = gameId, inputNumbers = inputNumbers))
            ?.let { Json.decodeFromString(GamePlayResponse.serializer(), it) }
            ?: return false

        printer.print(response)

        return response.isGameEnd ?: false
    }
}
