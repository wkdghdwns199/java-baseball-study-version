package view.payload

interface ViewInput {
    val type: ViewType
}

data class GamePlayInput(
    val gameId: Long?,
    val inputNumbers: List<Long>?,
    override val type: ViewType = ViewType.CONSOLE
) : ViewInput

data class GameStopInput(
    val gameId: Long?,
    override val type: ViewType = ViewType.CONSOLE
) : ViewInput

data class PreviousPlayResultInput(
    val gameId: Long?,
    override val type: ViewType = ViewType.CONSOLE
) : ViewInput

data class GameCreationInput(
    override val type: ViewType = ViewType.CONSOLE,
) : ViewInput
