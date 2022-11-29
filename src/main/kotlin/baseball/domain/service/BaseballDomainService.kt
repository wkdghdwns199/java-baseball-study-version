package baseball.domain.service

import baseball.domain.dto.BaseBallResultDto
import baseball.domain.dto.BaseBallScore
import baseball.domain.dto.GameCreationDto
import baseball.domain.entity.*
import baseball.domain.repository.BaseBallGameHistoryRepository
import baseball.domain.repository.BaseballGameRepository
import baseball.exception.EntityNotFoundException
import baseball.exception.ErrorCode
import baseball.exception.ServiceException


class BaseballDomainService(
    private val baseballGameRepository: BaseballGameRepository,
    private val baseballGameHistoryRepository: BaseBallGameHistoryRepository,
    private val baseballGenerator: BaseballGenerator,
    private val baseballPolicy: BaseballPolicy,
) {

    fun createGame(): GameCreationDto {
        val game = BaseballGame(
            answerBaseball = baseballGenerator.createBaseball(),
            remainingPlays = baseballPolicy.getTotalPlayCount()
        )

        return baseballGameRepository.save(game)
            .let { GameCreationDto(gameId = it.gameId) }
    }

    fun play(gameId: Long, userBaseball: Baseball): BaseBallResultDto {
        val game: BaseballGame = baseballGameRepository.findByGameId(gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_NOT_FOUND)

        if (game.gameStatus != GameStatus.ACTIVE)
            throw ServiceException(ErrorCode.GAME_CANNOT_PLAY)

        val playResult: PlayResult = game.play(userBaseball)

        if (playResult.gameResult != GameResult.GAME_OVER)
            baseballGameHistoryRepository.save(playResult)

        return BaseBallResultDto(
            gameId = game.gameId,
            remainingPlays = playResult.remainPlays,
            baseballScore = BaseBallScore(ball = playResult.ball, strike = playResult.strike),
            isGameEnd = playResult.gameResult != GameResult.PLAYING_GAME
        )
    }

    fun getPreviousResultOrNull(gameId: Long): BaseBallResultDto? {

        return baseballGameHistoryRepository.findRecentResultByGameId(gameId)
            ?.let { playResult: PlayResult ->
                BaseBallResultDto(
                    gameId = playResult.gameId,
                    remainingPlays = playResult.remainPlays,
                    baseballScore = BaseBallScore(playResult.ball, playResult.strike),
                    isGameEnd = playResult.gameResult != GameResult.PLAYING_GAME
                )
            }
    }

    fun stopGame(gameId: Long) {
        val game: BaseballGame = baseballGameRepository.findByGameId(gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_NOT_FOUND)

        game.stop()
        baseballGameRepository.save(game)
    }


}