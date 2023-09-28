package com.android.androidsensortest.main

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

interface GyroscopeListener {
    fun onRotationChanged(rotationX: Float, rotationY: Float, rotationZ: Float)
}

class GyroscopeManager(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var listener: GyroscopeListener? = null

    fun registerListener(listener: GyroscopeListener) {
        this.listener = listener
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterListener() {
        listener = null
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesitas implementar esto para el giroscopio
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor == gyroscopeSensor) {
                // Obtener los valores de rotación en los ejes X, Y y Z
                val rotationX = it.values[0]
                val rotationY = it.values[1]
                val rotationZ = it.values[2]

                // Llamar al callback para notificar los cambios
                listener?.onRotationChanged(rotationX, rotationY, rotationZ)
            }
        }
    }
}
