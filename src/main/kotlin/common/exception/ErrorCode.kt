package common.exception


enum class ErrorCode(
    val code: String,
    val message: String,
) {
    GAME_SERVICE_ERROR("10001", "서비스에 장애가 발생했습니다. %s"),

    GAME_NOT_FOUND("20001", "해당 게임을 찾을 수 없습니다. %s"),
    GAME_CANNOT_PLAY("20002", "해당 게임을 실행할 수 없습니다. %s"),
    GAME_CANNOT_STOP("20003", "해당 게임을 중지할 수 없습니다. %s"),

    GAME_PLAY_HISTORY_NOT_FOUND("30001", "해당 게임의 진행결과가 없습니다. %s"),
}
