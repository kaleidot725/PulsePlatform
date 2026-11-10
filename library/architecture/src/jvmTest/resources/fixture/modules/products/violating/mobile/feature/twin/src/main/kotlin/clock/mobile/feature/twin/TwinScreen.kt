package clock.mobile.feature.twin

import androidx.compose.runtime.Composable
import jp.kaleidot725.pulse.mvi.PulseContent

@Composable
fun TwinScreen(viewModel: FirstTwinViewModel) {
    PulseContent(viewModel = viewModel) { _, _ -> }
}
