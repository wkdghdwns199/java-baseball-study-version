package api.infrastructure

import api.interfaces.response.Response
import api.interfaces.response.ResponseStatus
import common.exception.*

class ExceptionResolver {

    fun resolve(exception: Exception): Response<Any> {
        return when (exception) {
            is ServiceException -> handleServiceException(exception)
            is DomainServiceException -> handleDomainServiceException(exception)
            is EntityNotFoundException -> handleEntityNotFoundException(exception)
            is EntityCreationFailException -> handleEntityCreationFailException(exception)
            is EntityUpdateFailException -> handleEntityUpdateFailException(exception)
            is CommonException -> handleCommonException(exception)
            else -> handleUnknownException(exception)
        }
    }

    private fun handleUnknownException(ex: Exception): Response<Any> {
        return Response.empty(
            status = ResponseStatus.SERVER_ERROR,
            message = "알수없는 에러가 발생했습니다. (${ex.localizedMessage}"
        )
    }

    private fun handleServiceException(ex: ServiceException): Response<Any> {
        return Response.error(
            status = ResponseStatus.SERVER_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun handleDomainServiceException(ex: DomainServiceException): Response<Any> {
        return Response.error(
            status = ResponseStatus.DOMAIN_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun handleEntityNotFoundException(ex: EntityNotFoundException): Response<Any> {
        return Response.error(
            status = ResponseStatus.DOMAIN_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun handleEntityCreationFailException(ex: EntityCreationFailException): Response<Any> {
        return Response.error(
            status = ResponseStatus.DOMAIN_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun handleEntityUpdateFailException(ex: EntityUpdateFailException): Response<Any> {
        return Response.error(
            status = ResponseStatus.DOMAIN_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun handleCommonException(ex: CommonException): Response<Any> {
        return Response.error(
            status = ResponseStatus.SERVER_ERROR,
            message = ex.getErrorMessage()
        )
    }

    private fun CommonException.getErrorMessage(): String {
        return "[${this.errorCode.code}] ${this.errorCode.message}"
    }
}