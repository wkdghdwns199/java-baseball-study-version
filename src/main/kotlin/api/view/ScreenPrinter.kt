package api.view

import api.view.payload.response.GamePlayResponse

interface ScreenPrinter {
    fun printWelcome()
    fun printGameStart()
    fun printServiceError()
    fun print(response: GamePlayResponse)
    fun printSelectKeepPlaying()

}
