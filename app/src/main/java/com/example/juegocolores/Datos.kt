package com.example.juegocolores

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData

/**
 * Clase que almacena los datos del juego
 */
object Datos{
    val rondaLiveData: MutableLiveData<Int> = MutableLiveData(0)

    var secuenciaJuego = mutableListOf<Int>() // secuencia de colores
    var secuenciaJugador = mutableListOf<Int>() // secuencia de colores del jugador

    var rondas = 0 // numero de rondas

    data class Puntuacion(var numRondas: Int = 0){} // puntuacion
    var puntuacion = Puntuacion()
}

/**
 * Colores utilizados
 * color: Color color normal
 * txt: String nombre del color
 * num: Int numero del color
 * color_suave: Color color suave
 */
enum class Colores(val color: Color, val nombre: String, val num: Int, val color_suave: Color) {
    CLASE_ROJO(color = Color.Red, nombre = "Rojo", num = 1, color_suave = Color(0xF95454)),
    CLASE_VERDE(color = Color.Green, nombre = "Verde", num = 2, color_suave = Color(0x00FF9C)),
    CLASE_AZUL(color = Color.Blue, nombre = "Azul", num = 3, color_suave = Color(0x5B99C2)),
    CLASE_AMARILLO(color = Color.Yellow, nombre = "Amarillo", num = 4, color_suave = Color(0xF6E96B)),
    CLASE_START(color = Color.Gray, nombre = "Start", num = 5, color_suave = Color.LightGray)

}

/**
 * Estados del juego
 * INICIO: estado inicial
 * GENERANDO: generando numero random
 * ADIVINANDO: adivinando el numero
 * COMPROBAR: comprobando si el numero es correcto
 * ACTUALIZAR: actualizando el estado
 * @param start_activo: Boolean si el boton Start esta activo
 * @param boton_activo: Boolean si los botones de colores estan activos
 */
enum class Estados(val start_activo: Boolean, val botonColor_activo: Boolean) {
    INICIO(start_activo = true, botonColor_activo = false),
    GENERANDO(start_activo = false, botonColor_activo = false),
    ADIVINANDO(start_activo = false, botonColor_activo = true),
    COMPROBAR(start_activo = false, botonColor_activo = false),
    ACTUALIZAR(start_activo = false, botonColor_activo = false)
}
