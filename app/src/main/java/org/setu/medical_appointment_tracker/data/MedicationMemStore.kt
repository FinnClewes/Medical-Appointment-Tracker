package org.setu.medical_appointment_tracker.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.setu.medical_appointment_tracker.models.MedicationModel
import java.io.File
import java.util.concurrent.atomic.AtomicLong

class MedicationMemStore(private val medsFile: File) {
    private val medications = ArrayList<MedicationModel>()
    private val lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    private fun load() {
        if (!medsFile.exists()) return

        val type = object : TypeToken<ArrayList<MedicationModel>>() {}.type

        val loaded: ArrayList<MedicationModel> =
            gson.fromJson(medsFile.readText(), type) ?: arrayListOf()

        medications.clear()
        medications.addAll(loaded)

        lastId.set(medications.maxOfOrNull { it.id } ?: 0L)
    }

    private fun save() {
        medsFile.parentFile?.mkdirs()
        medsFile.writeText(gson.toJson(medications))
    }

    fun findAll(): List<MedicationModel> {
        return medications
    }

    fun create(medication: MedicationModel) {
        medication.validate()
        medication.id = lastId.incrementAndGet()
        medications.add(medication)
        save()
    }

    fun update(medication: MedicationModel): Boolean {
        medication.validate()
        val foundMedication = findOne(medication.id)
        return if (foundMedication != null) {
            foundMedication.title = medication.title
            foundMedication.description = medication.description
            foundMedication.doctor = medication.doctor
            foundMedication.amount = medication.amount
            foundMedication.imageUri = medication.imageUri
            save()
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundMedication = findOne(id)
        return if (foundMedication != null) {
            medications.remove(foundMedication)
            save()
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): MedicationModel? {
        return medications.find { p -> p.id == id }
    }
}