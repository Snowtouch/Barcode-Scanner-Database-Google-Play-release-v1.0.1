package com.snowtouch.barcodetodb.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.snowtouch.barcodetodb.R
import com.snowtouch.barcodetodb.data.Item
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: ItemListViewModel,
    addItemViewModel: AddItemViewModel
) {
    val itemFlow: StateFlow<List<Item>> = if (viewModel.isFiltered.value) {
        viewModel.filteredItemFlow
    } else {
        viewModel.itemFlow
    }
    ItemsList(viewModel, addItemViewModel, navController, itemFlow)
}
@Composable
fun ItemsList(
    itemListViewModel: ItemListViewModel,
    addItemViewModel: AddItemViewModel,
    navController: NavController,
    itemFlow: StateFlow<List<Item>>,
    modifier: Modifier = Modifier
) {
    val itemListState by itemFlow.collectAsState()
    var expandedItemId by rememberSaveable { mutableLongStateOf(-1L) }
    var openDeleteConfirmationDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier)
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "ID", modifier = modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text(text = "Code", modifier = modifier.weight(10f))
            Text(text = "Name", modifier = modifier.weight(9f))
            Text(text = "Price", modifier = modifier.weight(4f))
            Text(text = "Qty", modifier = modifier.weight(3f))
            Text(text = "Date", modifier = modifier.weight(5f))
        }
        Divider()
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(itemListState) { item ->
                Card(
                    modifier = modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                    elevation = CardDefaults.cardElevation(8.dp),
                    border = BorderStroke(width = Dp.Hairline, Color.Black),
                    onClick = { expandedItemId =
                        if (expandedItemId == item.id.toLong()) -1L else item.id.toLong()
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            text = item.id.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(start = 2.dp)
                                .weight(2f))
                        Text(
                            text = item.itemCode,
                            modifier = modifier.weight(10f))
                        Text(
                            text = item.itemName,
                            modifier = modifier.weight(9f))
                        Text(
                            text = item.itemPrice.toString(),
                            modifier = modifier.weight(4f))
                        Text(
                            text = item.itemQuantity.toString(),
                            modifier = modifier.weight(3f))
                        Text(
                            text = item.writeDate,
                            modifier = modifier.weight(5f))
                    }
                        AnimatedVisibility(
                            visible = expandedItemId == item.id.toLong(),
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                        Row(modifier = modifier)
                        {
                            TextButton(
                                modifier = modifier
                                    .size(width = 80.dp, height = 35.dp),
                                onClick = {
                                    addItemViewModel
                                        .updateUiState(ItemDetails(
                                        id = item.id.toString(),
                                        code = item.itemCode,
                                        name = item.itemName,
                                        price = item.itemPrice.toString(),
                                        quantity = item.itemQuantity.toString(),
                                        date = item.writeDate))
                                    addItemViewModel.editItemFlag = true
                                    navController.navigate(route = AppScreen.EditItem.name)
                                },
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Text(stringResource(R.string.item_list_on_click_context_menu_edit))
                            }
                            TextButton(
                                modifier = modifier
                                    .padding(start = 8.dp)
                                    .size(width = 90.dp, height = 35.dp),
                                onClick = { openDeleteConfirmationDialog = true },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(stringResource(R.string.item_list_on_click_context_menu_delete))
                            }
                            if (openDeleteConfirmationDialog) {
                                AlertDialog(
                                    onDismissRequest = { openDeleteConfirmationDialog = false },
                                    title = {
                                        Text(text = stringResource(R.string.delete_item_confirmation_window_title))
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                itemListViewModel.deleteItem(item)
                                                openDeleteConfirmationDialog = false
                                            }
                                        ) {
                                            Text(stringResource(R.string.delete_item_confirmation_window_confirm_button))
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { openDeleteConfirmationDialog = false }
                                        ) {
                                            Text(stringResource(R.string.delete_item_confirmation_window_dismiss_button))
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