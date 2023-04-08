package com.aminul.companion.navigation

import com.aminul.companion.R


sealed class ScreenHolder(
    val route: String,
    val title: String,
    val icon: Int,
    val header: String
){
    object Home: ScreenHolder(
        route = "converter",
        title = "Converter",
        icon = R.drawable.ic_convert,
        header = "Tank Level Converter"
    )
    object Production: ScreenHolder(
        route = "production",
        title = "Production",
        icon = R.drawable.ic_production,
        header = "Chlorine Production Calculator"
    )
    object HAP: ScreenHolder(
        route = "hap",
        title = "HAP",
        icon = R.drawable.ic_tools,
        header = "Hourly Chlorine Production"
    )
    object About: ScreenHolder(
        route = "about",
        title = "About",
        icon = R.drawable.ic_group,
        header = "About This app"
    )
    object Rate: ScreenHolder(
        route = "rate",
        title = "Rate",
        icon = R.drawable.ic_group,
        header = "Rate the App"
    )
}