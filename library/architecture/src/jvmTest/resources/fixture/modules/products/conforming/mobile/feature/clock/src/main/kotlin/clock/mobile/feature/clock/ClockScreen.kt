package clock.mobile.feature.clock

import androidx.compose.runtime.Composable
import jp.kaleidot725.pulse.mvi.PulseContent

@Composable
fun ClockScreen(viewModel: ClockViewModel) {
    PulseContent(viewModel = viewModel) { _, _ -> }
}
