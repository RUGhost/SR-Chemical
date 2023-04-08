package com.aminul.companion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TabThreeScreen() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colors.background),
//        contentAlignment = Alignment.Center
//    ){
//        Text(
//            text = "Tab Screen Three",
//            fontSize = MaterialTheme.typography.h3.fontSize,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colors.onBackground
//        )
//    }
}

@Composable
fun ScrollableTableWithFloatingButton() {
    val column1 = "Column 1"
    val column2 = listOf("Column 2 Child 1", "Column 2 Child 2")
    val column3 = listOf("Column 3 Child 1", "Column 3 Child 2")
    val column4 = listOf("Column 4 Child 1", "Column 4 Child 2")
    val column5 = listOf("Column 5 Child 1", "Column 5 Child 2")
    val column6 = "Column 6"

    var rows by remember { mutableStateOf(listOf("Row 1")) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { rows = rows + "Row ${rows.size + 1}" }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Row")
            }
        }
    ) {
        BoxWithConstraints {
            val maxWidth = constraints.maxWidth
            LazyRow(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                content = {
                    item {
                        Column(
                            modifier = Modifier
                                .widthIn(max = maxWidth.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            // Table header row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = column1,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                for (i in 0..3) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Column ${i + 2}",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Row {
                                            Text(text = column2[i], modifier = Modifier.weight(1f))
                                            Text(text = column3[i], modifier = Modifier.weight(1f))
                                            Text(text = column4[i], modifier = Modifier.weight(1f))
                                            Text(text = column5[i], modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                                Text(
                                    text = column6,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Table body rows
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                items(rows) { rowText ->
                                    Row(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(text = rowText, modifier = Modifier.weight(1f))
                                        for (i in 0..3) {
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = column2[i],
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = column3[i],
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = column4[i],
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = column5[i],
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                        Text(text = rowText, modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}