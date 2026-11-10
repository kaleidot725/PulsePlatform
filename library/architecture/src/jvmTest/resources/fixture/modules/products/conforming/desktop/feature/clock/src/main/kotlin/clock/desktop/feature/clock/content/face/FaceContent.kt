package clock.desktop.feature.clock.content.face

import androidx.compose.runtime.Composable
import jp.kaleidot725.pulse.mvi.PulseContent

@Composable
fun FaceContent(viewModel: FaceViewModel) {
    PulseContent(viewModel = viewModel) { _, _ -> }
}
