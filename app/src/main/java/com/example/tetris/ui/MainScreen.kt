package com.example.tetris.ui

import android.service.controls.templates.ControlButton
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris.viewmodel.MainViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.automirrored.filled.*



@Composable
fun MainScreen(viewModel: MainViewModel, onExit: () -> Unit) {
    val isPlaying by viewModel.isPlaying.collectAsState()

    if (!isPlaying) {
        StartMenu(
            onStart = { viewModel.startGame() },
            onExit = onExit
        )
    } else {
        GameUI(viewModel)
    }
}


@Composable
fun StartMenu(onStart: () -> Unit, onExit: () -> Unit) {
    val fadeInAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        fadeInAnim.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF000C1D), Color(0xFF0062C4))
                )
            )
            .graphicsLayer(alpha = fadeInAnim.value),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 🎮 Encabezado con estilo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "TETRIS",
                    fontSize = 36.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                    letterSpacing = 2.sp
                )
            }

            // Botón Iniciar
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0074D9),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar", fontSize = 18.sp)
            }

            // Botón Salir
            Button(
                onClick = onExit,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salir", fontSize = 18.sp)
            }
        }
    }
}



@Composable
fun GameUI(viewModel: MainViewModel) {
    val tetromino by viewModel.tetromino.collectAsState()
    val offset by viewModel.offset.collectAsState()
    val animatedOffset by animateFloatAsState(targetValue = offset.toFloat(), label = "tetrominoOffset")
    val gameOver by viewModel.gameOver.collectAsState()
    val board = viewModel.game.board
    val rows = board.size
    val cols = board[0].size
    val cellSize = 24.dp
    val flashingRows by viewModel.flashingRows.collectAsState()
    val score by viewModel.score.collectAsState()
    val highScore by viewModel.highScore.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isPaused by viewModel.isPaused.collectAsState()

    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            viewModel.pauseGame()
        } else {
            viewModel.resumeGame()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxSize(),
                drawerContainerColor = Color.Transparent  // Hacemos la hoja del drawer transparente
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color(0xF0001F3F),  // 🔵 94% opaco
                                    0.2f to Color(0xE0001F3F),  // 88%
                                    0.4f to Color(0xC0001F3F),  // 75%
                                    0.6f to Color(0x80001F3F),  // 50%
                                    1.0f to Color.Transparent   // 0%
                                )
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Título del menú
                        Text(
                            text = "🎮 MENÚ DE JUEGO",
                            color = Color.White,
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.titleLarge
                        )

                        HorizontalDivider(color = Color.White.copy(alpha = 0.4f))

                        // Botón Reanudar
                        Button(
                            onClick = { scope.launch { drawerState.close() } },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x9900B894),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reanudar")
                        }

                        // Botón Salir
                        Button(
                            onClick = {
                                viewModel.exitGame()
                                scope.launch { drawerState.close() }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x99D63031),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salir al menú")
                        }
                    }
                }
            }
        }


    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF001F3F), Color(0xFF0074D9))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 🔹 Header con botón de menú
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Puntaje: $score", color = Color.White, fontSize = 20.sp)
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }

            Text("Mejor: $highScore", color = Color.Yellow, fontSize = 16.sp)

            // 🔹 Tablero
            Box(
                modifier = Modifier
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .width(cellSize * cols)
                        .height(cellSize * rows)
                ) {
                    for (row in board.indices) {
                        for (col in board[0].indices) {
                            val cell = board[row][col]
                            val isFlashing = row in flashingRows
                            if (cell.isFilled || isFlashing) {
                                drawRoundRect(
                                    color = if (isFlashing) Color.White else Color(cell.color),
                                    topLeft = Offset(col * cellSize.toPx(), row * cellSize.toPx()),
                                    size = Size(cellSize.toPx(), cellSize.toPx()),
                                    cornerRadius = CornerRadius(6.dp.toPx())
                                )
                            }
                        }
                    }

                    for ((row, col) in tetromino.shape) {
                        val animatedRow = row + animatedOffset
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(tetromino.color).copy(alpha = 0.95f),
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            ),
                            topLeft = Offset(col * cellSize.toPx(), animatedRow * cellSize.toPx()),
                            size = Size(cellSize.toPx(), cellSize.toPx()),
                            cornerRadius = CornerRadius(6.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!gameOver) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Fila de movimiento y rotación
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ControlIconButton(icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft) { viewModel.moveLeft() }
                        ControlIconButton(icon = Icons.AutoMirrored.Filled.RotateRight) { viewModel.rotate() }
                        ControlIconButton(icon = Icons.AutoMirrored.Filled.KeyboardArrowRight) { viewModel.moveRight() }
                    }

                    // Fila de drops
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DropButton(
                            text = "Soft Drop",
                            icon = Icons.Filled.KeyboardArrowDown,
                            backgroundColor = Color(0xFF004080),
                            onClick = { viewModel.softDrop() }
                        )
                        DropButton(
                            text = "Hard Drop",
                            icon = Icons.Filled.FileDownload,
                            backgroundColor = Color(0xFFD63031),
                            onClick = { viewModel.hardDrop() }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            if (gameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xAA000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("☠️ GAME OVER ☠️", color = Color.White, fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        DropButton(
                            text = "Reiniciar",
                            icon = Icons.Filled.Refresh,
                            backgroundColor = Color(0xFF2E86DE)
                        ) {
                            viewModel.restartGame()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ControlIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .background(Color(0xFF004080), shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun DropButton(text: String, icon: ImageVector, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        modifier = Modifier.height(48.dp),
        elevation = ButtonDefaults.buttonElevation(6.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

