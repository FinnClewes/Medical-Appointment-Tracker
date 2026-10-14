package org.setu.medical_appointment_tracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditMedicationActivity : AppCompatActivity() {

    private lateinit var medsTitleInput: EditText
    private lateinit var medsDescriptionInput: EditText
    private lateinit var medsDoctorInput: EditText
    private lateinit var amountInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit_medication)

        medsTitleInput = findViewById(R.id.titleText)

        medsDescriptionInput = findViewById(R.id.descriptionText)

        medsDoctorInput = findViewById(R.id.doctorText)

        amountInput = findViewById(R.id.amountInput)

        val saveMedsButton = findViewById<Button>(R.id.saveMedsButton)

        val cancelMedsButton = findViewById<Button>(R.id.cancelMedsButton)

        editingId =
            intent.getLongExtra("id", -1L)
                .takeIf { it != -1L }

        if (editingId != null) {

            findViewById<TextView>(R.id.medsTitle)
                .text = "Edit Medication"

            loadExistingMark(editingId!!)
        }

        saveMedsButton.setOnClickListener {
            saveMark()
        }

        cancelMedsButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark = AppData.placedMeds.findOne(id)

        if (mark == null) {
            Toast.makeText(
                this,
                "Mark not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        medsTitleInput.setText(mark.title)
        medsDescriptionInput.setText(mark.description)
        medsDoctorInput.setText(mark.doctor)
        amountInput.setText(mark.amount.toString())
    }

    private fun saveMark() {

        val title = medsTitleInput.text.toString().trim()
        val description = medsDescriptionInput.text.toString().trim()
        val doctor = medsDoctorInput.text.toString().trim()

        if (title.isEmpty()) {
            medsTitleInput.error = "Title is required"
            return
        }

        val amount = amountInput.text.toString().toFloatOrNull()

        if (amount == null) {
            amountInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {

            val mark = MedicationModel(
                title = title,
                description = description,
                doctor = doctor,
                amount = amount,
            )

            AppData.placedMeds.create(mark)

            Toast.makeText(
                this,
                "Medication created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val mark = MedicationModel(
                id = editingId!!,
                title = title,
                description = description,
                doctor = doctor,
                amount = amount,
            )

            AppData.placedMeds.update(mark)

            Toast.makeText(
                this,
                "Medication updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}