package com.test.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.jettipapp.components.InputField
import com.test.jettipapp.ui.theme.JetTipAppTheme
import com.test.jettipapp.ui.theme.Shapes
import com.test.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                //TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp( content: @Composable () -> Unit ) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader( totalPerPerson: Double = 134.0 ) {
    Surface( modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .height(150.dp)
                        //.clip(shape = RoundedCornerShape(12.dp))
                        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
                             color = Color(0xFFE9D7F7)
    ) {
        Column( modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center ) {
            Text( text = "Total Per Person",
                  style = MaterialTheme.typography.h5 )
            Text( text = "$${String.format("%.2f", totalPerPerson)}",
                  style = MaterialTheme.typography.h4,
                  fontWeight = FontWeight.ExtraBold )
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {

    Column {
        BillForm() { billAmt ->
            Log.d("Amt", "MainContent: $billAmt")
        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun BillForm( modifier: Modifier = Modifier,
              onValChange: (String) -> Unit = {} ) {

    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val splitNumber = remember {
        mutableStateOf(1)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val range = IntRange(1, 100)

    TopHeader()

    Surface( modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 1.dp, color = Color.LightGray) ) {
        Column( modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start ) {
            InputField( valueState = totalBillState,
                        labelId = "Enter Bill",
                        enabled = true,
                        isSingleLine = true,
                        onAction = KeyboardActions {
                            if (!validState) return@KeyboardActions
                            onValChange(totalBillState.value.trim())
                            keyboardController?.hide()
                        } )

//            if(validState){
                Row( modifier = Modifier.padding(3.dp),
                     horizontalArrangement = Arrangement.Start ){
                    Text( text = "Split",
                          modifier = Modifier.align( alignment = Alignment.CenterVertically ) )
                    Spacer(modifier = Modifier.width(120.dp))

                    Row( modifier = Modifier.padding( horizontal = 3.dp ),
                         horizontalArrangement = Arrangement.End ){
                        RoundIconButton( imageVector = Icons.Default.Remove,
                                         onClick = {
                                             if (splitNumber.value == 1)
                                                 splitNumber.value = 1
                                             else splitNumber.value--
                                         } )

                        Text( text = splitNumber.value.toString(),
                              modifier = Modifier.align(Alignment.CenterVertically)
                                                 .padding(start = 9.dp, end = 9.dp) )

                        RoundIconButton( imageVector = Icons.Default.Add,
                            onClick = {
                                if( splitNumber.value < range.last )
                                    splitNumber.value++
                            } )
                    }
                }

                // Tip row
                Row( modifier = Modifier.padding( horizontal = 3.dp, vertical = 12.dp ) ) {
                    Text( text = "Tip",
                          modifier = Modifier.align( alignment = Alignment.CenterVertically ) )
                    Spacer( modifier = Modifier.width(200.dp) )
                    Text( text = "$33.00",
                          modifier = Modifier.align( alignment = Alignment.CenterVertically ) )
                }

                Column( verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally ) {
                    Text( text = "33%" )
                    Spacer( modifier = Modifier.height(14.dp) )
                    Slider( value = sliderPositionState.value, onValueChange = { newVal ->
                        Log.d("Slider", "BillForm: $newVal")
                        sliderPositionState.value = newVal
                    }, modifier = Modifier.padding( start = 16.dp, end = 16.dp ),
                    steps = 5,
                    onValueChangeFinished = {
                        //Log.d(TAG, "BillForm: a")
                    })
                }

//            }else{
//                Box() {}
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            Text(text = "Hello Again!")
        }
    }
}