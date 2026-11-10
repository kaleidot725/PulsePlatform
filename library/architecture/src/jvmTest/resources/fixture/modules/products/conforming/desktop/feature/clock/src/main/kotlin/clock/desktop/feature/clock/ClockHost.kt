package clock.desktop.feature.clock

import androidx.compose.runtime.Composable
import clock.desktop.feature.clock.content.face.FaceContent
import clock.desktop.feature.clock.content.face.FaceViewModel

@Composable
fun ClockHost(viewModel: FaceViewModel) {
    FaceContent(viewModel = viewModel)
}
