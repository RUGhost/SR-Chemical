package com.aminul.companion.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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

@Composable
fun TankLevelConverter() {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )
    val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value
    val localFocusManager = LocalFocusManager.current
    var level1 by remember { mutableStateOf("") }
    var level2 by remember { mutableStateOf("") }
    var level3 by remember { mutableStateOf("") }
    var level4 by remember { mutableStateOf("") }

    var filteredWeight1: String
    var filteredWeight2: String
    var filteredWeight3: String
    var filteredWeight4: String

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }


    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tank Level Converter",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))
            Column {
                Row {
                    OutlinedTextField(
                        value = level1,
                        onValueChange = {
                            level1 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                        },
                        label = { Text(text = "Tank Level - A") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext ={
                                localFocusManager.moveFocus(FocusDirection.Right)
                            }
                        )
                    )
                    OutlinedTextField(
                        value = level2,
                        onValueChange = {
                            level2 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                        },
                        label = { Text(text = "Tank Level - B") },
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusRequester1)
                            .fillMaxWidth(1f)
                            .padding(start = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                localFocusManager.moveFocus(FocusDirection.Left)
                                localFocusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )
                }
                Row {
                    OutlinedTextField(
                        value = level3,
                        onValueChange = {
                            level3 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                        },
                        label = { Text(text = "Tank Level - C") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext ={
                                localFocusManager.moveFocus(FocusDirection.Right)
                            }
                        )
                    )
                    OutlinedTextField(
                        value = level4,
                        onValueChange = {
                            level4 = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                        },
                        label = { Text(text = "Tank Level - D") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                localFocusManager.clearFocus()
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val user = arrayListOf<Double>()
                        user.add(level1.toDoubleOrNull() ?: 0.0)
                        user.add(level2.toDoubleOrNull() ?: 0.1)
                        user.add(level3.toDoubleOrNull() ?: 0.2)
                        user.add(level4.toDoubleOrNull() ?: 0.3)
                        userViewModel.getUserList(user)
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text(
                        text = "Submit",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(7.dp)
            ) {
                Text(
                    text = "Tank",
                    modifier = Modifier.weight(0.25f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Level",
                    modifier = Modifier.weight(0.25f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Volume",
                    modifier = Modifier.weight(0.25f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Weight",
                    modifier = Modifier.weight(0.25f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            val indexID1 = getUserRecord.indexOfFirst { it.level.equals(level1.toDoubleOrNull() ?:0.0)}
            val indexID2 = getUserRecord.indexOfFirst { it.level.equals(level2.toDoubleOrNull() ?:0.1)}
            val indexID3 = getUserRecord.indexOfFirst { it.level.equals(level3.toDoubleOrNull() ?:0.2)}
            val indexID4 = getUserRecord.indexOfFirst { it.level.equals(level4.toDoubleOrNull() ?:0.3)}

            val index1 = if (indexID1 == -1){ 0 } else indexID1
            val index2 = if (indexID2 == -1){ 0 } else indexID2
            val index3 = if (indexID3 == -1){ 0 } else indexID3
            val index4 = if (indexID4 == -1){ 0 } else indexID4

            if (getUserRecord.isNotEmpty()) {
                val weight1 = getUserRecord[index1].volume * 1.41
                val weight2 = getUserRecord[index2].volume * 1.41
                val weight3 = getUserRecord[index3].volume * 1.41
                val weight4 = getUserRecord[index4].volume * 1.41

                filteredWeight1 = getValidatedNumber(weight1.toString(), 2, 3)
                filteredWeight2 = getValidatedNumber(weight2.toString(), 2, 3)
                filteredWeight3 = getValidatedNumber(weight3.toString(), 2, 3)
                filteredWeight4 = getValidatedNumber(weight4.toString(), 2, 3)

                val weight = (getUserRecord[index1].volume + getUserRecord[index2].volume
                        + getUserRecord[index3].volume + getUserRecord[index4].volume) * 1.41
                val totalWeight = getValidatedNumber(weight.toString(),3,3)

                Column {
                    if (getUserRecord[index1].volume != 0.0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "Tank-A",
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = getUserRecord[index1].level.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getUserRecord[index1].volume.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = filteredWeight1,
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (getUserRecord[index2].volume != 0.0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "Tank-B",
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = getUserRecord[index2].level.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getUserRecord[index2].volume.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = filteredWeight2,
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (getUserRecord[index3].volume != 0.0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "Tank-C",
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = getUserRecord[index3].level.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getUserRecord[index3].volume.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = filteredWeight3,
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (getUserRecord[index4].volume != 0.0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "Tank-D",
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = getUserRecord[index4].level.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getUserRecord[index4].volume.toString(),
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = filteredWeight4,
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (totalWeight > 0.0.toString()){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.primaryVariant)
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "Total Weight",
                                modifier = Modifier.weight(0.75f),
                                color = MaterialTheme.colors.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = totalWeight,
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colors.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getValidatedNumber(text: String, beforeDot: Int, afterDot: Int): String {
    val filteredChars = text.filterIndexed { index, c ->
        c in "0123456789" || (c == '.' && text.indexOf('.') == index)
    }
    return if (filteredChars.contains('.')) {
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        beforeDecimal.take(beforeDot) + "." + afterDecimal.take(afterDot)
    } else {
        filteredChars.take(2)
    }
}
