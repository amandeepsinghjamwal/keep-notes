import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import domain.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import screens.EditScreen
import screens.MainScreen

object Constants {
    const val EDIT_SCREEN = "edit_screen"
    const val MAIN_SCREEN = "main_screen"
}

@Composable
@Preview
fun App() {
    AppTheme(darkTheme = false, dynamicColor = false) {
        KoinContext {
            val navController = rememberNavController()
            val viewModel = koinViewModel<MainViewModel>()
            val stateUi by viewModel.stateUi.collectAsState()
            NavHost(navController, startDestination = Constants.MAIN_SCREEN) {
                composable(Constants.MAIN_SCREEN, enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }) {
                    MainScreen(viewModel, stateUi, navController)
                }
                composable(Constants.EDIT_SCREEN, enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }, exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }) {
                    EditScreen(viewModel, stateUi, navController)
                }
            }
        }
    }
}

@Composable
inline fun <reified T:ViewModel> koinViewModel() : T {
    val scope = currentKoinScope()
    return viewModel{
        scope.get<T>()
    }
}
