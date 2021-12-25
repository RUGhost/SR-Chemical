package com.aminul.companion.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aminul.companion.database.UserViewModel
import com.aminul.companion.database.UserViewModelFactory
import kotlinx.coroutines.launch


@Composable
fun TankLevelConverter(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )
    val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value
    var level by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment= Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tank Level Converter",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = level,
                onValueChange = {
                        level = getValidatedNumber(text = it, beforeDot = 2, afterDot = 1)
                },
                label = { Text(text = "Enter Level")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone ={
                        localFocusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    if (level.isNotEmpty()){
                        scope.launch {
                            val user = arrayListOf<Double>()
                            user.add(level.toDouble())
                            userViewModel.getUserList(user)
                        }
                    }
                    else Toast.makeText(context,"Please Enter Number", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(10.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ){
                Text(
                    text = "Submit",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(7.dp)
            ){
                Text(
                    text = "Level",
                    modifier = Modifier.weight(0.3f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Volume",
                    modifier = Modifier.weight(0.3f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Weight",
                    modifier = Modifier.weight(0.3f),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (getUserRecord.isNotEmpty()){
                val weight = getUserRecord[0].volume * 1.41
                val filteredWeight = getValidatedNumber(weight.toString(), 2, 3)
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(7.dp)
                ){
                    Text(
                        text = getUserRecord[0].level.toString(),
                        modifier = Modifier.weight(0.3f),
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getUserRecord[0].volume.toString(),
                        modifier = Modifier.weight(0.3f),
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = filteredWeight,
                        modifier = Modifier.weight(0.3f),
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun getValidatedNumber(text: String, beforeDot: Int, afterDot: Int): String{
    val filteredChars = text.filterIndexed { index, c ->
        c in "0123456789" || (c == '.' && text.indexOf('.') == index)
    }
    return if (filteredChars.contains('.')){
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        beforeDecimal.take(beforeDot) + "." + afterDecimal.take(afterDot)
    } else {
        filteredChars.take(2)
    }
}
