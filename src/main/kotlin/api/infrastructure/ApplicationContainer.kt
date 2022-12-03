package api.infrastructure

import api.application.BaseballApplicationService
import api.interfaces.BaseballController
import api.view.ControllerDispatcher
import com.fasterxml.jackson.databind.ObjectMapper
import domain.repository.InMemoryBaseballGameRepository
import domain.repository.InMemoryBaseballPlayResultRepository
import domain.service.BaseballDomainService
import domain.service.BaseballPolicy
import domain.service.DefaultBaseballPolicy
import domain.service.RandomBaseballGenerator

class ApplicationContainer {
    private val controllerDispatcher: ControllerDispatcher

    init {
        val baseballPolicy: BaseballPolicy = DefaultBaseballPolicy()

        val baseballDomainService = BaseballDomainService(
            baseballGameRepository = InMemoryBaseballGameRepository(),
            baseballGamePlayHistoryRepository = InMemoryBaseballPlayResultRepository(),
            baseballGenerator = RandomBaseballGenerator(baseballPolicy),
            baseballPolicy = baseballPolicy
        )
        val baseballApplicationService = BaseballApplicationService(
            baseballDomainService = baseballDomainService
        )
        val baseballController = BaseballController(
            baseballApplicationService = baseballApplicationService
        )
        this.controllerDispatcher = BaseballControllerDispatcher(
            controller = baseballController,
            exceptionResolver = ExceptionResolver(),
            objectMapper = ObjectMapper()
        )
    }

    fun getControllerDispatcher(): ControllerDispatcher {
        return this.controllerDispatcher
    }

}