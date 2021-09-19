package com.ruthwikkk.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruthwikkk.compose.myapplication.ui.theme.ClickBallTheme
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

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

enum class GameState{
    PLAY, LOADING, WON, LOST
}

@Composable
fun MainScreen() {

    var points by remember {
        mutableStateOf(0)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    var state by remember {
        mutableStateOf(GameState.LOADING)
    }

    var level by remember {
        mutableStateOf(1)
    }

    LaunchedEffect(key1 = state){
        if(state == GameState.LOADING){
            delay(1500)
            state = GameState.PLAY

            points = 0
            isTimerRunning = !isTimerRunning
        }
    }

    when(state){
        GameState.LOADING -> {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Level $level",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
        GameState.PLAY -> {
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
                    CountDownTimer(
                        isTimerRunning = isTimerRunning
                    ){
                        isTimerRunning = false
                        when(level){
                            1 -> {
                                state = if(points > 20){
                                    level++
                                    GameState.LOADING
                                }else{
                                    GameState.LOST
                                }
                            }
                            2 -> {
                                state = if(points > 25){
                                    level++
                                    GameState.LOADING
                                }else{
                                    GameState.LOST
                                }
                            }
                            3 -> {
                                state = if(points > 30){
                                    level++
                                    GameState.LOADING
                                }else{
                                    GameState.LOST
                                }
                            }
                            4 -> {
                                state = if(points > 35){
                                    level++
                                    GameState.LOADING
                                }else{
                                    GameState.LOST
                                }
                            }
                            5 -> {
                                state = if(points > 40){
                                    level++
                                    GameState.WON
                                }else{
                                    GameState.LOST
                                }
                            }
                        }
                    }
                }

                ClickBall(enabled = isTimerRunning) {
                    points++
                }
            }
        }
        GameState.LOST -> {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "You Lost :(",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }

        GameState.WON -> {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Congratulations, You Won",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
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

@Composable
fun ClickBall(
    radius: Float = 100f,
    enabled: Boolean = false,
    ballColor: Color = Color.Red,
    onClick: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var position by remember {
            mutableStateOf(
                randomOffset(
                    radius = radius,
                    width = constraints.maxWidth,
                    height = constraints.maxHeight
                )
            )
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(enabled) {
                if (!enabled)
                    return@pointerInput

                detectTapGestures {
                    val distance = sqrt(
                        (it.x - position.x).pow(2) +
                                (it.y - position.y).pow(2)
                    )
                    if (distance <= radius) {
                        position = randomOffset(radius, constraints.maxWidth, constraints.maxHeight)
                        onClick()
                    }
                }
            }) {
            drawCircle(ballColor, radius, position)
        }
    }
}

private fun randomOffset(radius: Float, width: Int, height: Int): Offset {
    return Offset(
        x = Random.nextInt(radius.roundToInt(), (width - radius).roundToInt()).toFloat(),
        y = Random.nextInt(radius.roundToInt(), (height - radius).roundToInt()).toFloat()
    )
}