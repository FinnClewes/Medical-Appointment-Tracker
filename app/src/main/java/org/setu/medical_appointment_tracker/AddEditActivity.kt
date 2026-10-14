package org.setu.medical_appointment_tracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import kotlin.toString

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var doctorInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput = findViewById(R.id.titleInput)

        descriptionInput = findViewById(R.id.descriptionInput)

        doctorInput = findViewById(R.id.doctorInput)

        dateInput = findViewById(R.id.dateInput)

        xInput = findViewById(R.id.xInput)

        yInput = findViewById(R.id.yInput)

        val saveButton = findViewById<Button>(R.id.saveButton)

        val cancelButton = findViewById<Button>(R.id.cancelButton)

        editingId =
            intent.getLongExtra("id", -1L)
                .takeIf { it != -1L }

        if (editingId != null) {

            findViewById<TextView>(R.id.formTitle)
                .text = "Edit Mark"

            loadExistingMark(editingId!!)
        }

        dateInput.setOnClickListener { showDatePicker() }

        saveButton.setOnClickListener {
            saveMark()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark = AppData.placedMarks.findOne(id)

        if (mark == null) {
            Toast.makeText(
                this,
                "Mark not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        titleInput.setText(mark.title)
        descriptionInput.setText(mark.description)
        doctorInput.setText(mark.doctor)
        dateInput.setText(mark.date)
        xInput.setText(mark.x.toString())
        yInput.setText(mark.y.toString())
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                dateInput.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveMark() {

        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val doctor = doctorInput.text.toString().trim()
        val date = dateInput.text.toString().trim()

        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val x = xInput.text.toString().toFloatOrNull()

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        val y = yInput.text.toString().toFloatOrNull()

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {

            val mark = AppointmentModel(
                title = title,
                description = description,
                doctor = doctor,
                date = date,
                x = x,
                y = y
            )

            AppData.placedMarks.create(mark)

            Toast.makeText(
                this,
                "Appointment created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val mark = AppointmentModel(
                id = editingId!!,
                title = title,
                description = description,
                doctor = doctor,
                date = date,
                x = x,
                y = y
            )

            AppData.placedMarks.update(mark)

            Toast.makeText(
                this,
                "Appointment updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}