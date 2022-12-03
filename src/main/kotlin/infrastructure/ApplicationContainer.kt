package infrastructure

import baseball.application.BaseballApplicationService
import baseball.domain.repository.InMemoryBaseballGameRepository
import baseball.domain.repository.InMemoryBaseballPlayResultRepository
import baseball.domain.service.BaseballDomainService
import baseball.domain.service.BaseballPolicy
import baseball.domain.service.DefaultBaseballPolicy
import baseball.domain.service.RandomBaseballGenerator
import baseball.interfaces.BaseballController

import com.fasterxml.jackson.databind.ObjectMapper
import view.ControllerDispatcher

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