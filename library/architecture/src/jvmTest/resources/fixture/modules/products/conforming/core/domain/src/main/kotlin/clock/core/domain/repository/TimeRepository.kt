package clock.core.domain.repository

import clock.core.domain.model.Time
import kotlinx.coroutines.flow.Flow

interface TimeRepository {
    val time: Flow<Time>
}
