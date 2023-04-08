package com.aminul.companion.screen
import android.app.Application
import android.app.DatePickerDialog
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
import com.aminul.companion.appTheme.AppTheme
import com.aminul.companion.database.UserViewModel
import com.aminul.companion.database.UserViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ProductionScreen() {
    val timeOptions: List<String> = listOf("30M", "1H", "8H", "24H", "UD")
    var timeDiff: Long = 1
    val scrollState = rememberScrollState()
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Current Time",
                            modifier = Modifier.padding(start = 10.dp,top = 5.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Previous TIme",
                            modifier = Modifier.padding(top = 5.dp,end = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        currentTime = timePickerButton(context = context)
                        previousTime = timePickerButton(context = context)
                    }
                }
            }
            val currentSdfTime = dateFormatted(currentTime)
            val previousSdfTime = dateFormatted(previousTime)
            val days = (currentSdfTime.time - previousSdfTime.time)/86400000
            val hours = (currentSdfTime.time - previousSdfTime.time)%86400000/3600000
            val minutes = (currentSdfTime.time - previousSdfTime.time)%86400000%3600000/60000
            timeDiff = (days * 1440) + (hours * 60) + minutes

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
                    val production = getValidatedNumber(productionRate.toString(), 5, 1)
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
fun timePickerButton(context: Context): String {
    val calendar = Calendar.getInstance()
    val startYear = calendar.get(Calendar.YEAR)
    val startMonth = calendar.get(Calendar.MONTH)
    val startDay = calendar.get(Calendar.DAY_OF_MONTH)
    val startHour = calendar.get(Calendar.HOUR_OF_DAY)
    val startMinute = calendar.get(Calendar.MINUTE)

    val date = remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(context, { _, year, month, day ->
        TimePickerDialog(context, { _, hour, minute ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day, hour, minute)
            val monthStr: String = if ((month + 1).toString().length  == 1){
                "0${month + 1}"
            } else {
                month.toString()
            }
            val minuteStr: String = if (minute.toString().length == 1){
                "0$minute"
            } else{
                minute.toString()
            }
            date.value = "$day/$monthStr/$year $hour:$minuteStr"
        }, startHour, startMinute, true).show()
    }, startYear, startMonth, startDay)


    TextButton(
        modifier = Modifier.sizeIn(minWidth = 120.dp) ,
        onClick = { datePickerDialog.show() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
    ) {
        Text(text = date.value, fontSize = 15.sp)
    }
    return date.value
}
fun dateFormatted(date: String): Date {
    var fDate = Date()
    if (date.isNotEmpty()){
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        fDate = sdf.parse(date)!!
    }
    return fDate
}
