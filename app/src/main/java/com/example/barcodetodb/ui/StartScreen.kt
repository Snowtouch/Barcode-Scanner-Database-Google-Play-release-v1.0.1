package com.example.barcodetodb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.barcodetodb.R
import com.example.barcodetodb.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class AppScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    AddItem(title = R.string.add_item_screen),
    EditItem(title = R.string.edit_item_screen),
    Search(title = R.string.search_screen),
    Browse(title = R.string.browse_screen)
}

@Composable
fun BarcodeApp(navController: NavHostController = rememberNavController())
{
    val startScreenViewModel: StartScreenViewModel = hiltViewModel()
    val addItemViewModel: AddItemViewModel = hiltViewModel()
    val itemListViewModel: ItemListViewModel = hiltViewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Main.name)
    val queryState = rememberSaveable { mutableStateOf("") }
    val showCalendar = rememberSaveable { mutableStateOf(false) }
    var calendarState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Picker)

    AppTheme(useDarkTheme = startScreenViewModel.uiState.currentThemeIsDark){
        Scaffold(
            topBar = {
                BarcodeAppBar(
                    viewModel = startScreenViewModel,
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() })
                     },
            bottomBar = {
                BottomAppBar(modifier = Modifier){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        TextField(
                            value = queryState.value,
                            onValueChange = { newValue ->
                                queryState.value = newValue
                                if (newValue.isNotBlank())
                                {
                                    itemListViewModel.filterList(
                                        newValue,
                                        calendarState.selectedStartDateMillis,
                                        calendarState.selectedEndDateMillis)
                                    itemListViewModel.isFiltered.value = true
                                }
                                else itemListViewModel.isFiltered.value = false
                            },
                            modifier = Modifier,
                            label = { Text(stringResource(R.string.search_text_field))},
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "") },
                            trailingIcon = { if (queryState.value.isNotBlank())
                                IconButton(
                                    onClick = {
                                        queryState.value = ""
                                        itemListViewModel.isFiltered.value = false}) {
                                    Icon(Icons.Filled.Clear, contentDescription = "")
                                }
                            },
                            singleLine = true
                        )
                        IconButton(
                            onClick = { showCalendar.value = true }
                        ){
                            Icon(
                                painterResource(R.drawable.baseline_calendar_month_24),
                                contentDescription = ""
                            )
                            if (showCalendar.value)
                            {
                                Popup(
                                    alignment = Alignment.CenterStart,
                                    onDismissRequest = { showCalendar.value = false})
                                {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = Color.Black.copy(alpha = 0.7f)),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        CDateRangePicker(calendarState)
                                        Row(modifier = Modifier.padding(top = 16.dp)) {
                                            Button(
                                                onClick = { /*TODO*/ },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(text = "Filter")
                                            }
                                            Spacer(modifier = Modifier.size(16.dp))
                                            Button(
                                                onClick = { },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(text = "Clear")
                                            }
                                            Spacer(modifier = Modifier.size(16.dp))
                                            Button(
                                                onClick = { showCalendar.value = false },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(text = "Close")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                        },
            snackbarHost = { },
            floatingActionButton = { if (currentScreen != AppScreen.AddItem)
                {
                    AddFloatingActionButton{
                        addItemViewModel.resetTextFields()
                        navController.navigate(AppScreen.AddItem.name) }
                }
                                   },
            floatingActionButtonPosition = FabPosition.End
        ){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(route = AppScreen.Main.name){
                    val viewModel = hiltViewModel<ItemListViewModel>()
                    val itemViewModel = hiltViewModel<AddItemViewModel>()
                    MainScreen(navController, viewModel, itemViewModel)
                }
                composable(route = AppScreen.AddItem.name){
                    val viewModel = hiltViewModel<AddItemViewModel>()
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
fun CDateRangePicker(state: DateRangePickerState){
    val startDate: Long? = state.selectedStartDateMillis
    val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
    val instant = Instant.ofEpochMilli(startDate?: 0)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    DateRangePicker(
        state = state,
        modifier = Modifier
            .size(width = 350.dp, height = 450.dp)
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier,
                    text = formatter.format(date)
                )
            } }
    )



}
@Composable
fun AddFloatingActionButton(buttonClicked: () -> Unit)
{
    FloatingActionButton( onClick =  buttonClicked )
    {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add new item")
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
                                onCheckedChange =
                                { isChecked ->
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
fun BarcodeToDbPreview() {
    BarcodeApp()
}