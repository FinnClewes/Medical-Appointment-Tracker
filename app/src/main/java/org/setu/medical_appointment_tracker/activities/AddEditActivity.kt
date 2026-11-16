package org.setu.medical_appointment_tracker.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.medical_appointment_tracker.data.AppData
import org.setu.medical_appointment_tracker.models.AppointmentModel
import org.setu.medical_appointment_tracker.R
import timber.log.Timber
import java.util.Calendar
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var doctorInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText
    private var editingId: Long? = null
    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                    // some providers don't allow persistable access
                }
                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput = findViewById(R.id.titleInput)

        descriptionInput = findViewById(R.id.descriptionInput)

        doctorInput = findViewById(R.id.doctorInput)

        dateInput = findViewById(R.id.dateInput)

        xInput = findViewById(R.id.xInput)

        yInput = findViewById(R.id.yInput)

        imagePreview = findViewById(R.id.imageViewAdd)

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

        findViewById<Button>(R.id.imagePickButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }

        saveButton.setOnClickListener {
            Timber.i("Save button clicked")
            saveMark()
        }

        cancelButton.setOnClickListener {
            Timber.i("Cancel button clicked")
            finish()
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark = AppData.placedMarks.findOne(id)

        if (mark == null) {
            Timber.e("No appointment found with id=%d", id)
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

        selectedImageUri = mark.imageUri?.let { Uri.parse(it) }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }
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
            Timber.w("Save failed: title is empty")
            titleInput.error = "Title is required"
            return
        }

        if (date.isEmpty()) {
            Timber.w("Save failed: date is empty")
            dateInput.error = "Date is required"
            return
        }

        val x = xInput.text.toString().toFloatOrNull()

        if (x == null) {
            Timber.w("Save failed: invalid X value '%s'", xInput.text)
            xInput.error = "Enter a valid number"
            return
        }

        val y = yInput.text.toString().toFloatOrNull()

        if (y == null) {
            Timber.w("Save failed: invalid Y value '%s'", yInput.text)
            yInput.error = "Enter a valid number"
            return
        }

        try {
            if (editingId == null) {
                val mark = AppointmentModel(
                    title = title,
                    description = description,
                    doctor = doctor,
                    date = date,
                    x = x,
                    y = y,
                    imageUri = selectedImageUri?.toString()
                )
                AppData.placedMarks.create(mark)
                Timber.i("Appointment created: id=%d, title=%s", mark.id, mark.title)
                Toast.makeText(this, "Appointment created", Toast.LENGTH_SHORT).show()
            } else {
                val mark = AppointmentModel(
                    id = editingId!!,
                    title = title,
                    description = description,
                    doctor = doctor,
                    date = date,
                    x = x,
                    y = y,
                    imageUri = selectedImageUri?.toString()
                )
                AppData.placedMarks.update(mark)
                Timber.i("Appointment updated: id=%d, title=%s", mark.id, mark.title)
                Toast.makeText(this, "Appointment updated", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IllegalArgumentException) {
            Timber.w("Save rejected: %s", e.message)
            Toast.makeText(this, e.message ?: "Please check the entered values.", Toast.LENGTH_LONG).show()
            xInput.error = "Must be a decimal between -90.0 and 90.0"
            yInput.error = "Must be a decimal between -180.0 and 180.0"
            return
        }

        finish()
    }
}