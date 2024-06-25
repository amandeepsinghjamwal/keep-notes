import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import theme.Typography

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = Typography,
        content = content
    )

}