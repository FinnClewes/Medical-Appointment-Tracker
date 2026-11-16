package org.setu.medical_appointment_tracker.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.medical_appointment_tracker.data.AppData
import org.setu.medical_appointment_tracker.models.MedicationModel
import org.setu.medical_appointment_tracker.R
import timber.log.Timber
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class AddEditMedicationActivity : AppCompatActivity() {

    private lateinit var medsTitleInput: EditText
    private lateinit var medsDescriptionInput: EditText
    private lateinit var medsDoctorInput: EditText
    private lateinit var amountInput: EditText
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

        setContentView(R.layout.activity_add_edit_medication)

        medsTitleInput = findViewById(R.id.titleText)

        medsDescriptionInput = findViewById(R.id.descriptionText)

        medsDoctorInput = findViewById(R.id.doctorText)

        amountInput = findViewById(R.id.amountInput)

        imagePreview = findViewById(R.id.imageViewAddMeds)

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

        findViewById<Button>(R.id.imagePickButtonMeds).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }

        saveMedsButton.setOnClickListener {
            Timber.i("Save button clicked")
            saveMark()
        }

        cancelMedsButton.setOnClickListener {
            Timber.i("Cancel button clicked")
            finish()
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark = AppData.placedMeds.findOne(id)

        if (mark == null) {
            Timber.e("No mark found with id=%d", id)
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
        selectedImageUri = mark.imageUri?.let { Uri.parse(it) }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    private fun saveMark() {

        val title = medsTitleInput.text.toString().trim()
        val description = medsDescriptionInput.text.toString().trim()
        val doctor = medsDoctorInput.text.toString().trim()

        if (title.isEmpty()) {
            Timber.w("Save failed: title is empty")
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
                imageUri = selectedImageUri?.toString()
            )

            AppData.placedMeds.create(mark)

            Timber.i("Mark created: title=%s", mark.title)

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
                imageUri = selectedImageUri?.toString()
            )

            AppData.placedMeds.update(mark)

            Timber.i("Medication updated: title=%s", mark.title)

            Toast.makeText(
                this,
                "Medication updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}