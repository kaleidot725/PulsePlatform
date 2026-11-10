package clock.desktop.feature.clock.content.face

import clock.core.domain.model.Time
import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.pulse.mvi.PulseEvent
import jp.kaleidot725.pulse.mvi.PulseState

data class FaceState(
    val time: Time? = null,
) : PulseState

sealed class FaceAction : PulseAction {
    data object Refresh : FaceAction()
}

sealed class FaceEvent : PulseEvent
