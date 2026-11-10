package clock.desktop.core.designsystem.window

import androidx.compose.runtime.Composable

@Composable
fun WindowFrame(content: @Composable () -> Unit) {
    content()
}
