package org.setu.medical_appointment_tracker.adapters

import org.setu.medical_appointment_tracker.models.AppointmentModel

interface OnApptListener {
    fun onApptClick(mark: AppointmentModel)

    fun onApptLongClick(mark: AppointmentModel): Boolean
}