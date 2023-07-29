package com.example.barcodetodb.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.barcodetodb.data.Item
import java.time.LocalDate

@Composable
fun MainScreen(viewModel: MainScreenViewModel = viewModel()){
    val itemFlow by viewModel.itemFlow.collectAsState(initial = emptyList())
    ItemsList(itemFlow = itemFlow)
}
@Composable
fun ItemsList(
    itemFlow: List<Item>,
    modifier: Modifier = Modifier
    ){
    Column(modifier = modifier.background(color = Color.White)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = "ID", modifier = modifier.weight(2f))
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
            items(itemFlow) { item ->
                Card(
                    modifier = modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                    border = BorderStroke(width = Dp.Hairline, Color.Black),
                    onClick = { }
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.id.toString(),
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
                }
            }
        }
    }
}
@Composable
fun BottomBar(
    modifier: Modifier = Modifier
){
    BottomAppBar(modifier = modifier) {

    }
}

@Preview
@Composable
fun MainScreenPreview(){
    ItemsList(itemFlow = listOf(
        Item(1,"45363456","AAAAAAA", 33.toDouble(),54,LocalDate.now().toString()),
                Item(1,"453634554363456346","AAA",33.toDouble(),54,LocalDate.now().toString()),
        Item(1,"45363455436346","AAA",33.toDouble(),5455,LocalDate.now().toString())
    ))
}