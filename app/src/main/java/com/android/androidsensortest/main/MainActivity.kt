package com.android.androidsensortest.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.androidsensortest.R
import com.android.androidsensortest.databinding.ActivityMainBinding
import android.hardware.Sensor
import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var accelerationDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accelerationDataTextView = findViewById(R.id.accelerationData)

        // Inicializar el SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Obtener el sensor de aceleración
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    override fun onResume() {
        super.onResume()
        // Registrar el listener del sensor de aceleración cuando la actividad esté en primer plano
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        // Detener la escucha del sensor cuando la actividad esté en segundo plano
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Obtener datos de aceleración en los ejes X, Y y Z
            val accelerationX = event.values[0]
            val accelerationY = event.values[1]
            val accelerationZ = event.values[2]

            // Actualizar el TextView con los datos de aceleración
            val accelerationText = "Aceleración X: $accelerationX\nAceleración Y: $accelerationY\nAceleración Z: $accelerationZ"
            accelerationDataTextView.text = accelerationText
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario implementar esto en este ejemplo
    }
}

/*import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accelerationDataTextView = findViewById(R.id.accelerationData)


    }


}

*/
