package clock.mobile.app

import androidx.compose.runtime.Composable
import clock.mobile.feature.clock.ClockScreen
import clock.mobile.feature.clock.ClockViewModel

@Composable
fun MobileApp(viewModel: ClockViewModel) {
    ClockScreen(viewModel = viewModel)
}
