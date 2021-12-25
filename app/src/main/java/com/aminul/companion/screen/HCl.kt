package com.aminul.companion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun HCl() {
    Table()
}

@Composable
fun RowScope.TableCell(text: String, weight: Float){
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun Table(){
    val tableData = (1..5).mapIndexed { index, _ ->
        index to "Item $index"
    }
    val columnOneWeight = 0.3f
    val columnTwoWeight = 0.3f
    val columnThreeWeight = 0.3f
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)){
        item {
            Row(modifier = Modifier.background(Color.Gray)) {
                TableCell(text = "Column One", weight = columnOneWeight)
                TableCell(text = "Column Two", weight = columnTwoWeight)
                TableCell(text = "Column Three", weight = columnThreeWeight)
            }
        }
        items (tableData){
            val (id, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = id.toString(), weight = columnOneWeight)
                TableCell(text = text, weight = columnTwoWeight)
                TableCell(text = text, weight = columnThreeWeight)
            }
        }
    }
}