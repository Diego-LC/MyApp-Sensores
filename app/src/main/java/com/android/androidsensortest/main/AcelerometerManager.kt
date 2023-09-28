package com.android.androidsensortest.main

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.android.androidsensortest.databinding.ActivityMainBinding

class AccelerometerManager(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var listener: AccelerometerListener? = null

    init {
    }

    fun registerListener(listener: AccelerometerListener) {
        this.listener = listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        listener = null
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val accelerationX = event.values[0]
            val accelerationY = event.values[1]
            val accelerationZ = event.values[2]

            listener?.handleAcceleration(accelerationX, accelerationY, accelerationZ)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario implementar esto en este ejemplo
    }
}

interface AccelerometerListener {

    fun handleAcceleration(accelerationX: Float, accelerationY: Float, accelerationZ: Float)
}
