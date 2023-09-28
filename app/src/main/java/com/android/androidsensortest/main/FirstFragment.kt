package com.android.androidsensortest.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.androidsensortest.R
import com.android.androidsensortest.databinding.FragmentFirstBinding
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.TextView

class FirstFragment : Fragment(), AccelerometerListener, GyroscopeListener {

    private var accelerometerManager: AccelerometerManager? = null
    private var gyroscopeManager: GyroscopeManager? = null
    private var _binding: FragmentFirstBinding? = null
    private var sensorActive = false
    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null
    private val binding get() = _binding!!
    private lateinit var accelerationDataTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Inicializar el TextView
        accelerationDataTextView = binding.textviewAcelData
        // Inicializar los managers y configurar oyentes
        accelerometerManager = AccelerometerManager(requireContext())
        accelerometerManager?.registerListener(this)

        gyroscopeManager = GyroscopeManager(requireContext())
        gyroscopeManager?.registerListener(this)


        binding.buttonNextFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.startStopButton.setOnClickListener {
            if (sensorActive) {
                // Si el sensor está activo, desactívalo y cambia el texto del botón
                sensorActive = false
                binding.startStopButton.text = "Iniciar"
            } else {
                // Si el sensor no está activo, actívalo y cambia el texto del botón
                sensorActive = true
                binding.startStopButton.text = "Detener"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometerManager?.registerListener(this)
    }

    override fun onPause() {
        accelerometerManager?.unregisterListener()
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    override fun handleAcceleration(accelerationX: Float, accelerationY: Float, accelerationZ: Float) {
        // Formatear los valores con un máximo de 4 decimales
        val formattedAccelerationX = String.format("%.4f", accelerationX)
        val formattedAccelerationY = String.format("%.4f", accelerationY)
        val formattedAccelerationZ = String.format("%.4f", accelerationZ)

        // Actualizar la interfaz de usuario con los datos formateados
        val accelerationText =
            "Acel X: $formattedAccelerationX\nAcel Y: $formattedAccelerationY\nAcel Z: $formattedAccelerationZ"

        if (sensorActive){
            binding.textviewAcelData.text = accelerationText
            if (accelerationX < -1 || accelerationX > 1 || accelerationY < -1 || accelerationY > 1) {
                binding.mensajeHorizontal.text = "No horizontal"
                binding.mensajeMovimiento.text = "En Movimento"
                binding.root.setBackgroundColor(Color.RED)
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (vibrator?.hasVibrator() == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //val vibrationEffect =
                            //VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE)
                        //vibrator.vibrate(vibrationEffect)
                    } else {
                        // Para versiones anteriores a Android Oreo
                        vibrator.vibrate(100)
                    }
                }

                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(
                        requireContext(),
                        R.raw.mouse_click
                    ) // Reproduce un archivo de sonido
                }
                mediaPlayer?.start()

            } else {
                binding.mensajeHorizontal.text = "Horizontal"
                binding.mensajeMovimiento.text = "Quieto"
                binding.root.setBackgroundColor(Color.GREEN)
                // Detener la vibración si es necesario
                vibrator?.cancel()
                // Detener la reproducción si es necesario
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }

    override fun onRotationChanged(rotationX: Float, rotationY: Float, rotationZ: Float) {
        if (sensorActive){
            val giroData = "Gy X: $rotationX \nGy Y: $rotationY \nGz : $rotationZ"
            binding.textviewGirosData.text = giroData
            if (rotationX < -2 || rotationX > 2 || rotationY < -2 || rotationY > 2){
                binding.mensajeMovimiento.text = "En Movimento"
                binding.root.setBackgroundColor(Color.RED)
            }
            else{
                binding.mensajeMovimiento.text = "Quieto"
                binding.root.setBackgroundColor(Color.GREEN)
            }
        }


        // Por ejemplo, puedes imprimir los valores de rotación para depurar:
        Log.d("Gyroscope", "RotationX: $rotationX, RotationY: $rotationY, RotationZ: $rotationZ")

        // Luego, puedes implementar la lógica para detectar la orientación y el movimiento aquí.
        // Puedes combinar los datos del acelerómetro y el giroscopio para lograrlo.
    }

    override fun onDestroyView() {
        // Desregistrar los oyentes al salir del fragmento
        accelerometerManager?.unregisterListener()
        gyroscopeManager?.unregisterListener()
        super.onDestroyView()
        _binding = null
    }

}