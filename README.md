# 🎮 Tetris Game - Android App

Este es un proyecto de Tetris desarrollado con tecnologías modernas de Android, siguiendo buenas prácticas de arquitectura y desarrollo limpio.

---

## 🧠 Arquitectura y Diseño

- **MVVM (Model-View-ViewModel)**: Separación clara de responsabilidades.
- **Principios SOLID**: Organización modular y mantenible.
- **Inyección de dependencias con Hilt**: Facilitando la escalabilidad y testeo.
- **Jetpack Compose**: Interfaz de usuario declarativa, moderna y reactiva.
- **State Management con StateFlow**: Reactividad eficiente y limpia.
- **Persistencia con Room**: Almacenamiento local de puntajes altos.
- **Animaciones**: Transiciones suaves con `Animatable` y `tween`.

---

## 🧩 Tecnologías y Herramientas

- **Lenguaje**: Kotlin
- **UI Toolkit**: Jetpack Compose + Material 3
- **Base de datos**: Room
- **DI**: Hilt
- **Manejo de estado**: Kotlin Coroutines + StateFlow
- **Testing**: Pruebas unitarias (`TetrisGameTest.kt`)
- **Gestión de dependencias**: Gradle con `libs.versions.toml` (Catálogo de versiones)
- **Controles de juego**: UI personalizada con íconos, animaciones y estilo arcade

---

## 🚀 Características

- Menú de inicio con animación y estilo arcade 🎮
- Controles de juego táctiles con separación y diseño accesible
- Menú de pausa tipo “hamburguesa” con fondo degradado transparente
- Animaciones en piezas y pantalla de inicio
- Registro de puntaje alto persistente
- Pantalla de Game Over con opción de reinicio

---

## 🛠️ Mejora futura (Ideas)

- Sonidos de fondo
- Soporte para más idiomas (i18n)
- Guardado/restauración de partidas
- Tests instrumentados con Compose UI Test
- CI/CD (GitHub Actions o Firebase)

---

