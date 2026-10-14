package org.setu.medical_appointment_tracker

import java.util.concurrent.atomic.AtomicLong

class MedicationMemStore {
    private val medications = ArrayList<MedicationModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<MedicationModel> {
        return medications
    }

    fun create(medication: MedicationModel) {
        medication.id = lastId.incrementAndGet()
        medications.add(medication)
    }

    fun update(medication: MedicationModel): Boolean {
        val foundMedication = findOne(medication.id)
        return if (foundMedication != null) {
            foundMedication.title = medication.title
            foundMedication.description = medication.description
            foundMedication.doctor = medication.doctor
            foundMedication.amount = medication.amount
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundMedication = findOne(id)
        return if (foundMedication != null) {
            medications.remove(foundMedication)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): MedicationModel? {
        return medications.find { p -> p.id == id }
    }
}