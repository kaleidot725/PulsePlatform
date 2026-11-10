package clock.mobile.feature.clock

import clock.core.domain.model.Time
import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.pulse.mvi.PulseEvent
import jp.kaleidot725.pulse.mvi.PulseState

data class ClockState(
    val time: Time? = null,
) : PulseState

sealed class ClockAction : PulseAction {
    data object Refresh : ClockAction()
}

sealed class ClockEvent : PulseEvent
