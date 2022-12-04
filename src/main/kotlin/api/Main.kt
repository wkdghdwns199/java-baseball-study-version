package api

import api.infrastructure.ApplicationContainer
import api.view.ConsoleView
import api.view.ControllerDispatcher
import api.view.InputScanner
import api.view.ScreenPrinter
import api.view.payload.response.GamePlayResponse
import com.fasterxml.jackson.databind.ObjectMapper

fun main() {
    val applicationContainer = ApplicationContainer()
    val controllerDispatcher: ControllerDispatcher = applicationContainer.getControllerDispatcher()

    val consoleView = ConsoleView(
        controller = controllerDispatcher,
        objectMapper = ObjectMapper(),
        printer = object : ScreenPrinter {
            override fun printWelcome() {
                TODO("Not yet implemented")
            }

            override fun printGameStart() {
                TODO("Not yet implemented")
            }

            override fun printServiceError() {
                TODO("Not yet implemented")
            }

            override fun print(response: GamePlayResponse) {
                TODO("Not yet implemented")
            }

            override fun printSelectKeepPlaying() {
                TODO("Not yet implemented")
            }
        },
        scanner = object : InputScanner {
            override fun getNumbers(): List<Long> {
                TODO("Not yet implemented")
            }

            override fun getBoolean(): Boolean {
                TODO("Not yet implemented")
            }
        },
    )

    consoleView.start()
}