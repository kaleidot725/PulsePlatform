package clock.desktop.feature.clock.content.face

import clock.core.domain.repository.TimeRepository
import jp.kaleidot725.pulse.mvi.PulseViewModel

class FaceViewModel(
    private val repository: TimeRepository,
) : PulseViewModel<FaceState, FaceAction, FaceEvent, Nothing, Nothing>(FaceState()) {
    override fun onAction(uiAction: FaceAction) = Unit
}
