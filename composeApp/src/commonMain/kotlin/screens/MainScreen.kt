package screens

import Constants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import database.Notes
import domain.MainViewModel
import domain.StateUi
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.add
import keepnotes.composeapp.generated.resources.filter
import keepnotes.composeapp.generated.resources.nunito_regular
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import theme.BgColor

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(mainViewModel: MainViewModel, stateUi: StateUi, navController: NavHostController) {
    LaunchedEffect(stateUi.notesList) {
        println("This is notes list in la ${stateUi.notesList}")
    }
    val fontFamily = FontFamily(Font(Res.font.nunito_regular, FontWeight.Normal, FontStyle.Normal))
    LaunchedEffect(Unit) {
        mainViewModel.updateSelectedModel(
            Notes(
                id = null,
                body = "",
                title = "",
                createdDate = 0,
                updatedDate = 0
            )
        )
    }
    var isSearchBarOpened by remember {
        mutableStateOf(false)
    }
    var searchQuery by remember {
        mutableStateOf("")
    }
    var showFilterDialog by remember {
        mutableStateOf(false)
    }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    var showMenuOptions by remember {
        mutableStateOf(false)
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Box {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(end = 5.dp, bottom = 5.dp).size(60.dp)
                        .bounceClick(),
                    containerColor = Color(0xFFFDB600),
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.add),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    shape = CircleShape,
                    onClick = {
                        navController.navigate(Constants.EDIT_SCREEN)
                    })
            },
            topBar = {
                if (isSearchBarOpened) {
                    SearchBar(
                        query = searchQuery,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    if (searchQuery.isNotEmpty()) {
                                        searchQuery = ""
                                        keyboard?.hide()
                                    } else {
                                        isSearchBarOpened = !isSearchBarOpened
                                    }
                                }
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        },
                        placeholder = {
                            Text("Search titles...")
                        },
                        onSearch = {},
                        active = true,
                        onActiveChange = {},
                        colors = SearchBarDefaults.colors(
                            containerColor = BgColor,
                            inputFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedPlaceholderColor = Color.LightGray.copy(alpha = .3f),
                                unfocusedPlaceholderColor = Color.LightGray.copy(alpha = .3f)
                            )
                        ),
                        content = {
                            Spacer(modifier = Modifier.height(10.dp))
                            LazyColumn(modifier = Modifier.padding()) {
                                items(stateUi.notesList.filter {
                                    it.title.contains(
                                        searchQuery,
                                        ignoreCase = true
                                    )
                                }) {
                                    Note(Modifier, it.title, fontFamily, Color(it.colorHex)) {
                                        mainViewModel.updateSelectedModel(it)
                                        navController.navigate(Constants.EDIT_SCREEN)
                                    }
                                }
                            }
                        },
                        onQueryChange = { searchQuery = it })
                } else {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        modifier = Modifier.padding(top = 20.dp),
                        title = {
                            Text(
                                "Notes",
                                fontFamily = null,
                                color = Color.White,
                                fontSize = 35.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color(
                                0xFF252525
                            )
                        ),
                        actions = {
                            Row(
                                modifier = Modifier.padding(end = 15.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                WrappedIcon(Icons.Default.Search) {
                                    isSearchBarOpened = !isSearchBarOpened
                                }
                                WrappedIcon(Res.drawable.filter) {
                                    showFilterDialog = !showFilterDialog
                                }
                            }
                        }
                    )
                }
            },
            containerColor = Color(0xFF252525)
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                itemsIndexed(stateUi.notesList, key = { it, item -> item.id!! }) { index, it ->
                    Note(
                        Modifier.animateItemPlacement(),
                        it.title,
                        fontFamily,
                        Color(it.colorHex)
                    ) {
                        mainViewModel.updateSelectedModel(it)
                        navController.navigate(Constants.EDIT_SCREEN)
                    }
                }
            }
        }
        if (showFilterDialog) {
            SortDialog(selectedIndex, onDone = {
                showFilterDialog = !showFilterDialog
                mainViewModel.sortList(selectedIndex)
            }) {
                selectedIndex = it
            }
        }
    }
}

@Composable
fun SortDialog(selectedIndex: Int, onDone: () -> Unit, changeSelectedIndex: (Int) -> Unit) {
    AlertDialog(
        title = { Text("Sort By") },
        onDismissRequest = { onDone() },
        confirmButton = { Text("Apply", modifier = Modifier.clickable { onDone() }) },
        text = {
            Column {
                repeat(filterList.size) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedIndex == it,
                            onClick = {
                                changeSelectedIndex(it)
                            }
                        )
                        Text(filterList[it])
                    }
                }
            }
        })
}

val filterList = listOf(
    "Name", "Created", "Updated"
)

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.90f else 1f,
        label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

@Composable
fun WrappedIcon(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier.bounceClick().background(
            color = Color.White.copy(alpha = .1f),
            shape = AbsoluteRoundedCornerShape(10.dp)
        )
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(10.dp).size(22.dp)
                .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    onClick()
                }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WrappedIcon(resource: DrawableResource, onClick: () -> Unit) {
    Box(
        modifier = Modifier.bounceClick().background(
            color = Color.White.copy(alpha = .1f),
            shape = AbsoluteRoundedCornerShape(10.dp)
        )
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(10.dp).size(22.dp)
                .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    onClick()
                }
        )
    }
}

@Composable
fun Note(
    modifier: Modifier = Modifier,
    header: String,
    fontFamily: FontFamily,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().heightIn(100.dp)
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .background(color = color, shape = AbsoluteRoundedCornerShape(20f))
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = header,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = fontFamily,
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight(580),
                lineHeight = 30.sp
            )
        )
    }
}

@Composable
fun sortDialog() {

}