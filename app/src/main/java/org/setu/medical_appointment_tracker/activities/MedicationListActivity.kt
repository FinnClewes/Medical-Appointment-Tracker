package org.setu.medical_appointment_tracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.medical_appointment_tracker.data.AppData
import org.setu.medical_appointment_tracker.adapters.MedicationAdapter
import org.setu.medical_appointment_tracker.models.MedicationModel
import org.setu.medical_appointment_tracker.R
import org.setu.medical_appointment_tracker.adapters.OnMedsListener
import timber.log.Timber

class MedicationListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mark_list)

        findViewById<TextView>(R.id.marksTitle).text = "Medication List"

        recyclerView =
            findViewById(R.id.marksRecyclerView)

        adapter = MedicationAdapter(
            marks = AppData.placedMeds.findAll(),

            listener = object : OnMedsListener {
                override fun onMedsClick(mark: MedicationModel) {
                    editMark(mark)
                }

                override fun onMedsLongClick(mark: MedicationModel): Boolean {
                    deleteMark(mark)
                    return true
                }
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {

            adapter.updateMarks(
                AppData.placedMeds.findAll()
            )
        }
    }

    private fun editMark(mark: MedicationModel) {

        val intent =
            Intent(this, AddEditMedicationActivity::class.java)

        intent.putExtra("id", mark.id)

        startActivity(intent)

        Timber.d("Editing medication id=${mark.id}, title=${mark.title}")
    }

    private fun deleteMark(mark: MedicationModel) {

        AppData.placedMeds.delete(mark.id)

        adapter.updateMarks(
            AppData.placedMeds.findAll()
        )

        Timber.d("Deleting medication id=${mark.id}, title=${mark.title}")
    }
}