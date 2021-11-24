package com.aminul.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aminul.companion.navigation.NavGraph
import com.aminul.companion.navigation.ScreenHolder
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.aminul.companion.ui.theme.CompanionTheme

class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun MainScreen(){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope,scaffoldState = scaffoldState, navController = navController)},

        bottomBar = {
            BottomBar(navController = navController)
        },

        drawerContent = {
            DrawerContent(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        NavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        ScreenHolder.Home,
        ScreenHolder.Production,
        ScreenHolder.Setting
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: ScreenHolder,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Bottom Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}
@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavHostController){
    val menuExpanded = remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Companion App",
                    fontSize = 20.sp,
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
        backgroundColor = Color.Green,
        contentColor = Color.Black,
        actions = {
            IconButton(
                onClick = {
                    menuExpanded.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Action Button",
                )
            }
            Column(
                modifier = Modifier
            ) {
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = {
                        menuExpanded.value = false
                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    DropdownMenuItem(onClick = {
                        navController.navigate(ScreenHolder.ActionOne.route)
                        menuExpanded.value = false
                    }) {
                        Text(text = ScreenHolder.ActionOne.title)
                    }
                    DropdownMenuItem(onClick = {
                        navController.navigate(ScreenHolder.ActionTwo.route)
                        menuExpanded.value = false
                    }) {
                        Text(text = ScreenHolder.ActionTwo.title)
                    }
                }
            }
        }
    )
}

@Composable
fun DrawerContent(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController){
    val items = listOf(
        ScreenHolder.About,
        ScreenHolder.Rate
    )
    Column(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.LightGray),
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
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        items.forEach { item ->
            DrawerItem(
                item = item,
                selected = currentRoute?.hierarchy?.any{
                    it.route == item.route
                } == true,
                onItemClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = false
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Copy Right",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
@Composable
fun DrawerItem(item: ScreenHolder, selected: Boolean, onItemClick: (ScreenHolder) -> Unit) {
    val background = if (selected) R.color.gray else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(24.dp).width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}