package com.aminul.companion.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aminul.companion.database.UserViewModel
import com.aminul.companion.database.UserViewModelFactory
import kotlinx.coroutines.launch



@Composable
fun ActualProductionScreen() {
    val scrollState = rememberScrollState()
    val localFocusManager = LocalFocusManager.current
    val (focusRequesters1, focusRequesters2) = remember {
        List(8) { FocusRequester() } to List(4) { FocusRequester() }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val userViewModel: UserViewModel = viewModel(
            factory = UserViewModelFactory(context.applicationContext as Application)
        )
        val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value

        var currentLevel1 by remember { mutableStateOf("") }
        var currentLevel2 by remember { mutableStateOf("") }
        var currentLevel3 by remember { mutableStateOf("") }
        var currentLevel4 by remember { mutableStateOf("") }
        var previousLevel1 by remember { mutableStateOf("") }
        var previousLevel2 by remember { mutableStateOf("") }
        var previousLevel3 by remember { mutableStateOf("") }
        var previousLevel4 by remember { mutableStateOf("") }


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
                    value = currentLevel1,
                    onValueChange = {
                        currentLevel1 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-A Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters1[0]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters1[1].requestFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedTextField(
                    value = previousLevel1,
                    onValueChange = {
                        previousLevel1 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-A Previous Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters2[0]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters2[1].requestFocus()
                        }
                    )
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentLevel2,
                    onValueChange = {
                        currentLevel2 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-B Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters1[1]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters1[2].requestFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedTextField(
                    value = previousLevel2,
                    onValueChange = {
                        previousLevel2 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-B Previous Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters2[1]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters2[2].requestFocus()
                        }
                    )
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentLevel3,
                    onValueChange = {
                        currentLevel3 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-C Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters1[2]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters1[3].requestFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedTextField(
                    value = previousLevel3,
                    onValueChange = {
                        previousLevel3 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-C Previous Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters2[2]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters2[3].requestFocus()
                        }
                    )
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentLevel4,
                    onValueChange = {
                        currentLevel4 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-D Current Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters1[3]),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters2[0].requestFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedTextField(
                    value = previousLevel4,
                    onValueChange = {
                        previousLevel4 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                    },
                    label = { Text(text = "T-D Previous Level") },
                    singleLine = true,
                    modifier = Modifier.weight(0.5f).focusRequester(focusRequesters2[3]),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            localFocusManager.clearFocus()
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (currentLevel1.isEmpty() || previousLevel1.isEmpty() ||
                        currentLevel2.isEmpty() || previousLevel2.isEmpty() ||
                        currentLevel3.isEmpty() || previousLevel3.isEmpty() ||
                        currentLevel4.isEmpty() || previousLevel4.isEmpty()
                    ) {
                        Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            val usersId = arrayListOf<Double>()
                            usersId.add(currentLevel1.toDouble())
                            usersId.add(previousLevel1.toDouble())
                            usersId.add(currentLevel2.toDouble())
                            usersId.add(previousLevel2.toDouble())
                            usersId.add(currentLevel3.toDouble())
                            usersId.add(previousLevel3.toDouble())
                            usersId.add(currentLevel4.toDouble())
                            usersId.add(previousLevel4.toDouble())
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

                fun getIndex(level: String): Int {
                    return getUserRecord.indexOfFirst {
                        it.level.equals(
                            level.toDoubleOrNull() ?: 0.0
                        )
                    }
                        .let { if (it == -1) 0 else it }
                }

                val indexC1 = getIndex(currentLevel1)
                val indexC2 = getIndex(currentLevel2)
                val indexC3 = getIndex(currentLevel3)
                val indexC4 = getIndex(currentLevel4)
                val indexP1 = getIndex(previousLevel1)
                val indexP2 = getIndex(previousLevel2)
                val indexP3 = getIndex(previousLevel3)
                val indexP4 = getIndex(previousLevel4)

                if (getUserRecord.isNotEmpty()) {
                    fun calculateWeight(index: Int): Double {
                        return getUserRecord[index].volume * 1.41
                    }

                    val weightC1 = calculateWeight(indexC1)
                    val weightC2 = calculateWeight(indexC2)
                    val weightC3 = calculateWeight(indexC3)
                    val weightC4 = calculateWeight(indexC4)
                    val weightP1 = calculateWeight(indexP1)
                    val weightP2 = calculateWeight(indexP2)
                    val weightP3 = calculateWeight(indexP3)
                    val weightP4 = calculateWeight(indexP4)

                    val productionRate = (
                            (weightC1 - weightP1) +
                                    (weightC2 - weightP2) +
                                    (weightC3 - weightP3) +
                                    (weightC4 - weightP4)
                            ) * 1000

                    val production = getValidNumber(productionRate.toString(), 6, 2)
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

fun getValidNumber(text: String, beforeDot: Int, afterDot: Int): String {
    var filteredChars = text.filterIndexed { index, c ->
        c in "0123456789" || (c == '.' && text.indexOf('.') == index) || (c == '-' && index == 0)
    }
    val isNegative = filteredChars.startsWith('-')
    if (isNegative) {
        filteredChars = filteredChars.substring(1)
    }
    filteredChars = if (filteredChars.contains('.')) {
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        beforeDecimal.take(beforeDot) + "." + afterDecimal.take(afterDot)
    } else {
        filteredChars.take(6)
    }
    if (isNegative) {
        filteredChars = "-$filteredChars"
    }
    return filteredChars
}