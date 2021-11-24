package com.aminul.companion.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aminul.companion.screen.*
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = ScreenHolder.Home.route
    ){
        composable(route = ScreenHolder.Home.route){
            TankLevelConverter()
        }
        composable(route = ScreenHolder.Production.route){
            ProductionScreen()
        }
        composable(route = ScreenHolder.Setting.route){
            SettingScreen()
        }
        composable(route = ScreenHolder.About.route){
            AboutScreen()
        }
        composable(route = ScreenHolder.Rate.route){
            RateScreen()
        }
        composable(route = ScreenHolder.ActionOne.route){
            ActionOneScreen()
        }
        composable(route = ScreenHolder.ActionTwo.route){
            ActionTwoScreen()
        }
    }
}