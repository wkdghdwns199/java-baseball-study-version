package api.interfaces.response

/**
 * 공통 응답 포맷
 *
 * @param T Response Data Type
 * @property status Response status
 * @property data Response Data
 * @property message Response Message
 */
class Response<T>(
    val status: ResponseStatus,
    val data: T?,
    val message: String
) {
    constructor(data: T?, status: ResponseStatus = ResponseStatus.SUCCESS) :
            this(status = status, data = data, message = status.message)

    companion object {

        fun empty(
            status: ResponseStatus = ResponseStatus.SUCCESS,
            message: String? = null
        ): Response<Any> =
            Response(status = status, data = null, message = message ?: status.message)

        fun error(
            status: ResponseStatus = ResponseStatus.SERVER_ERROR,
            message: String? = null
        ): Response<Any> =
            Response(status = status, data = null, message = message ?: status.message)
    }
}

enum class ResponseStatus(val message: String) {
    SUCCESS(message = "성공"),
    BAD_REQUEST(message = "잘못된 요청 값"),
    SERVICE_NOT_FOUND(message = "존재하지 않는 서비스 요청"),
    SERVER_ERROR(message = "서버 에러"),
    DOMAIN_ERROR(message = "도메인 로직 에러")
}
