package com.aminul.companion.navigation

import com.aminul.companion.R


sealed class ScreenHolder(
    val route: String,
    val title: String,
    val icon: Int
){
    object Home: ScreenHolder(
        route = "converter",
        title = "Converter",
        icon = R.drawable.ic_convert
    )
    object Production: ScreenHolder(
        route = "production",
        title = "Production",
        icon = R.drawable.ic_production
    )
    object Tools: ScreenHolder(
        route = "tools",
        title = "Tools",
        icon = R.drawable.ic_tools
    )
    object About: ScreenHolder(
        route = "about",
        title = "About",
        icon = R.drawable.ic_group
    )
    object Rate: ScreenHolder(
        route = "rate",
        title = "Rate",
        icon = R.drawable.ic_group
    )
    object ActionOne: ScreenHolder(
        route = "actionOne",
        title = "Action One",
        icon = R.drawable.ic_group
    )
    object ActionTwo: ScreenHolder(
        route = "actionTwo",
        title = "Action Two",
        icon = R.drawable.ic_group
    )
}