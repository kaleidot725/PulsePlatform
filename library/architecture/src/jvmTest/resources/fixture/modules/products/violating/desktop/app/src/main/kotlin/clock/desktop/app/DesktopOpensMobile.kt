package clock.desktop.app

import androidx.compose.runtime.Composable
import clock.mobile.feature.clock.ClockScreen
import clock.mobile.feature.clock.ClockViewModel

@Composable
fun DesktopOpensMobile(viewModel: ClockViewModel) {
    ClockScreen(viewModel = viewModel)
}
