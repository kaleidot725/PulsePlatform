package clock.mobile.feature.twin.content.second

import clock.mobile.feature.twin.TwinAction
import clock.mobile.feature.twin.TwinEvent
import clock.mobile.feature.twin.TwinState
import jp.kaleidot725.pulse.mvi.PulseViewModel

class SecondTwinViewModel : PulseViewModel<TwinState, TwinAction, TwinEvent>(TwinState()) {
    override fun onAction(uiAction: TwinAction) = Unit
}
