package clock.desktop.app

import androidx.compose.runtime.Composable
import clock.desktop.core.designsystem.window.WindowFrame
import clock.desktop.feature.clock.ClockHost
import clock.desktop.feature.clock.content.face.FaceViewModel

@Composable
fun DesktopApp(viewModel: FaceViewModel) {
    WindowFrame { ClockHost(viewModel = viewModel) }
}
