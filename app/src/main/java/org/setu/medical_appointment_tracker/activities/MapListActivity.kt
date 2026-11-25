package org.setu.medical_appointment_tracker.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.setu.medical_appointment_tracker.R
import org.setu.medical_appointment_tracker.data.AppData
import org.setu.medical_appointment_tracker.models.AppointmentModel
import org.osmdroid.config.Configuration

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.CopyrightOverlay
import org.setu.medical_appointment_tracker.adapters.OnApptListener
import org.setu.medical_appointment_tracker.adapters.PlacedMarkAdapter
import timber.log.Timber

class MapListActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var imgPlace: ImageView

    private lateinit var editMapButton: Button
    private lateinit var deleteMapButton: Button
    private var selectedPlacemark: AppointmentModel? = null
    private lateinit var adapter: PlacedMarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = "PlacemarkLab-SETU-FinnClewes (student project)"
        setContentView(R.layout.activity_map_list)

        // Initialise views
        mapView = findViewById(R.id.mapList)
        txtTitle = findViewById(R.id.mapTitleText)
        txtDescription = findViewById(R.id.mapDescText)
        imgPlace = findViewById(R.id.mapImageView)

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

        val cancelMapButton = findViewById<Button>(R.id.cancelMapButton)
        editMapButton = findViewById(R.id.editMapButton)
        deleteMapButton = findViewById(R.id.deleteMapButton)

        editMapButton.isEnabled = false
        deleteMapButton.isEnabled = false

        cancelMapButton.setOnClickListener {
            Timber.i("Cancel button clicked")
            finish()
        }
        editMapButton.setOnClickListener {
            Timber.i("Edit button clicked")
            selectedPlacemark?.let { editMark(it) }
        }
        deleteMapButton.setOnClickListener {
            Timber.i("Delete button clicked")
            selectedPlacemark?.let { deleteMark(it) }
        }

        // Configure OpenStreetMap
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Show map attribution
        mapView.overlays.add(CopyrightOverlay(this))
    }

    private fun loadMarkers() {

        val placemarks = AppData.placedMarks.findAll()

        placemarks.forEach { placemark ->

            val position = GeoPoint(
                placemark.x.toDouble(),
                placemark.y.toDouble()
            )

            val marker = Marker(mapView)

            marker.position = position
            marker.title = placemark.title
            marker.snippet = placemark.description

            marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            // Handle marker selection
            marker.setOnMarkerClickListener { clickedMarker, _ ->

                displayPlace(placemark)

                // Return true to consume the click
                true
            }

            mapView.overlays.add(marker)
        }

        // Empty list: tell the user and stop
        if (placemarks.isEmpty()) {
            txtTitle.text = "No appointments to show"
            txtDescription.text = "Add an appointment with a location to see it here."
            mapView.invalidate()
            return
        }

        // Zoom to fit every marker
        if (placemarks.size == 1) {
            val only = placemarks.first()
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(GeoPoint(only.x.toDouble(), only.y.toDouble()))
        } else {
            val points = placemarks.map { GeoPoint(it.x.toDouble(), it.y.toDouble()) }
            val box = BoundingBox.fromGeoPoints(points)
            // post{} waits until the map has a size, otherwise the zoom is ignored
            mapView.post { mapView.zoomToBoundingBox(box, true, 100) }
        }

        mapView.invalidate()
    }

    private fun displayPlace(placemark: AppointmentModel) {

        selectedPlacemark = placemark
        editMapButton.isEnabled = true
        deleteMapButton.isEnabled = true

        txtTitle.text = placemark.title

        txtDescription.text = placemark.description

        if (placemark.imageUri != null) {
            imgPlace.setImageURI(
                Uri.parse(placemark.imageUri)
            )
        } else {
            imgPlace.setImageDrawable(null)
        }
    }

    private fun editMark(mark: AppointmentModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", mark.id)

        startActivity(intent)

        Timber.d("Editing appointment id=${mark.id}, title=${mark.title}")
    }

    private fun reloadMarkers() {
        mapView.overlays.clear()
        mapView.overlays.add(CopyrightOverlay(this))
        selectedPlacemark = null
        editMapButton.isEnabled = false
        deleteMapButton.isEnabled = false
        txtTitle.text = "Select a Placemark"
        txtDescription.text = ""
        imgPlace.setImageDrawable(null)
        loadMarkers()
    }

    private fun deleteMark(mark: AppointmentModel) {
        AppData.placedMarks.delete(mark.id)
        reloadMarkers()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        reloadMarkers()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        mapView.onDetach()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.tileProvider.clearTileCache()
        mapView.invalidate()
    }
}
