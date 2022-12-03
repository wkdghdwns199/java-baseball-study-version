package api

import api.infrastructure.ApplicationContainer
import api.view.ConsoleView
import api.view.ControllerDispatcher
import api.view.InputScanner
import api.view.ScreenPrinter

fun main() {
    val applicationContainer = ApplicationContainer()
    val controllerDispatcher: ControllerDispatcher = applicationContainer.getControllerDispatcher()

    val consoleView = ConsoleView(
        controllerDispatcher = controllerDispatcher,
        screenPrinter = object : ScreenPrinter {}, // fixme 구현체 클래스로 변경
        inputScanner = object : InputScanner {}, // fixme 구현체 클래스로 변경
    )

    consoleView.start()
}