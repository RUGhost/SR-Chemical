package com.aminul.companion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Caustic() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        CustomRow(
            padding = 10.dp,
            fontSize = 16.sp,
            text = "Current Level of Tank",
            labelWeight = .7f,
            textFieldWeight = .3f
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomRow(
            padding = 10.dp,
            fontSize = 16.sp,
            text = "Previous Level of Tank",
            labelWeight = .7f,
            textFieldWeight = .3f
        )
    }
}

@Composable
fun CustomRow(padding: Dp, fontSize: TextUnit, text: String, labelWeight: Float, textFieldWeight: Float) {
    var textBoxValue by remember { mutableStateOf("") }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface, RoundedCornerShape(percent = 10))
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(padding)
                .weight(labelWeight),
            fontSize = fontSize
        )
        BasicTextField(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colors.background, RoundedCornerShape(percent = 20))
                .fillMaxWidth()
                .weight(textFieldWeight),
            value = textBoxValue,
            onValueChange = { textBoxValue = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface,
                fontSize = fontSize,
                textAlign = TextAlign.End
            )
        )
    }
}