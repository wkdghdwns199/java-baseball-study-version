package domain.service

import common.exception.DomainServiceException
import common.exception.EntityNotFoundException
import common.exception.ErrorCode
import domain.dto.BaseBallResultDto
import domain.dto.GameCreationDto
import domain.entity.Baseball
import domain.entity.BaseballGame
import domain.entity.BaseballGamePlayHistory
import domain.repository.BaseballGameRepository
import domain.repository.BaseballPlayResultRepository


class BaseballDomainService(
    private val baseballGameRepository: BaseballGameRepository,
    private val baseballGamePlayHistoryRepository: BaseballPlayResultRepository,
    private val baseballGenerator: BaseballGenerator,
    private val baseballPolicy: BaseballPolicy,
) {

    fun createGame(): GameCreationDto {
        val game = BaseballGame(
            answerBaseball = baseballGenerator.createBaseball(),
            remainingPlays = baseballPolicy.totalGamePlayCount,
        )

        return GameCreationDto(gameId = baseballGameRepository.save(game).id)
    }

    fun playGame(gameId: Long, userBaseball: List<Long>): BaseBallResultDto {
        val baseballGame: BaseballGame = baseballGameRepository.findById(gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_NOT_FOUND)

        if (baseballGame.isGameOver)
            throw DomainServiceException(ErrorCode.GAME_CANNOT_PLAY)

        val gamePlayHistory: BaseballGamePlayHistory = baseballGame.play(userBaseball.toBaseballOrThrow())

        return baseballGamePlayHistoryRepository.save(gamePlayHistory)
            .let(BaseBallResultDto::fromEntity)
    }

    fun getPreviousPlayResultOrNull(gameId: Long): BaseBallResultDto? =
        baseballGamePlayHistoryRepository.findRecentPlayHistoryByGameId(gameId)
            ?.let(BaseBallResultDto::fromEntity)

    fun stopGame(gameId: Long) {
        val game: BaseballGame = baseballGameRepository.findById(gameId)
            ?: throw EntityNotFoundException(ErrorCode.GAME_NOT_FOUND)

        if(game.isGameOver)
            throw DomainServiceException(ErrorCode.GAME_CANNOT_STOP)

        game.stop()
        baseballGameRepository.save(game)
    }

    private fun List<Long>.toBaseballOrThrow(): Baseball {
        require(this.size.toLong() == baseballPolicy.baseballNumberCount) {
            "유효하지 않은 게임 입력값 입니다. ($this)"
        }
        require(this.all { it in baseballPolicy.baseballNumberRange }){
            "유효하지 않은 입력 숫자가 포함되어 있습니다. ($this)"
        }
        return Baseball(this)
    }

}