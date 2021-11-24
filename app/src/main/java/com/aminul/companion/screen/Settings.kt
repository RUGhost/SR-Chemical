package com.aminul.companion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun SettingScreen() {
    TabScreen()
}
@ExperimentalPagerApi
@Composable
fun TabScreen(){
    val pagerState = rememberPagerState(pageCount = 6)
    Column(modifier = Modifier.background(Color.White)
    ) {
        Tabs(pagerState = pagerState)
        TabContent(pagerState = pagerState)
    }
}
@ExperimentalPagerApi
@Composable
fun TabContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when(page){
            0 -> TabOneScreen()
            1 -> TabOneScreen()
            2 -> TabOneScreen()
            3 -> TabOneScreen()
            4 -> TabOneScreen()
            5 -> TabOneScreen()
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("MEI 1", "MEI 2", "MEI 3", "MEI 1", "MEI 2", "MEI 3")
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Blue,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.Divider(
                thickness = 2.dp,
                color = Color.Green
            )
        },
        indicator = { tabPosition ->
            TabRowDefaults.Indicator(
                modifier = Modifier.
                pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPosition
                ),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {
        list.forEachIndexed{ index, _ ->
            Tab(
                text= {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}