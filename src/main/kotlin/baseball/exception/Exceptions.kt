package baseball.exception

sealed class CommonException(
    open val code: ErrorCode,
    override val message: String?,
) : RuntimeException(message)

class ServiceException(
    override val code: ErrorCode,
    override val message: String? = null,
) : CommonException(code, code.message.format(message ?: ""))

class DomainServiceException(
    override val code: ErrorCode,
    override val message: String? = null,
) : CommonException(code, code.message.format(message ?: ""))

class EntityNotFoundException(
    override val code: ErrorCode,
    override val message: String? = null,
) : CommonException(code, code.message.format(message ?: ""))

class EntityCreationFailException(
    override val code: ErrorCode,
    override val message: String? = null,
) : CommonException(code, code.message.format(message ?: ""))

class EntityUpdateFailException(
    override val code: ErrorCode,
    override val message: String? = null,
) : CommonException(code, code.message.format(message ?: ""))