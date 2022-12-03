package baseball.domain.service

import baseball.domain.entity.Baseball
import baseball.domain.entity.BaseballGame
import baseball.domain.entity.BaseballGamePlayHistory
import baseball.domain.repository.BaseballPlayResultRepository
import baseball.domain.repository.BaseballGameRepository
import baseball.domain.type.GameStatus
import baseball.exception.DomainServiceException
import baseball.exception.EntityNotFoundException
import io.kotest.assertions.asClue
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.fail

class BaseballDomainServiceTest : FreeSpec({
    val baseballGameRepository: BaseballGameRepository = mockk(relaxed = true)
    val baseballPlayResultRepository: BaseballPlayResultRepository = mockk(relaxed = true)
    val baseballPolicy: BaseballPolicy = DefaultBaseballPolicy()
    val baseballGenerator: BaseballGenerator = RandomBaseballGenerator(baseballPolicy)

    val baseballService = BaseballDomainService(
        baseballGameRepository = baseballGameRepository,
        baseballGamePlayHistoryRepository = baseballPlayResultRepository,
        baseballGenerator = baseballGenerator,
        baseballPolicy = baseballPolicy
    )

    "createGame: 새로운 게임 생성" - {
        every { baseballGameRepository.save(any()) } returnsArgument 0

        "게임 생성에 성공 한다." {
            val result = baseballService.createGame()

            result.gameId shouldNotBe 0
        }
    }

    "playGame: 유저 게임 실행" - {
        val gameId = 1L
        val answerBaseball = listOf(1L, 2L, 3L)
        every { baseballPlayResultRepository.save(any()) } returnsArgument 0


        "살향 후 ball, strike 로직 검증" {

            every { baseballGameRepository.findById(gameId) } answers {
                BaseballGame(Baseball(answerBaseball), remainingPlays = 4)
            }

            listOf(
                // userBaseball to Pair(ball, strike)
                answerBaseball to Pair(0L, 3L),
                listOf(1L, 1L, 1L) to Pair(2L, 1L),
                listOf(4L, 4L, 4L) to Pair(0L, 0L),
                listOf(3L, 1L, 2L) to Pair(3L, 0L),
                listOf(1L, 2L, 4L) to Pair(0L, 2L),
                listOf(1L, 2L, 1L) to Pair(1L, 2L),
                listOf(1L, 3L, 2L) to Pair(2L, 1L),
                listOf(3L, 2L, 3L) to Pair(1L, 2L),
            ).forAll { (baseball: List<Long>, expectedScore: Pair<Long, Long>) ->
                val result = baseballService.playGame(gameId, baseball)

                result.baseballScore.asClue {
                    it.ball shouldBe expectedScore.first
                    it.strike shouldBe expectedScore.second
                }
            }
        }

        "실행 후 게임 상태 검증" {

            every { baseballGameRepository.findById(gameId) } answers {
                BaseballGame(Baseball(listOf(1, 2, 3)), remainingPlays = 4)
            }

            listOf(
                // userBaseball to Pair(ball, strike)
                listOf(1L, 2L, 3L) to GameStatus.GAME_WIN,
                listOf(1L, 3L, 2L) to GameStatus.ACTIVE,
                listOf(1L, 1L, 1L) to GameStatus.ACTIVE,
                listOf(4L, 4L, 4L) to GameStatus.ACTIVE,
                listOf(3L, 1L, 2L) to GameStatus.ACTIVE,
                listOf(1L, 2L, 4L) to GameStatus.ACTIVE,
                listOf(1L, 2L, 1L) to GameStatus.ACTIVE,
                listOf(3L, 2L, 3L) to GameStatus.ACTIVE,
            ).forAll { (baseball: List<Long>, gameStatus: GameStatus) ->
                val result = baseballService.playGame(gameId, baseball)

                result.asClue {
                    it.gameStatus shouldBe gameStatus
                }
            }
        }

        "남은 플레이 횟수 검증" - {
            val game = BaseballGame(Baseball(answerBaseball), remainingPlays = 5)
            every { baseballGameRepository.findById(gameId) } returnsMany ((0..5).map { game })

            "게임을 최대 횟수를 다 사용했다면 GAME_OVER 를 반환한다." {
                listOf(
                    // userBaseball to Pair(ball, strike)
                    listOf(1L, 3L, 2L) to GameStatus.ACTIVE,
                    listOf(1L, 1L, 1L) to GameStatus.ACTIVE,
                    listOf(4L, 4L, 4L) to GameStatus.ACTIVE,
                    listOf(3L, 1L, 2L) to GameStatus.ACTIVE,
                    listOf(1L, 2L, 4L) to GameStatus.GAME_OVER,
                ).forAll { (baseball: List<Long>, gameStatus: GameStatus) ->
                    val result = baseballService.playGame(gameId, baseball)

                    result.asClue {
                        it.gameStatus shouldBe gameStatus
                    }
                }
            }

            "남은 실행 횟수가 없다면 정답 여부와 상관 없이 예외를 반환한다. " {
                shouldThrow<DomainServiceException> {
                    baseballService.playGame(gameId, answerBaseball)
                }
            }
        }


        "게임을 찾을 수 없다면 예외를 반환한다" {
            every { baseballGameRepository.findById(any()) } returns null

            shouldThrow<EntityNotFoundException> {
                baseballService.playGame(gameId, answerBaseball)
            }
        }

        "게임 상태가 ACTIVE 가 아니라면 예외를 반환한다." {
            every {
                baseballGameRepository.findById(any())
            } returnsMany (listOf(
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.STOP_IN_PLAYING),
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.GAME_OVER),
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.GAME_WIN),
            ))

            (1..3).toList()
                .forAll {
                    shouldThrow<DomainServiceException> {
                        baseballService.playGame(gameId, answerBaseball)
                    }
                }
        }

        "게임 입력 값이 유효하지 않으면 예외를 반환한다" {
            val game = BaseballGame(Baseball(answerBaseball), remainingPlays = 5)
            every { baseballGameRepository.findById(gameId) } returnsMany ((0..5).map { game })

            listOf<List<Long>>(
                listOf(1L, 2L, 3L, 4L),
                listOf(1L),
                listOf(1L, 2L),
                listOf(1L, 2L, 3L, 4L, 5L),
                listOf(-1L, -2L, -3L),
                listOf(-1L, 2L, 3L),
                listOf(-1L, -2L, -3L, -4L),
                listOf(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 3L),
            ).forAll { wrongInput: List<Long> ->

                shouldThrow<IllegalArgumentException> {
                    baseballService.playGame(gameId, wrongInput)
                }
            }
        }


    }

    "getPreviousPlayResultOrNull: 이전 게임결과 조회" - {
        val gameId = 1L
        val game = BaseballGamePlayHistory(gameId, GameStatus.ACTIVE, 5, 0, 0)

        "결과 조회에 성공한다." {
            every { baseballPlayResultRepository.findRecentPlayHistoryByGameId(gameId) } answers { game }

            val result = baseballService.getPreviousPlayResultOrNull(gameId)
                ?: fail { "result is null" }

            result.asClue {
                it.gameId shouldBe game.gameId
                it.gameStatus shouldBe game.gameStatus
                it.baseballScore.ball shouldBe game.ball
                it.baseballScore.strike shouldBe game.strike
                it.remainingPlays shouldBe game.remainPlays
            }

        }

        "결과가 존재하지 않는다면 null을 반환한다." {
            every { baseballPlayResultRepository.findRecentPlayHistoryByGameId(gameId) } answers { null }

            val result = baseballService.getPreviousPlayResultOrNull(gameId)

            result.asClue {
                it shouldBe null
            }
        }

    }

    "stopGame: 게임 강제 중지" - {
        val gameId = 1L
        val answerBaseball = listOf(1L, 2L, 3L)

        "게임 중지에 성공한다" {
            val game = BaseballGame(Baseball(answerBaseball), 5, GameStatus.ACTIVE)
            every { baseballGameRepository.findById(gameId) } returns game

            baseballService.stopGame(gameId)

            verify(exactly = 1) { baseballGameRepository.save(game) }
            game.isGameOver shouldBe true
        }


        "게임이 존재하지 않으면 예외를 반환한다." {
            every { baseballGameRepository.findById(gameId) } returns null

            shouldThrow<EntityNotFoundException> {
                baseballService.stopGame(gameId)
            }
        }


        "이미 종료된 게임을 중지하면 예외를 반환한다." {

            every { baseballGameRepository.findById(gameId) } returnsMany (listOf(
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.GAME_WIN),
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.GAME_OVER),
                BaseballGame(Baseball(answerBaseball), 5, GameStatus.STOP_IN_PLAYING),
            ))

            shouldThrow<DomainServiceException> {
                baseballService.stopGame(gameId)
            }
        }

    }
})
