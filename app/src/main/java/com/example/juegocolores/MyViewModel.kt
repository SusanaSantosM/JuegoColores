package com.example.juegocolores

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyViewModel {

    // etiqueta para el logcat
    private val TAG_LOG = "miDebug"

    // usamos LiveData para que la IU se actualice patron de diseño observer
    val estadoLiveData: MutableLiveData<Estados> = MutableLiveData(Estados.INICIO)

    // este va a ser nuestra lista para la secuencia random usamos mutable, ya que la queremos modificar
    var _numbers = mutableStateOf(0)

    val iluminadoFlow = MutableStateFlow<Int?>(null)

    // inicializamos variables cuando instanciamos
    init {
        // estado inicial
        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoLiveData.value}")
    }

    /**
     * crear entero random
     */
    fun crearRandom() {
        synchronized(this){
            // cambiamos estado, por lo tanto la IU se actualiza
            estadoLiveData.value == Estados.GENERANDO

            // Generar un número aleatorio entre 1 y 4
            _numbers.value = (1..4).random()

            // Añadir el número a la secuencia de juego
            Datos.secuenciaJuego.add(_numbers.value)

            // Emitir el número generado en el flujo
            CoroutineScope(Dispatchers.Main).launch {
                iluminadoFlow.emit(_numbers.value) // Actualizar para iluminar
                delay(1000) // Simular un breve retraso de iluminación
                iluminadoFlow.emit(null) // Apagar la iluminación
            }

            Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoLiveData.value}")
            estadoLiveData.value = Estados.ADIVINANDO
        }
    }

    /**
     *  Reiniciamos las secuencias de colores
     */
    fun reiniciarSecuencia(restart : Boolean){
        Datos.secuenciaJugador.clear()
        if(restart == true){
            Datos.secuenciaJuego.clear()
            Log.d("REINICIAR - Estado", "Secuencia del juego: ${Datos.secuenciaJuego}")
            Datos.rondas = 0
            Datos.puntuacion.numRondas = 0
        } else {
            Log.d("ACTUALIZAR - Estado", "Secuencia del juego: ${Datos.secuenciaJuego}")
            estadoLiveData.value = Estados.ACTUALIZAR
        }
    }

    /**
     * Comprobamos si la secuencia es correcta
     * @return Boolean
     */
    fun comprobarSecuencia(): Boolean {

        val indice = Datos.secuenciaJugador.size - 1

        if (indice >= Datos.secuenciaJuego.size) {
            Log.e("Error", "Índice fuera de rango: $indice")
            return false // No permitir que se compare si el índice no es válido
        }


        var comprobacion = Datos.secuenciaJugador[indice] == Datos.secuenciaJuego[indice]

        Log.d(TAG_LOG, "COMPROBAR - Estado: ${estadoLiveData.value}")
        estadoLiveData.value = Estados.COMPROBAR

        return if (comprobacion) {
            estadoLiveData.value = Estados.INICIO
            Log.d(TAG_LOG, "GANAMOS - Estado: ${estadoLiveData.value}")
            true
        } else {
            estadoLiveData.value = Estados.ADIVINANDO
            Log.d(TAG_LOG, "otro intento - Estado: ${estadoLiveData.value}")
            false
        }
    }

    fun comprobarNumero() : Unit {
        if (Datos.secuenciaJugador.isNotEmpty() && comprobarSecuencia()){
            if (Datos.secuenciaJugador.size == Datos.secuenciaJuego.size){
                Datos.puntuacion.numRondas++
                Datos.secuenciaJugador.clear() // Limpiar secuencia del jugador
                estadoLiveData.value = Estados.INICIO // Cambiar al estado inicial para la siguiente ronda
                Log.d(TAG_LOG, "Rondas: ${Datos.rondas}")
                reiniciarSecuencia(false)
            } else {
                Log.d(TAG_LOG, "Secuencia Jugador: ${Datos.secuenciaJugador}")
                estadoLiveData.value = Estados.ADIVINANDO
            }
        }else {
            Log.d(TAG_LOG, "Secuencia Jugador: ${Datos.secuenciaJugador}")
            estadoLiveData.value = Estados.ACTUALIZAR
            reiniciarSecuencia(true)
        }
    }


}