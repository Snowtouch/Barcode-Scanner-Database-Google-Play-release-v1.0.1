package com.example.barcodetodb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.barcodetodb.R

@Composable
fun SearchScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        UserInputField(label = R.string.enter_code_field, value = "", onValueChanged = {})
    }
}

@Preview
@Composable
fun SearchScreenPreview(){
    SearchScreen()
}