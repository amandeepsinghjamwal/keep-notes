import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Database
import database.NotesDao
import domain.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.EditScreen
import screens.MainScreen

object Constants {
    const val EDIT_SCREEN = "edit_screen"
    const val MAIN_SCREEN = "main_screen"
}

@Composable
@Preview
fun App(notesDao: NotesDao) {
    AppTheme(darkTheme = false, dynamicColor = false) {
        val navController = rememberNavController()
        val viewModel = viewModel<MainViewModel>{
            MainViewModel(notesDao)
        }
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
