package com.example.tetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tetris.ui.MainScreen
import com.example.tetris.ui.theme.TetrisTheme
import com.example.tetris.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen(
                viewModel = viewModel,
                onExit = {
                    finishAffinity()
                }
            )
        }
    }
}


@Composable
fun Bienvenida(modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenidos guapos",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun BienvenidaPreview() {
    TetrisTheme {
        Bienvenida()
    }
}
