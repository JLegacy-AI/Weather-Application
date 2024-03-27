package com.jlegacyai.weatherapplication

import android.content.SharedPreferences

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCity(navController: NavController){
    var cityName: String by rememberSaveable {
        mutableStateOf("")
    }


    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text("Select City")
                        })
            },
            content = Content()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(): @Composable (PaddingValues) -> Unit {
    var inputCity by rememberSaveable { mutableStateOf("") }
    val sharedPrefManager = SharedPrefManager(LocalContext.current)

    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
        )
        Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Map",
                modifier = Modifier.fillMaxWidth()
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

@Composable
fun Report(navController: NavController) {
    val sharedPrefManager = SharedPrefManager(LocalContext.current)
    val cityName = sharedPrefManager.getValue("city", "") as String

    val weatherInfo = when (cityName) {
        "Karachi" -> resources.getStringArray(R.array.weather_info_karachi)
        "Lahore" -> resources.getStringArray(R.array.weather_info_lahore)
        "Islamabad" -> resources.getStringArray(R.array.weather_info_islamabad)
        "Quetta" -> resources.getStringArray(R.array.weather_info_quetta)
        "Peshawar" -> resources.getStringArray(R.array.weather_info_peshawar)
        "Multan" -> resources.getStringArray(R.array.weather_info_multan)
        "Faisalabad" -> resources.getStringArray(R.array.weather_info_faisalabad)
        "Sialkot" -> resources.getStringArray(R.array.weather_info_sialkot)
        "Gujranwala" -> resources.getStringArray(R.array.weather_info_gujranwala)
        "Hyderabad" -> resources.getStringArray(R.array.weather_info_hyderabad)
        else -> emptyArray()
    }

    Column {
        Row {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Thermometer")
            Column {
                Text(text = "Temperature")
                Text(text = weatherInfo[1])
            }
        }
        Row {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Humidity")
            Column {
                Text(text = "Humidity")
                Text(text = weatherInfo[2])
            }
        }
        Row {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Condition")
            Column {
                Text(text = "Condition")
                Text(text = weatherInfo[3])
            }
        }
    }
}


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
