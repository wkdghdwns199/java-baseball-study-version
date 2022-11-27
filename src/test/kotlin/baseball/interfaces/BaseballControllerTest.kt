package baseball.interfaces

import baseball.application.BaseballService
import baseball.domain.dto.BaseBallResultDto
import baseball.domain.dto.BaseBallScore
import baseball.domain.dto.GameCreationDto
import baseball.interfaces.request.BaseballPlayRequest
import baseball.interfaces.request.BaseballResultRequest
import baseball.interfaces.request.GameStopRequest
import baseball.interfaces.response.ResponseStatus
import io.github.serpro69.kfaker.Faker
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
    val baseballService: BaseballService = mockk(relaxed = true)
    val baseballController: BaseballController = BaseballController(baseballService)

    val faker: Faker = Faker()

    "createGame: 게임 생성" - {
        "게임이 생성에 성공하여 gameId를 반환한다." {
            val gameId: String = "${faker.idNumber}"
            every { baseballService.createGame() } returns GameCreationDto(gameId)

            val result = baseballController.createGame()

            assertSoftly {
                verify(exactly = 1) { baseballService.createGame() }
                result.status shouldBe ResponseStatus.SUCCESS
                result.data?.gameId shouldBe gameId
            }
        }

        "게임 생성에 실패하면 예외를 반환한다." {
            every { baseballService.createGame() } throws IllegalArgumentException()

            val exception = shouldThrow<IllegalArgumentException> {
                baseballController.createGame()
            }
        }
    }


    "playBaseball: 야구 게임 실행" - {
        "게임 실행에 성공하여 실행한 게임 결과를 반환한다." {
            listOf(
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 5, BaseBallScore(ball = 0, strike = 0)),
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 4, BaseBallScore(ball = 0, strike = 1)),
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 3, BaseBallScore(ball = 0, strike = 2)),
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 2, BaseBallScore(ball = 1, strike = 1)),
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 1, BaseBallScore(ball = 2, strike = 1)),
                BaseBallResultDto("${faker.idNumber}", remainingPlays = 0, BaseBallScore(ball = 0, strike = 3)),
            ).forAll { dto: BaseBallResultDto ->
                val request = BaseballPlayRequest(gameId = dto.gameId)
                every { baseballService.playBaseball(request) } returns dto

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
            val request = BaseballPlayRequest(gameId = "${faker.idNumber}")
            every { baseballService.playBaseball(any()) } throws IllegalStateException()

            val exception = shouldThrowAny {
                baseballController.playBaseball(request)
            }
        }

        "잘못된 gameId를 입력하면 예외를 반환한다" {
            listOf(
                null,
                "",
                "          ",
                "잘못된 아이디"
            ).forAll { gameId: String? ->
                val request = BaseballPlayRequest(gameId)
                every { baseballService.playBaseball(any()) } throws IllegalArgumentException()

                val exception = shouldThrow<IllegalArgumentException> {
                    baseballController.playBaseball(request)
                }
            }
        }
    }


    "getPreviousBaseballResult: 이전 결과 조회" - {
        "이전 결과 조회에 성공 한다." {
            val dto = BaseBallResultDto("${faker.idNumber}", remainingPlays = 5, BaseBallScore(ball = 0, strike = 0))
            val request = BaseballResultRequest(gameId = dto.gameId)
            every { baseballService.getPreviousResult(request) } returns dto

            val result = baseballController.getPreviousBaseballResult(request)

            result.status shouldBe ResponseStatus.SUCCESS
            result.data?.gameId shouldBe dto.gameId
            result.data?.score?.ball shouldBe dto.baseballScore.ball
            result.data?.score?.strike shouldBe dto.baseballScore.strike
        }

        "잘못된 gameId를 입력하면 예외를 반환한다" {
            listOf(
                null,
                "",
                "          ",
                "잘못된 아이디"
            ).forAll { gameId: String? ->
                val request = BaseballResultRequest(gameId = gameId)
                every { baseballService.getPreviousResult(request) } throws IllegalArgumentException()

                val exception = shouldThrow<IllegalArgumentException> {
                    baseballController.getPreviousBaseballResult(request)
                }
            }
        }
    }


    "stopGame: 야구 게임 종료" - {
        "gameId에 해당하는 야구 게임을 종료한다." {
            val gameId: String = "${faker.idNumber}"
            val request = GameStopRequest(gameId)

            val result = baseballController.stopGame(request)

            verify(exactly = 1) { baseballService.stopGame(request) }
            result.data shouldBe null
        }

        "게임 종료에 실패하면 예외를 반환한다." {
            val gameId: String = "${faker.idNumber}"
            val request = GameStopRequest(gameId)
            every { baseballService.stopGame(request) } throws IllegalStateException()

            shouldThrowAny {
                baseballController.stopGame(request)
            }
        }

    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest
}