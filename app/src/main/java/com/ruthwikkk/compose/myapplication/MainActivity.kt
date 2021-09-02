package com.ruthwikkk.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruthwikkk.compose.myapplication.ui.theme.ClickBallTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickBallTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {

    var points by remember {
        mutableStateOf(0)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Points $points",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                points = 0
                isTimerRunning = !isTimerRunning
            }) {
                Text(text = if(isTimerRunning) "Reset" else "Start")
            }
            CountDownTimer(
                isTimerRunning = isTimerRunning
            ){
                isTimerRunning = false
            }
        }
    }
}

@Composable
fun CountDownTimer(
    time: Int = 30000,
    isTimerRunning: Boolean = false,
    onTimerEnd: () -> Unit = {}
) {
    var currentTime by remember {
        mutableStateOf(time)
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning){
        if(!isTimerRunning){
            currentTime = time
            return@LaunchedEffect
        }

        if(currentTime > 0){
            delay(1000)
            currentTime -= 1000
        }else{
            onTimerEnd()
        }
    }
    Text(
        text = (currentTime/1000).toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}
