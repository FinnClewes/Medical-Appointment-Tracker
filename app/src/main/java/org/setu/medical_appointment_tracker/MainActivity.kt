package org.setu.medical_appointment_tracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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
            startActivity(
                Intent(this, MarkListActivity::class.java)
            )
        }

        addMedsButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditMedicationActivity::class.java)
            )
        }

        viewMedsButton.setOnClickListener {
            startActivity(
                Intent(this, MedicationListActivity::class.java)
            )
        }

        addMarkButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }

    }
}