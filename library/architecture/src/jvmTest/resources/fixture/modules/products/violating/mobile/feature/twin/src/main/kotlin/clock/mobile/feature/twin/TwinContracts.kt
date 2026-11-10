package clock.mobile.feature.twin

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.pulse.mvi.PulseEvent
import jp.kaleidot725.pulse.mvi.PulseState

data class TwinState(
    val count: Int = 0,
) : PulseState

sealed class TwinAction : PulseAction

sealed class TwinEvent : PulseEvent
