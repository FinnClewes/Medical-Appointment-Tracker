package org.setu.medical_appointment_tracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

            onEdit = { mark ->
                editMark(mark)
            },

            onDelete = { mark ->
                deleteMark(mark)
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
    }

    private fun deleteMark(mark: MedicationModel) {

        AppData.placedMeds.delete(mark.id)

        adapter.updateMarks(
            AppData.placedMeds.findAll()
        )
    }
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mark_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.marksTitle)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/
}