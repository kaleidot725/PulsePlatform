package clock.mobile.feature.clock

import clock.core.domain.repository.TimeRepository
import jp.kaleidot725.pulse.mvi.PulseViewModel

class ClockViewModel(
    private val repository: TimeRepository,
) : PulseViewModel<ClockState, ClockAction, ClockEvent>(ClockState()) {
    override fun onAction(uiAction: ClockAction) = Unit
}
