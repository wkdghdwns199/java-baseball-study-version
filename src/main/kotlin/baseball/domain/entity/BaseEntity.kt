package baseball.domain.entity

import java.time.Instant

abstract class BaseEntity() {
    var createdAt: Instant = Instant.now()
        protected set

    var modifiedAt: Instant = Instant.now()
        protected set
}