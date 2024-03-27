package com.jlegacyai.weatherapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppStart()
        }
    }
}

@Composable
fun AppStart(){
    var navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start"){
        composable("start"){
            CustomSplashScreen(navController = navController)
        }

        composable("city"){
            SelectCity(navController)
        }

        composable("report"){
            Report(navController)
        }
    }
}


/**
 * Screen Splash Screen
 */
@Composable
fun CustomSplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("city")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splash Screen ICON",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


/**
 *
 * Select City Screen
 *
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCity(navController: NavController){

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text("Select City")
                        })
            },
        content = {
            Content(navController)
        }
    )
}


// Content of Scaffold Layout
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(navController: NavController) {
    var inputCity by rememberSaveable { mutableStateOf("") }
    val sharedPrefManager = SharedPrefManager(LocalContext.current)

    Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
        )
        Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Map",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        TextField(
                value = inputCity,
                onValueChange = { inputCity = it },
                modifier = Modifier.padding(16.dp)
        )
        Button(
                onClick = {
                    sharedPrefManager.saveValue("city", inputCity)
                    navController.navigate("report")
                },
                modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Show Weather")
        }
    }
}


/**
 * Report Screen which Show the Temperature of the City
 */
@Composable
fun Report(navController: NavController) {
    val sharedPrefManager = SharedPrefManager(LocalContext.current)
    val cityName:String = sharedPrefManager.getValue("city", "") as String

    val weatherInfo = when (cityName.lowercase()) {
        "karachi" -> stringArrayResource(id = R.array.weather_info_karachi)
        "lahore" -> stringArrayResource(R.array.weather_info_lahore)
        "islamabad" -> stringArrayResource(R.array.weather_info_islamabad)
        "quetta" -> stringArrayResource(R.array.weather_info_quetta)
        "peshawar" -> stringArrayResource(R.array.weather_info_peshawar)
        "multan" -> stringArrayResource(R.array.weather_info_multan)
        "faisalabad" -> stringArrayResource(R.array.weather_info_faisalabad)
        "sialkot" -> stringArrayResource(R.array.weather_info_sialkot)
        "gujranwala" ->stringArrayResource(R.array.weather_info_gujranwala)
        "hyderabad" -> stringArrayResource(R.array.weather_info_hyderabad)
        else -> emptyArray<String>()
    }



    if(weatherInfo.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Information not Present \uD83D\uDE14", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = {
                navController.navigate("city")
            }) {
                Text(text = "Go Back")
            }
        }
    }else{
        val weatherIconId = when(weatherInfo[3].lowercase()) {
            "cloudy" -> R.drawable.cloudy
            "hot" -> R.drawable.hot
            "rainy" -> R.drawable.rainy
            "snow" -> R.drawable.snow
            "sunny" -> R.drawable.sunny
            else -> R.drawable.sunny
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherCard(
                iconId = R.drawable.temperature,
                title = "Temperature",
                value = weatherInfo[1]
            )
            WeatherCard(
                iconId = R.drawable.humidity,
                title = "Humidity",
                value = weatherInfo[2]
            )
            WeatherCard(
                iconId = weatherIconId,
                title = "Condition",
                value = weatherInfo[3]
            )
            Button(onClick = {
                navController.navigate("city")
            }){
                Text(text = "Go Back")
            }
        }
    }
}

// WeatherCard Used in Report Screen
@Composable
fun WeatherCard(iconId: Int, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(8.dp, 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 12.dp)) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = title,
                modifier = Modifier
                    .size(56.dp)
                    .padding(8.dp)
            )
            Column {
                Text(text = title, style = MaterialTheme.typography.headlineMedium)
                Text(text = value, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}


/**
 *  SharedPreference Manager Class
 */
class SharedPrefManager(context: Context) {

    private var sharedPref: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveValue(key: String, value: Any) {
        when (value) {
            is String -> sharedPref.edit().putString(key, value).apply()
            is Int -> sharedPref.edit().putInt(key, value).apply()
            is Boolean -> sharedPref.edit().putBoolean(key, value).apply()
            is Float -> sharedPref.edit().putFloat(key, value).apply()
            is Long -> sharedPref.edit().putLong(key, value).apply()
        }
    }

    fun getValue(key: String, defaultValue: Any): Any? {
        return when (defaultValue) {
            is String -> sharedPref.getString(key, defaultValue)
            is Int -> sharedPref.getInt(key, defaultValue)
            is Boolean -> sharedPref.getBoolean(key, defaultValue)
            is Float -> sharedPref.getFloat(key, defaultValue)
            is Long -> sharedPref.getLong(key, defaultValue)
            else -> null
        }
    }
}