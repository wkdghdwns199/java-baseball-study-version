package baseball.interfaces.request

data class BaseballPlayRequest(
    val gameId: Long?,
    val baseballNumbers: List<Long>?
)
