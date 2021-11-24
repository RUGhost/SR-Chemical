package com.aminul.companion.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aminul.companion.database.UserViewModel
import com.aminul.companion.database.UserViewModelFactory
import com.aminul.companion.ui.theme.Purple500
import kotlinx.coroutines.launch


@Composable
fun ProductionScreen() {
    val timeOptions: List<String> = listOf("1H", "30M", "8H", "24H", "Custom")
    var timeDiff = 1
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Chlorine Production Calculator",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        val selectedTime = radioGroup(
            radioOptions = timeOptions,
            title = "Select the production time",
            cardBackgroundColor = Color(0xFFF8F8FF)
        )

        when (selectedTime) {
            "30M" -> timeDiff = 30
            "1H" -> timeDiff = 60
            "8H" -> timeDiff = 480
            "24H" -> timeDiff = 1440
            "Custom" -> timeDiff = 1  //I will calculate user defined time later
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(context.applicationContext as Application)
            )
            val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value
            var currentLevel by remember { mutableStateOf("") }
            var previousLevel by remember { mutableStateOf("") }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentLevel,
                    onValueChange = {
                        currentLevel = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedTextField(
                    value = previousLevel,
                    onValueChange = {
                        previousLevel = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "Previous Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (currentLevel.isEmpty() || previousLevel.isEmpty()) {
                        Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show()
                    } else {
                        //want to query current level in DB. This query give me id, level and volume
                        scope.launch {
                            userViewModel.getUser(currentLevel.toDouble())
                        }
                        //want to query previous level in DB. This query give me id, level and volume
                        scope.launch {
                            userViewModel.getUser(previousLevel.toDouble())
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(10.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Purple500)
            ) {
                Text(
                    text = "Submit",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple500)
                    .padding(15.dp)
            ) {
                Text(
                    text = "Production Rate",
                    modifier = Modifier.weight(0.3f),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                if (getUserRecord.isNotEmpty()) {

//                    val currentWeight = getUserRecord[0].volume * 1.41
//                    val previousWeight = getUserRecord[1].volume * 1.41
//                    val productionRate = (currentWeight - previousWeight) / timeDiff
//    But it does not work

                    Text(
                        text = "productionRate",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}

@Composable
fun radioGroup(
    radioOptions: List<String> = listOf(),
    title: String = "",
    cardBackgroundColor: Color = Color(0xFFFEFEFA)
): String {
    if (radioOptions.isNotEmpty()) {
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(radioOptions[0])
        }

        Card(
            backgroundColor = cardBackgroundColor,
            modifier = Modifier
                .padding(start = 5.dp, top = 10.dp, end = 5.dp, bottom = 10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                Modifier.padding(5.dp)
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)

                )
                Row(modifier = Modifier.fillMaxWidth()) {

                    radioOptions.forEach { item ->
                        Row(
                            Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (item == selectedOption),
                                onClick = { onOptionSelected(item) },

                                )

                            val annotatedString = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) { append("  $item  ") }
                            }

                            ClickableText(
                                text = annotatedString,
                                onClick = {
                                    onOptionSelected(item)
                                }
                            )
                        }
                    }
                }
            }
        }
        return selectedOption
    } else {
        return ""
    }
}
