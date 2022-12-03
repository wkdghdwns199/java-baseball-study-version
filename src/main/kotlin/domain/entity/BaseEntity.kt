package domain.entity

import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

abstract class BaseEntity() {
    var createdAt: Instant = Instant.now()
        protected set

    var modifiedAt: Instant = Instant.now()
        protected set

    val id: Long = idGenerator.incrementAndGet()

    companion object {
        private val idGenerator = AtomicLong()
    }
}