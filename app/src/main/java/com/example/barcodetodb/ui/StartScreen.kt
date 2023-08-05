package com.example.barcodetodb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.barcodetodb.R
import com.example.barcodetodb.ui.theme.AppTheme

enum class AppScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    AddItem(title = R.string.add_item_screen),
    EditItem(title = R.string.edit_item_screen),
    Search(title = R.string.search_screen),
    Browse(title = R.string.browse_screen)
}
@Composable
fun BarcodeApp(
    navController: NavHostController = rememberNavController()
){
    val startScreenViewModel: StartScreenViewModel = viewModel()
    val addItemViewModel: AddItemViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Main.name)

    AppTheme(useDarkTheme = startScreenViewModel.uiState.currentThemeIsDark) {

        Scaffold(
            topBar = {
                BarcodeAppBar(
                    viewModel = startScreenViewModel,
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp()},
                )
                     },
            bottomBar = {
                BottomBarcodeAppBar(
                    value = "",
                    onValueChange = {},
                    clearButtonOnClick = {},
                    calendarButtonOnClick = {}
                ) },
            snackbarHost = { },
            floatingActionButton = {
                if (currentScreen != AppScreen.AddItem){
                    AddFloatingActionButton {
                        addItemViewModel.resetTextFields()
                        navController.navigate(AppScreen.AddItem.name)
                    } } },
            floatingActionButtonPosition = FabPosition.End
        ){ innerPadding ->

            NavHost(
                navController = navController,
                startDestination = AppScreen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(route = AppScreen.Main.name){
                    val viewModel = hiltViewModel<MainScreenViewModel>()
                    val itemViewModel = hiltViewModel<AddItemViewModel>()
                    MainScreen(navController, viewModel, itemViewModel)
                }
                composable(route = AppScreen.AddItem.name){
                    val viewModel = hiltViewModel<AddItemViewModel>()
                    hiltViewModel<AddItemViewModel>()
                    AddItemScreen(navController, viewModel)
                }
                composable(route = AppScreen.EditItem.name){
                    val viewModel = hiltViewModel<AddItemViewModel>()
                    AddItemScreen(navController, viewModel, editItemFlag = true)
                }
                composable(route = AppScreen.Search.name){
                    SearchScreen()
                }
                composable(route = AppScreen.Browse.name){
                }
            }
        }
    }
}
@Composable
fun AddFloatingActionButton(buttonClicked: () -> Unit){
    FloatingActionButton(onClick =  buttonClicked ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add new item")
    }
}
@Composable
fun BottomBarcodeAppBar(
    value: String,
    onValueChange: (String) -> Unit,
    clearButtonOnClick: () -> Unit,
    calendarButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
){
    BottomAppBar(modifier = modifier) {
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                label = { Text(stringResource(R.string.search_text_field))},
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "") },
                trailingIcon = {
                    IconButton(onClick = clearButtonOnClick) {
                        Icon(Icons.Filled.Clear, contentDescription = "")
                    }
                }
            )
            IconButton(onClick = calendarButtonOnClick) {
                Icon(
                    painterResource(R.drawable.baseline_calendar_month_24),
                    contentDescription = ""
                )
            }
        }
    }
}
@Composable
fun BarcodeAppBar(
    viewModel: StartScreenViewModel,
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { viewModel.changeDropdownMenuState() }
            ){
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = viewModel.uiState.expandedTopBarMenu ,
                onDismissRequest = { viewModel.changeDropdownMenuState() }
            ){
                DropdownMenuItem(
                    text = {
                        Row(modifier = modifier, Arrangement.SpaceBetween, Alignment.CenterVertically)
                        {
                            Text(text = "Dark Theme", fontSize = 20.sp)
                            Spacer(modifier = modifier.size(16.dp))
                            Switch(
                                checked = viewModel.uiState.toggleThemeButton,
                                onCheckedChange = {
                                        isChecked ->
                                    viewModel.changeThemeAndSwitchButtonState(isChecked)
                                }
                            )
                        }
                           },
                    onClick = { }
                )
            }
        }
    )
}
@Preview
@Composable
fun BarcodeToDbPreview(){
    BottomBarcodeAppBar(calendarButtonOnClick = {}, value = "",
        onValueChange = {}, clearButtonOnClick = {})
}