package com.example.barcodetodb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barcodetodb.R
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(
    navController: NavController,
    addItemViewModel: AddItemViewModel = viewModel()
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            TextField(
                value = addItemViewModel.itemUiState.itemDetails.code,
                onValueChange = { addItemViewModel.updateUiState(addItemViewModel.itemUiState.itemDetails.copy(code = it)) },
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.enter_code_field)) },
                leadingIcon = {
                    Icon(painterResource(R.drawable.baseline_mode_24), contentDescription = null)},
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                value = addItemViewModel.itemUiState.itemDetails.name,
                onValueChange = { addItemViewModel.updateUiState(addItemViewModel.itemUiState.itemDetails.copy(name = it)) },
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.enter_name_field)) },
                leadingIcon = {
                    Icon(painterResource(R.drawable.baseline_mode_24), contentDescription = null)},
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                value = addItemViewModel.itemUiState.itemDetails.price ?: "",
                onValueChange = { addItemViewModel.updateUiState(addItemViewModel.itemUiState.itemDetails.copy(price = it)) },
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.enter_item_price_field)) },
                leadingIcon = {
                    Icon(painterResource(R.drawable.baseline_mode_24), contentDescription = null)},
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                value = addItemViewModel.itemUiState.itemDetails.quantity ?: "",
                onValueChange = { addItemViewModel.updateUiState(addItemViewModel.itemUiState.itemDetails.copy(quantity = it)) },
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.enter_quantity_field)) },
                leadingIcon = {
                    Icon(painterResource(R.drawable.baseline_mode_24), contentDescription = null)},
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onDone)
            )
        }
        Button(
            onClick = { },
            modifier = Modifier
                .padding(top = 8.dp)
                .defaultMinSize(250.dp),
            shape = shapes.medium)
        {
            Text(text = "Scan code", style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            onClick = {
                scope.launch {
                    addItemViewModel.saveNewItem()
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .defaultMinSize(250.dp),
            shape = shapes.medium)
        {
            Text(text = "Save", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview
@Composable
fun AddItemScreenPreview() {
    AddItemScreen(navController = NavController(LocalContext.current))
}





