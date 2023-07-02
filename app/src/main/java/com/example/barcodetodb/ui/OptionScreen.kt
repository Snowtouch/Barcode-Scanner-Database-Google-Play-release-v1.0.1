package com.example.barcodetodb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.barcodetodb.R

@Composable
fun OptionScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OptionButton(
                text = R.string.add_scan_button,
                onClick = { /*TODO*/ })
            OptionButton(
                text = R.string.search_db_button,
                onClick = { /*TODO*/ })
            OptionButton(
                text = R.string.browse_db_button,
                onClick = { /*TODO*/ })
        }
    }
}

@Composable
fun OptionButton(
    text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ){
        Text(text = stringResource(id = text))
    }
}
@Preview
@Composable
fun OptionScreenPreview(){
    OptionScreen()
}