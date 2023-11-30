package com.example.alarmapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alarmapp.model.data.AlarmDatabaseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //MemoryGame()
            //PhoneShaking()
            //MathEquation(difficulty = Difficulty.EASY)

            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val alarmDatabaseViewModel: AlarmDatabaseViewModel = viewModel(
                    it,
                    "AlarmDataBaseViewModel",
                    AlarmDatabaseViewModelFactory(
                        LocalContext.current.applicationContext as Application
                    )
                )
                AlarmClockApp(alarmDatabaseViewModel = alarmDatabaseViewModel)
            }
        }
    }
}

class AlarmDatabaseViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmDatabaseViewModel(application) as T
    }
}