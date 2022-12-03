package baseball

import baseball.infrastructure.ApplicationContainer
import baseball.infrastructure.ControllerDispatcher
import baseball.view.ConsoleView
import baseball.view.InputScanner
import baseball.view.ScreenPrinter

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