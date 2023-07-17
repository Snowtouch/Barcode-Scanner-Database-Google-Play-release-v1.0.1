package com.example.barcodetodb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.barcodetodb.R

@Composable
fun SearchScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        UserInputField(label = R.string.enter_code_field, value = "", onValueChanged = {}, keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview(){
    SearchScreen()
}