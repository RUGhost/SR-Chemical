package com.aminul.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aminul.companion.appTheme.AppTheme
import com.aminul.companion.appTheme.ThemeSetting
import com.aminul.companion.navigation.NavGraph
import com.aminul.companion.navigation.ScreenHolder
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.aminul.companion.ui.theme.CompanionTheme
import com.aminul.companion.ui.theme.darkGreenStatus
import com.aminul.companion.ui.theme.forestGreenStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeSetting: ThemeSetting

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val theme = themeSetting.themeStream.collectAsState()
            val useDarkColors = when (theme.value) {
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }

            CompanionTheme(darkTheme = useDarkColors) {
                val color = if (useDarkColors) darkGreenStatus else forestGreenStatus
                window.statusBarColor = color.toArgb()

                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(onItemSelected = { theme -> themeSetting.theme = theme })
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun MainScreen(onItemSelected: (AppTheme) -> Unit) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val (selectedScreenTitle, setSelectedScreenTitle) = remember { mutableStateOf("Tank Level Converter") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                selectedScreenTitle = selectedScreenTitle,
                scope = scope,
                scaffoldState = scaffoldState,
                onItemSelected = onItemSelected
            )
        },
         bottomBar = {
            BottomBar(navController = navController) { screenTitle ->
                setSelectedScreenTitle(screenTitle)
            }
        },
        drawerContent = {
            DrawerContent(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                onScreenSelected = setSelectedScreenTitle,
                onItemSelected = onItemSelected
            )
        }
    ) {
        NavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController, onScreenSelected: (String) -> Unit) {
    val screens = listOf(
        ScreenHolder.Home,
        ScreenHolder.Production,
        ScreenHolder.HAP
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = MaterialTheme.colors.primary) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                navController = navController,
                onScreenSelected = {
                    onScreenSelected(screen.header)
                },
                isSelected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: ScreenHolder,
    navController: NavHostController,
    onScreenSelected: () -> Unit,
    isSelected: Boolean
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title, fontWeight = FontWeight.SemiBold)
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Bottom Navigation Icon"
            )
        },
        selected = isSelected,

        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
            onScreenSelected()
        },
        selectedContentColor = MaterialTheme.colors.background,
        unselectedContentColor = MaterialTheme.colors.onBackground
    )
}

@Composable
fun TopBar(
    selectedScreenTitle: String,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onItemSelected: (AppTheme) -> Unit
) {
    val menuExpanded = remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = selectedScreenTitle,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
//        actions = {
//            IconButton(
//                onClick = {
//                    menuExpanded.value = true
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = "Action Button",
//                )
//            }
//            Column(
//                modifier = Modifier.wrapContentSize(Alignment.TopStart)
//            ) {
//                DropdownMenu(
//                    expanded = menuExpanded.value,
//                    onDismissRequest = {
//                        menuExpanded.value = false
//                    },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .wrapContentSize(Alignment.TopStart)
//                ) {
//                    DropdownMenuItem(
//                        onClick = {
//                            onItemSelected(AppTheme.fromOrdinal(AppTheme.MODE_AUTO.ordinal))
//                            menuExpanded.value = false
//                        }
//                    ) {
//                        Text(text = "System")
//                    }
//                    DropdownMenuItem(
//                        onClick = {
//                            onItemSelected(AppTheme.fromOrdinal(AppTheme.MODE_DAY.ordinal))
//                            menuExpanded.value = false
//                        }
//                    ) {
//                        Text(text = "Day")
//                    }
//                    DropdownMenuItem(
//                        onClick = {
//                            onItemSelected(AppTheme.fromOrdinal(AppTheme.MODE_NIGHT.ordinal))
//                            menuExpanded.value = false
//                        }
//                    ) {
//                        Text(text = "Night")
//                    }
//                }
//            }
//        }
    )
}

@Composable
fun DrawerContent(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    onScreenSelected: (String) -> Unit,
    onItemSelected: (AppTheme) -> Unit
) {
    val items = listOf(
        ScreenHolder.About,
        ScreenHolder.Rate
    )
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colors.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        items.forEach { item ->
            DrawerItem(
                item = item,
                selected = currentRoute?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onItemClick = {
                    onScreenSelected(item.header)
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = false
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
        }

        val radioOption: List<String> = listOf("Auto", "Light", "Dark")
        AlertDialogExample(
            radioOptions = radioOption,
            fontSize = 13.sp,
            onItemSelected = onItemSelected
        )


        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Copy Right",
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun DrawerItem(item: ScreenHolder, selected: Boolean, onItemClick: (ScreenHolder) -> Unit) {
    val background = if (selected) MaterialTheme.colors.surface else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(background)
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun AlertDialogExample(
    radioOptions: List<String> = listOf(),
    fontSize: TextUnit = 11.sp,
    onItemSelected: (AppTheme) -> Unit
){
    val showDialog = remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf(AppTheme.MODE_AUTO) }

    TextButton(onClick = { showDialog.value = true }) {
        Text("Show Dialog")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Select Theme") },
            text = {
                if (radioOptions.isNotEmpty()) {
                    Column(Modifier.padding(5.dp)) {
                        radioOptions.forEach { item ->
                            Row(
                                Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val appTheme = when (item) {
                                    "Auto" -> AppTheme.MODE_AUTO
                                    "Day" -> AppTheme.MODE_DAY
                                    "Night" -> AppTheme.MODE_NIGHT
                                    else -> AppTheme.MODE_AUTO
                                }
                                RadioButton(
                                    selected = (appTheme == selectedOption),
                                    onClick = { selectedOption = appTheme }
                                )

                                val annotatedString = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = fontSize,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    ) { append("  $item  ") }
                                }

                                ClickableText(
                                    text = annotatedString,
                                    onClick = {
                                        selectedOption = appTheme
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}

