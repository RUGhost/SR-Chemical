package com.aminul.companion.screen

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ActualProductionScreen() {
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

            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(context.applicationContext as Application)
            )
            val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value
            var currentLevel by remember { mutableStateOf("") }
            var previousLevel by remember { mutableStateOf("") }

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
                        if (currentLevel.isEmpty() || previousLevel.isEmpty()) {
                            Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show()
                        }
                        else {
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
                    if (getUserRecord.isNotEmpty()) {
                        val previousWeight = getUserRecord[0].volume * 1.41
                        val currentWeight = getUserRecord[1].volume * 1.41
                        val productionRate = (currentWeight - previousWeight) * 1000
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


