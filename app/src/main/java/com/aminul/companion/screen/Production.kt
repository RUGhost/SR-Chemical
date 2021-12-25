package com.aminul.companion.screen

import android.app.Application
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aminul.companion.database.UserViewModel
import com.aminul.companion.database.UserViewModelFactory
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun ProductionScreen() {
    val timeOptions: List<String> = listOf("30M", "1H", "8H", "24H", "UD")
    var timeDiff = 1
    val scrollState = rememberScrollState()
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Chlorine Production Calculator",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        val selectedTime = radioGroup(
            radioOptions = timeOptions,
            title = "SELECT THE PRODUCTION TIME",
            fontSize = 13.sp,
            cardBackgroundColor = MaterialTheme.colors.surface
        )
        var isCustom = false
        when (selectedTime) {
            "30M" -> timeDiff = 30
            "1H" -> timeDiff = 60
            "8H" -> timeDiff = 480
            "24H" -> timeDiff = 1440
            "UD" -> isCustom = true
        }


            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(context.applicationContext as Application)
            )
            val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value
            var currentLevel by remember { mutableStateOf("") }
            var previousLevel by remember { mutableStateOf("") }
            var currentTime by remember { mutableStateOf("") }
            var previousTime by remember { mutableStateOf("") }

        if (isCustom) {
            Surface(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                color = MaterialTheme.colors.surface,
                elevation = 8.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column {
                    Text(
                        text = "SELECT TIME",
                        modifier = Modifier.padding(start = 10.dp,top = 5.dp,end = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        currentTime = timePickerButton(context = context, title = "Current")
                        previousTime = timePickerButton(context = context, title = "Previous")
                    }
                }
            }
            timeDiff = currentTime.toInt() - previousTime.toInt()
        }
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, end = 15.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentLevel,
                    onValueChange = {
                        currentLevel = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext ={
                            localFocusManager.moveFocus(FocusDirection.Right)
                        }
                    )
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone ={
                            localFocusManager.clearFocus()
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (timeDiff < 1) {
                        Toast.makeText(
                            context,
                            "Current Time Should be Larger than Previous Time",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else if (currentLevel.isEmpty() || previousLevel.isEmpty()) {
                        Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show()
                    } else if (currentLevel < previousLevel || currentLevel == previousLevel) {
                        Toast.makeText(
                            context,
                            "Current Level Should be Larger than Previous Level",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        scope.launch {
                            val usersId = arrayListOf<Double>()
                            usersId.add(currentLevel.toDouble())
                            usersId.add(previousLevel.toDouble())
                            userViewModel.getUserList(usersId)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(10.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
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
                    .background(MaterialTheme.colors.primary)
                    .padding(7.dp)
            ) {
                Text(
                    text = "Production Rate:",
                    modifier = Modifier.weight(0.3f),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if(timeDiff < 1){
                    Text(
                        text = "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                else if (getUserRecord.isNotEmpty()) {
                    val previousWeight = getUserRecord[0].volume * 1.41
                    val currentWeight = getUserRecord[1].volume * 1.41
                    val productionRate = ((currentWeight - previousWeight) / timeDiff) * 60 * 1000
                    val production = getValidatedNumber(productionRate.toString(), 3, 1)
                    Text(
                        text = "$production kg/Hr",
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
    fontSize: TextUnit = 11.sp,
    cardBackgroundColor: Color = Color(0xFFFEFEFA)
): String {
    if (radioOptions.isNotEmpty()) {
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(radioOptions[1])
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = fontSize,
                                        color = MaterialTheme.colors.onSurface
                                    )
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

@Composable
fun timePickerButton(context: Context, title: String): String {
    val calendar = Calendar.getInstance()
    val hourState = remember { mutableStateOf(calendar[Calendar.HOUR_OF_DAY]) }
    val minuteState = remember { mutableStateOf(calendar[Calendar.MINUTE]) }
    val timePickerDialog = TimePickerDialog(
        context, { _, hour: Int, minute: Int ->
            hourState.value = hour
            minuteState.value = minute
        }, hourState.value, minuteState.value, true
    )
    val timeInMinute = (hourState.value * 60) + minuteState.value

    TextButton(
        onClick = { timePickerDialog.show() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
    ) {
        Text(text = title + ": ${hourState.value}:${minuteState.value}", fontSize = 15.sp)
    }
    return "$timeInMinute"
}
