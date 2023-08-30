package com.example.barcodetodb.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.barcodetodb.R
import com.example.barcodetodb.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

enum class AppScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    AddItem(title = R.string.add_item_screen),
    EditItem(title = R.string.edit_item_screen)
}

@Composable
fun BarcodeApp(
    startScreenViewModel: StartScreenViewModel,
    addItemViewModel: AddItemViewModel,
    itemListViewModel: ItemListViewModel,
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Main.name)
    val queryState = rememberSaveable { mutableStateOf("") }
    val showCalendar = rememberSaveable { mutableStateOf(false) }
    val calendarState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Picker)


    AppTheme(useDarkTheme = startScreenViewModel.uiState.currentThemeIsDark){
        Scaffold(
            topBar = {
                BarcodeAppBar(
                    viewModel = startScreenViewModel,
                    itemListViewModel = itemListViewModel,
                    context = context,
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp()})
            },
            bottomBar = {
                BottomAppBar(modifier = Modifier){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextField(
                            value = queryState.value,
                            onValueChange = { newValue ->
                                queryState.value = newValue
                                if (queryState.value.isNotBlank()) {
                                    itemListViewModel.isFiltered.value = true
                                    itemListViewModel.filterList(
                                        queryState.value,
                                        calendarState.selectedStartDateMillis,
                                        calendarState.selectedEndDateMillis
                                    )
                                } else itemListViewModel.isFiltered.value = false
                            },
                            modifier = Modifier,
                            label = { Text(stringResource(R.string.search_text_field))},
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            trailingIcon = {
                                if (queryState.value.isNotBlank() || calendarState.selectedStartDateMillis != null || calendarState.selectedEndDateMillis != null) {
                                    IconButton(
                                        onClick = {
                                            queryState.value = ""
                                            calendarState.setSelection(null, null)
                                            itemListViewModel.filterList("", null, null)
                                            itemListViewModel.isFiltered.value = false
                                        }
                                    ) {
                                        Icon(Icons.Filled.Clear, contentDescription = null)
                                    }
                                }
                            },
                            singleLine = true
                        )
                        IconButton(
                            onClick = { showCalendar.value = true }
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_calendar_month_24),
                                contentDescription = null
                            )
                            if (showCalendar.value)
                            {
                                Popup(
                                    alignment = Alignment.CenterStart,
                                    onDismissRequest = { showCalendar.value = false }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = Color.Black.copy(alpha = 0.7f)),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        CDateRangePicker(calendarState)
                                        Row(modifier = Modifier.padding(top = 16.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    itemListViewModel.filterList(
                                                        queryState.value,
                                                        calendarState.selectedStartDateMillis,
                                                        calendarState.selectedEndDateMillis
                                                    )
                                                    itemListViewModel.isFiltered.value = true
                                                },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(stringResource(R.string.calendar_filter_button))
                                            }
                                            Spacer(modifier = Modifier.size(16.dp))
                                            Button(
                                                onClick = {
                                                    calendarState.setSelection(null, null)
                                                    itemListViewModel.filterList(
                                                        queryState.value,
                                                        null,
                                                        null
                                                    )
                                                },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(stringResource(R.string.calendar_clear_button))
                                            }
                                            Spacer(modifier = Modifier.size(16.dp))
                                            Button(
                                                onClick = { showCalendar.value = false },
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(stringResource(R.string.calendar_close_button))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (currentScreen != AppScreen.AddItem) {
                AddFloatingActionButton{
                    addItemViewModel.resetTextFields()
                    navController.navigate(AppScreen.AddItem.name) } }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = AppScreen.Main.name){
                    MainScreen(navController, itemListViewModel, addItemViewModel)
                }
                composable(route = AppScreen.AddItem.name){
                    AddItemScreen(navController, addItemViewModel, itemListViewModel)
                    addItemViewModel.editItemFlag = false
                }
                composable(route = AppScreen.EditItem.name){
                    AddItemScreen(navController, addItemViewModel, itemListViewModel)
                    addItemViewModel.editItemFlag = true
                }
            }
        }
    }
}
@Composable
fun CDateRangePicker(state: DateRangePickerState) {
    DateRangePicker(
        state = state,
        modifier = Modifier
            .size(width = 350.dp, height = 450.dp)
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.date_range_picker_title_text), fontSize = 22.sp)
            }
        },
        headline = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
            {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center)
                {
                    if (state.selectedStartDateMillis != null) state.selectedStartDateMillis?.let {
                        Text(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it))
                    } else
                        Text(text = stringResource(R.string.date_range_picker_start_date))
                }
                Box(Modifier.weight(0.2f), contentAlignment = Alignment.Center)
                {
                    Text(text = "-")
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center)
                {
                    if (state.selectedEndDateMillis != null) state.selectedEndDateMillis?.let {
                        Text(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it))
                    } else
                        Text(text = stringResource(R.string.date_range_picker_end_date))
                }
            }
        }

    )
}
@Composable
fun AddFloatingActionButton(buttonClicked: () -> Unit) {
    FloatingActionButton( onClick =  buttonClicked )
    {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_new_item_floating_button_desc))
    }
}
@Composable
fun BarcodeAppBar(
    viewModel: StartScreenViewModel,
    itemListViewModel: ItemListViewModel,
    context: Context,
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFilePickerVisible by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack)
            {
                IconButton(onClick =  navigateUp)
                {
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
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = viewModel.uiState.expandedTopBarMenu,
                onDismissRequest = { viewModel.changeDropdownMenuState() }
            ) {
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = modifier,
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.appbar_dropdown_menu_item_dark_theme_mode)
                            )
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
                    onClick = { viewModel.changeThemeAndSwitchButtonState(!viewModel.uiState.toggleThemeButton) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.dropdown_menu_save_database_to_local_memory)) },
                    onClick = { itemListViewModel.saveDatabaseToFile(context) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.dropdown_menu_send_database)) },
                    onClick = { itemListViewModel.sendDatabaseByMail(context) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.dropdown_menu_import_database)) },
                    onClick = { isFilePickerVisible = true }
                )
                if (isFilePickerVisible) {
                    val selectedFileUri = rememberSaveable { mutableStateOf<Uri?>(null) }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()){
                            uri: Uri? ->
                        selectedFileUri.value = uri
                    }
                    Column {
                        Popup(
                            alignment = Alignment.Center,
                            onDismissRequest = { isFilePickerVisible = false }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.Black, RectangleShape),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextField(
                                    enabled = false,
                                    readOnly = true,
                                    value = if (selectedFileUri.value != null) "${selectedFileUri.value}"
                                    else "",
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    label = { Text(text = stringResource(R.string.import_database_textfield_label)) }
                                )
                                Row(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = { launcher.launch("application/json") },
                                        modifier = modifier.defaultMinSize(minWidth = 100.dp),
                                        shape = MaterialTheme.shapes.extraSmall
                                    ) {
                                        Text(text = stringResource(R.string.select_file_button))
                                    }
                                    Button(
                                        onClick = {
                                            showConfirmationDialog = true
                                        },
                                        modifier = modifier.defaultMinSize(minWidth = 100.dp),
                                        shape = MaterialTheme.shapes.extraSmall
                                    ) {
                                        Text(text = stringResource(R.string.import_button))
                                    }
                                    if (showConfirmationDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showConfirmationDialog = false },
                                            title = { Text(stringResource(R.string.confirm_import_alert_dialog_title)) },
                                            text = {
                                                Text(stringResource(R.string.confirm_import_alert_dialog_text))
                                            },
                                            confirmButton = {
                                                Button(
                                                    onClick = {
                                                        itemListViewModel.viewModelScope.launch {
                                                            val uri = selectedFileUri.value ?: return@launch
                                                            itemListViewModel.importDatabaseFromFileUri(context, uri)
                                                        }
                                                        isFilePickerVisible = false
                                                        showConfirmationDialog = false
                                                    },
                                                    modifier = modifier.defaultMinSize(minWidth = 100.dp),
                                                    shape = MaterialTheme.shapes.extraSmall
                                                ) {
                                                    Text(text = stringResource(R.string.import_button))
                                                }
                                            },
                                            dismissButton = {
                                                Button(
                                                    onClick = {
                                                        showConfirmationDialog = false
                                                    },
                                                    modifier = modifier.defaultMinSize(minWidth = 100.dp),
                                                    shape = MaterialTheme.shapes.extraSmall
                                                ) {
                                                    Text(stringResource(R.string.cancel_button_text))
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}