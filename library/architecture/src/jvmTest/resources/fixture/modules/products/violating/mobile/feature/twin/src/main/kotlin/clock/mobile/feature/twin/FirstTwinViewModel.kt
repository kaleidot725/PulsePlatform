package clock.mobile.feature.twin

import jp.kaleidot725.pulse.mvi.PulseViewModel

class FirstTwinViewModel : PulseViewModel<TwinState, TwinAction, TwinEvent>(TwinState()) {
    override fun onAction(uiAction: TwinAction) = Unit
}
