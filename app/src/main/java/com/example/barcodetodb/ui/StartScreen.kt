package com.example.barcodetodb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.barcodetodb.R

enum class AppScreen(@StringRes val title: Int) {
    Option(title = R.string.app_name),
    AddItem(title = R.string.add_item_screen),
    Search(title = R.string.search_screen),
    Browse(title = R.string.browse_screen)
}

@Composable
fun BarcodeApp(
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Option.name)
    Scaffold(
        topBar = {
            BarcodeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp()}
            )}
    ){ innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppScreen.Option.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = AppScreen.Option.name){
                OptionScreen(
                    onAddButtonClicked = {
                        navController.navigate(AppScreen.AddItem.name)
                    },
                    onSearchButtonClicked = {
                        navController.navigate(AppScreen.Search.name)
                    },
                    onBrowseButtonClicked = {
                        navController.navigate(AppScreen.Browse.name)
                    }
                )
            }
            composable(route = AppScreen.AddItem.name){
                AddItemScreen()
            }
            composable(route = AppScreen.Search.name){
                SearchScreen()
            }
            composable(route = AppScreen.Browse.name){
                BrowseScreen()
            }
        }
    }
}
@Composable
fun BarcodeAppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button))
                }
            }
        }
    )
}
@Preview
@Composable
fun BarcodeToDbPreview(){
    BarcodeApp()
}