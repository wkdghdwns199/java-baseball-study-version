package baseball.view

import baseball.infrastructure.ControllerDispatcher

class ConsoleView(
    private val controllerDispatcher: ControllerDispatcher,
    private val screenPrinter: ScreenPrinter,
    private val inputScanner: InputScanner,
) {
    fun getInput() {
        // TODO 화면 그리기 & 사용자 입력 받기
    }
}