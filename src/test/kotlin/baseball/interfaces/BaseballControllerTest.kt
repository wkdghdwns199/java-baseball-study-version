package baseball.interfaces

import baseball.application.BaseballApplicationService
import baseball.domain.dto.BaseBallResultDto
import baseball.domain.dto.BaseBallResultDto.*
import baseball.domain.dto.GameCreationDto
import baseball.domain.type.GameStatus.*
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest
import baseball.interfaces.response.ResponseStatus
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BaseballControllerTest : FreeSpec({
    val baseballApplicationService: BaseballApplicationService = mockk(relaxed = true)
    val baseballController: BaseballController = BaseballController(baseballApplicationService)

    "createGame: 게임 생성" - {
        "게임이 생성에 성공하여 gameId를 반환한다." {
            val gameId: Long = 1
            every { baseballApplicationService.createGame() } returns GameCreationDto(gameId)

            val result = baseballController.createGame()

            assertSoftly {
                verify(exactly = 1) { baseballApplicationService.createGame() }
                result.status shouldBe ResponseStatus.SUCCESS
                result.data?.gameId shouldBe gameId
            }
        }

        "게임 생성에 실패하면 예외를 반환한다." {
            every { baseballApplicationService.createGame() } throws IllegalArgumentException()

            val exception = shouldThrow<IllegalArgumentException> {
                baseballController.createGame()
            }
        }
    }


    "playBaseball: 야구 게임 실행" - {
        "게임 실행에 성공하여 실행한 게임 결과를 반환한다." {
            listOf(
                BaseBallResultDto(1, remainingPlays = 5, BaseBallScore(ball = 0, strike = 0), ACTIVE),
                BaseBallResultDto(2, remainingPlays = 4, BaseBallScore(ball = 0, strike = 1), ACTIVE),
                BaseBallResultDto(3, remainingPlays = 3, BaseBallScore(ball = 0, strike = 2), ACTIVE),
                BaseBallResultDto(4, remainingPlays = 2, BaseBallScore(ball = 1, strike = 1), ACTIVE),
                BaseBallResultDto(5, remainingPlays = 1, BaseBallScore(ball = 2, strike = 1), ACTIVE),
                BaseBallResultDto(6, remainingPlays = 0, BaseBallScore(ball = 0, strike = 3), ACTIVE),
            ).forAll { dto: BaseBallResultDto ->
                val request = BaseballPlayRequest(gameId = dto.gameId, emptyList())
                every { baseballApplicationService.playBaseball(request) } returns dto

                val result = baseballController.playBaseball(request)

                assertSoftly {
                    result.status shouldBe ResponseStatus.SUCCESS
                    result.data?.gameId shouldBe dto.gameId
                    result.data?.score?.ball shouldBe dto.baseballScore.ball
                    result.data?.score?.strike shouldBe dto.baseballScore.strike
                }
            }
        }

        "게임 실행에 실패하면 예외를 반환한다" {
            val request = BaseballPlayRequest(gameId = 1, emptyList())
            every { baseballApplicationService.playBaseball(any()) } throws IllegalStateException()

            val exception = shouldThrowAny {
                baseballController.playBaseball(request)
            }
        }

        "잘못된 gameId를 입력하면 예외를 반환한다" {
            listOf(
                null,
                1L
            ).forAll { gameId: Long? ->
                val request = BaseballPlayRequest(gameId, emptyList())
                every { baseballApplicationService.playBaseball(any()) } throws IllegalArgumentException()

                val exception = shouldThrow<IllegalArgumentException> {
                    baseballController.playBaseball(request)
                }
            }
        }
    }


    "getPreviousBaseballResult: 이전 결과 조회" - {
        "이전 결과 조회에 성공 한다." {
            val dto = BaseBallResultDto(1, remainingPlays = 5, BaseBallScore(ball = 0, strike = 0), ACTIVE)
            val request = BaseballResultRequest(gameId = dto.gameId)
            every { baseballApplicationService.getPreviousResult(request) } returns dto

            val result = baseballController.getPreviousBaseballResult(request)

            result.status shouldBe ResponseStatus.SUCCESS
            result.data?.gameId shouldBe dto.gameId
            result.data?.score?.ball shouldBe dto.baseballScore.ball
            result.data?.score?.strike shouldBe dto.baseballScore.strike
        }

        "잘못된 gameId를 입력하면 예외를 반환한다" {
            listOf(
                null,
                1L
            ).forAll { gameId: Long? ->
                val request = BaseballResultRequest(gameId = gameId)
                every { baseballApplicationService.getPreviousResult(request) } throws IllegalArgumentException()

                val exception = shouldThrow<IllegalArgumentException> {
                    baseballController.getPreviousBaseballResult(request)
                }
            }
        }
    }


    "stopGame: 야구 게임 종료" - {
        "gameId에 해당하는 야구 게임을 종료한다." {
            val gameId: Long = 1
            val request = GameStopRequest(gameId)

            val result = baseballController.stopGame(request)

            verify(exactly = 1) { baseballApplicationService.stopGame(request) }
            result.data shouldBe null
        }

        "게임 종료에 실패하면 예외를 반환한다." {
            val gameId: Long = 1
            val request = GameStopRequest(gameId)
            every { baseballApplicationService.stopGame(request) } throws IllegalStateException()

            shouldThrowAny {
                baseballController.stopGame(request)
            }
        }

    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest
}