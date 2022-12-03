package baseball.exception

sealed class CommonException(
    open val errorCode: ErrorCode,
    override val message: String?,
) : RuntimeException(message)

class ServiceException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
) : CommonException(errorCode, errorCode.message.format(message ?: ""))

class DomainServiceException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
) : CommonException(errorCode, errorCode.message.format(message ?: ""))

class EntityNotFoundException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
) : CommonException(errorCode, errorCode.message.format(message ?: ""))

class EntityCreationFailException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
) : CommonException(errorCode, errorCode.message.format(message ?: ""))

class EntityUpdateFailException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
) : CommonException(errorCode, errorCode.message.format(message ?: ""))