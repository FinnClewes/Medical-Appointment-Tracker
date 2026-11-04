package org.setu.medical_appointment_tracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.medical_appointment_tracker.data.AppData
import org.setu.medical_appointment_tracker.models.AppointmentModel
import org.setu.medical_appointment_tracker.adapters.PlacedMarkAdapter
import org.setu.medical_appointment_tracker.R
import org.setu.medical_appointment_tracker.adapters.OnApptListener
import timber.log.Timber

class MarkListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlacedMarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mark_list)

        recyclerView =
            findViewById(R.id.marksRecyclerView)

        adapter = PlacedMarkAdapter(
            marks = AppData.placedMarks.findAll(),

            listener = object : OnApptListener {

                override fun onApptClick(mark: AppointmentModel) {
                    editMark(mark)
                }

                override fun onApptLongClick(mark: AppointmentModel): Boolean {
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
                AppData.placedMarks.findAll()
            )
        }
    }

    private fun editMark(mark: AppointmentModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", mark.id)

        startActivity(intent)

        Timber.d("Editing appointment id=${mark.id}, title=${mark.title}")
    }

    private fun deleteMark(mark: AppointmentModel) {

        AppData.placedMarks.delete(mark.id)

        adapter.updateMarks(
            AppData.placedMarks.findAll()
        )

        Timber.d("Deleting appointment id=${mark.id}, title=${mark.title}")
    }
}