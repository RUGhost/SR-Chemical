package com.aminul.companion.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun AboutScreen(){
    val pagerState = rememberPagerState(pageCount = 6)
    Column {
        Tabs(pagerState = pagerState)
        TabContent(pagerState = pagerState)
    }
}

@ExperimentalPagerApi
@Composable
fun TabContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when(page){
            0 -> Caustic()
            1 -> HCl()
            2 -> TabThreeScreen()
            3 -> Caustic()
            4 -> Caustic()
            5 -> Caustic()
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("Caustic", "HCl", "MEI 3", "MEI 4", "MEI 5", "MEI 6")
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        edgePadding = 5.dp,
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.primaryVariant),
        divider = {
            TabRowDefaults.Divider(
                thickness = 3.dp,
                color = MaterialTheme.colors.secondary
            )
        },
        indicator = { tabPosition ->
            TabRowDefaults.Indicator(
                modifier = Modifier.
                pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPosition
                ),
                height = 3.dp,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    ) {
        list.forEachIndexed{ index, _ ->
            Tab(
                text= {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) {
                            MaterialTheme.colors.surface
                        } else MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.SemiBold
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