package org.setu.medical_appointment_tracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.medical_appointment_tracker.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainTitle)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewMarksButton =
            findViewById<Button>(R.id.viewMarksButton)

        val addMarkButton =
            findViewById<Button>(R.id.addMarkButton)

        val viewMedsButton =
            findViewById<Button>(R.id.viewMedsButton)

        val addMedsButton =
            findViewById<Button>(R.id.addMedsButton)

        viewMarksButton.setOnClickListener {
            Timber.i("View Appointments clicked, opening MarkListActivity")
            startActivity(
                Intent(this, MarkListActivity::class.java)
            )
        }

        addMedsButton.setOnClickListener {
            Timber.i("Add Meds clicked, opening AddEditMedication")
            startActivity(
                Intent(this, AddEditMedicationActivity::class.java)
            )
        }

        viewMedsButton.setOnClickListener {
            Timber.i("View Meds clicked, opening MedicationListActivity")
            startActivity(
                Intent(this, MedicationListActivity::class.java)
            )
        }

        addMarkButton.setOnClickListener {
            Timber.i("Add Appointment clicked, opening AddEditActivity")
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }

    }
}