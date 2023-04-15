package com.aminul.companion

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
                AppTheme.AUTO -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
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
                scaffoldState = scaffoldState
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
) {
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

        AlertDialogExample(
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
    onItemSelected: (AppTheme) -> Unit
) {
    val radioOptions: List<String> = listOf("Auto", "Light", "Dark")
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf(getSelectedTheme(context)) }

    TextButton(
        onClick = { showDialog.value = true }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_theme),
                contentDescription = "Theme Icon",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "Theme",
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Select Theme",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(Modifier.padding(5.dp)) {
                    radioOptions.forEach { item ->
                        Row(
                            Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (item.uppercase() == selectedOption.name),
                                onClick = {
                                    selectedOption = AppTheme.valueOf(item.uppercase())
                                    saveSelectedTheme(context, selectedOption)
                                }
                            )

                            val annotatedString = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                ) { append("  $item  ") }
                            }

                            ClickableText(
                                text = annotatedString,
                                onClick = {
                                    selectedOption = AppTheme.valueOf(item.uppercase())
                                    saveSelectedTheme(context = context, theme = selectedOption)
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showDialog.value = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            showDialog.value = false
                            onItemSelected(selectedOption)
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            },
            modifier = Modifier.size(width = 250.dp, height = 200.dp)
        )
    }
}

private fun getSelectedTheme(context: Context): AppTheme {
    val prefs = context.getSharedPreferences("AppThemePrefs", Context.MODE_PRIVATE)
    val themeValue = prefs.getInt("selected_theme", AppTheme.AUTO.ordinal)
    return AppTheme.fromOrdinal(themeValue)
}

private fun saveSelectedTheme(context: Context, theme: AppTheme) {
    val prefs = context.getSharedPreferences("AppThemePrefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("selected_theme", theme.ordinal).apply()
}