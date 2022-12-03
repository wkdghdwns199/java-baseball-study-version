package view

import view.payload.ViewInput

interface ControllerDispatcher {
    fun run(input: ViewInput): String?
}