package baseball.domain.service

import baseball.domain.repository.BaseBallGameHistoryRepository
import baseball.domain.repository.BaseballGameRepository
import baseball.domain.service.BaseballGenerator
import baseball.domain.service.BaseballPolicy
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockk

public class BaseballDomainServiceTest : FreeSpec({
val baseballGameRepository: BaseballGameRepository = mockk(relaxed = true)
val baseballGameHistoryRepository: BaseBallGameHistoryRepository = mockk(relaxed = true)
val baseballGenerator: BaseballGenerator = mockk(relaxed = true)
val baseballPolicy: BaseballPolicy = mockk(relaxed = true)

    // TODO - 도메인 서비스 테스트 검증
})
