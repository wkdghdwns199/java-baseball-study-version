package api.application

import domain.service.BaseballDomainService
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockk

public class BaseballApplicationServiceTest : FreeSpec({
    val baseballDomainService: BaseballDomainService = mockk(relaxed = true)

    // TODO - 통합테스트 작성

})
