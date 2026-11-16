package org.setu.medical_appointment_tracker.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.medical_appointment_tracker.R
import org.setu.medical_appointment_tracker.models.MedicationModel
import timber.log.Timber

class MedicationAdapter(
    private var marks: List<MedicationModel>,
    private val listener: OnMedsListener
) : RecyclerView.Adapter<MedicationAdapter.MarkViewHolder>() {

    class MarkViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val medsTitleText: TextView =
            itemView.findViewById(R.id.medsTitleText)

        val medsDescriptionText: TextView =
            itemView.findViewById(R.id.medsDescriptionText)

        val medsDoctorText: TextView =
            itemView.findViewById(R.id.medsDoctorText)

        val amountText: TextView =
            itemView.findViewById(R.id.amountText)

        val markImage: ImageView = itemView.findViewById(R.id.markImageMeds)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.meds_placed_mark,
                parent,
                false
            )

        return MarkViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MarkViewHolder,
        position: Int
    ) {

        val mark = marks[position]

        val imageUri = mark.imageUri?.let { Uri.parse(it) }

        holder.medsTitleText.text =
            "${mark.id}: ${mark.title}"

        holder.medsDescriptionText.text =
            mark.description

        holder.medsDoctorText.text =
            mark.doctor

        holder.amountText.text =
            "${mark.amount}mg"

        holder.itemView.setOnClickListener {
            Timber.i("Medication clicked: id=%d, title=%s", mark.id, mark.title)
            listener.onMedsClick(mark)
        }

        holder.itemView.setOnLongClickListener {
            Timber.i("Medication long-clicked: id=%d, title=%s", mark.id, mark.title)
            listener.onMedsLongClick(mark)
        }

        if (imageUri != null) {
            holder.markImage.setImageURI(imageUri)
        } else {
            holder.markImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount(): Int {
        return marks.size
    }

    fun updateMarks(newMarks: List<MedicationModel>) {
        Timber.d("Updating medications: %d -> %d items", marks.size, newMarks.size)
        marks = newMarks
        notifyDataSetChanged()
    }
}