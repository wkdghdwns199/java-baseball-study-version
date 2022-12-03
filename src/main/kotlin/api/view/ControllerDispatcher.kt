package api.view

import api.view.payload.ViewInput

interface ControllerDispatcher {
    fun run(input: ViewInput): String?
}