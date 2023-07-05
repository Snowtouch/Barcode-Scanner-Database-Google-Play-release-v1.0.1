package com.example.barcodetodb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.barcodetodb.R

@Composable
fun AddItemScreen(barcodeViewModel : BarcodeViewModel = viewModel()){

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
            //colors = CardDefaults.elevatedCardColors(colorScheme.secondary)
        ){
            UserInputField(
                label = R.string.enter_code_field,
                value = barcodeViewModel.userInput,
                onValueChanged = { barcodeViewModel.userInput = it },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.baseline_mode_24),
                        contentDescription = null)})
            Spacer(modifier = Modifier.padding(4.dp))

            UserInputField(
                label = R.string.enter_name_field,
                value = barcodeViewModel.userInput,
                onValueChanged = { barcodeViewModel.userInput = it},
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.baseline_mode_24),
                        contentDescription = null) })
        }
        AddScreenButton(
            onClick = { /*TODO*/ },
            label = "Scan")
    }
}
@Composable
fun AddScreenButton(
    onClick: () -> Unit,
    label: String
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp)
            .defaultMinSize(250.dp),
        shape = shapes.medium)
    {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
fun UserInputField(
    label: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier){
    TextField(
        value = value,
        singleLine = true,
        shape = shapes.medium,
        leadingIcon = { leadingIcon() },
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(colorScheme.surface),
        onValueChange = {onValueChanged},
        label = {Text(stringResource(label))},
    )
}
@Preview
@Composable
fun AddItemScreenPreview(){
    AddItemScreen()
}