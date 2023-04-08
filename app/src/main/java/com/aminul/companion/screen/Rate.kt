package com.aminul.companion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RateScreen() {
   Table()
}

@Composable
fun TableCell(text: String, width: Dp){
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, Color.Black)
            .width(width)
            .defaultMinSize(120.dp)
            .padding(8.dp)
    )
}

@Composable
fun Table() {
    val tableData = (1..31).mapIndexed { index, _ ->
        "${index + 1}.01.2023"
    }
    val columnWidth = 150
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .horizontalScroll(rememberScrollState())
    ){
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(modifier = Modifier.background(Color.Gray)) {
                TableCell(text = "Column One", width = columnWidth.dp)
                TableCell(text = "Column Two", width = columnWidth.dp)
                TableCell(text = "Column Three", width = columnWidth.dp)
                TableCell(text = "Column Four", width = columnWidth.dp)
                TableCell(text = "Column Five", width = columnWidth.dp)
            }
        }
        items(tableData) {
            val id = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = id, width = columnWidth.dp)
                TableCell(text = "", width = columnWidth.dp)
                TableCell(text = "", width = columnWidth.dp)
                TableCell(text = "", width = columnWidth.dp)
                TableCell(text = "", width = columnWidth.dp)
            }
        }
    }
}
}