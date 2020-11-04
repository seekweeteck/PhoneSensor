package my.edu.tarc.phonesensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.TimeZoneFormat
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


//Make this activity listen to sensor data
class MainActivity : AppCompatActivity(), SensorEventListener {
    //Declare global variables
    //1: Sensor Manager
    private lateinit var sensorManager: SensorManager
    //2: Light sensor
    private var light: Sensor? = null

    private lateinit var textViewLight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Link UI to program
        textViewLight = findViewById(R.id.textViewLight)

        val myCalendar = Calendar.getInstance(Locale.getDefault())
        val textViewCurrentDateTime: TextView = findViewById(R.id.textViewCurrentDateTime)
        textViewCurrentDateTime.text = "${myCalendar.get(Calendar.DATE).toString()}-${myCalendar.get(Calendar.MONTH).plus(1).toString()}-${myCalendar.get(Calendar.YEAR).toString()}"

        //Create an instance of the light sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val buttonSaveData : Button = findViewById(R.id.buttonSaveData)

        buttonSaveData.setOnClickListener {
            // Write a message to the database
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("sensor")

            myRef.child("date").setValue(textViewCurrentDateTime.text.toString())
            myRef.child("light").setValue(textViewLight.text.toString())
            Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSensorChanged(p0: SensorEvent) {
        val lightData = p0.values[0]
        textViewLight.text = lightData.toString()

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}//end of class