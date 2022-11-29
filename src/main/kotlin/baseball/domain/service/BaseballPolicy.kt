package baseball.domain.service

class BaseballPolicy {
    fun getTotalPlayCount(): Long {
        return DEFAULT_COUNT
    }

    companion object {
        private const val DEFAULT_COUNT = 5L
    }
}
