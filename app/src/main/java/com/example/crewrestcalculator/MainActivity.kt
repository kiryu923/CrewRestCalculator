package com.example.crewrestcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var totalFlightTimeEditText: EditText
    private lateinit var restSlotCountSpinner: Spinner
    private lateinit var resultTextView: TextView
    private lateinit var calculateButton: Button
    private lateinit var manualToggle: Switch
    private val timeSlots = mutableListOf<Pair<EditText, EditText>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalFlightTimeEditText = findViewById(R.id.totalFlightTimeEditText)
        restSlotCountSpinner = findViewById(R.id.restSlotCountSpinner)
        resultTextView = findViewById(R.id.resultTextView)
        calculateButton = findViewById(R.id.calculateButton)
        manualToggle = findViewById(R.id.manualToggle)

        val slotCountAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            (1..10).map { "$it slots" }
        )
        restSlotCountSpinner.adapter = slotCountAdapter

        manualToggle.setOnCheckedChangeListener { _, isChecked ->
            for ((start, end) in timeSlots) {
                start.isEnabled = isChecked
                end.isEnabled = isChecked
            }
        }

        calculateButton.setOnClickListener {
            val totalFlightTime = totalFlightTimeEditText.text.toString()
            val slots = restSlotCountSpinner.selectedItemPosition + 1
            val useManual = manualToggle.isChecked

            if (!useManual) {
                val result = autoDistributeRest(totalFlightTime, slots)
                resultTextView.text = result
            } else {
                val result = manualRestResult()
                resultTextView.text = result
            }
        }

        setupManualSlotInputs()
    }

    private fun autoDistributeRest(flightTime: String, slots: Int): String {
        return "Auto rest calculated: $flightTime / $slots"
    }

    private fun manualRestResult(): String {
        val slotTimes = timeSlots.map { (start, end) ->
            "${start.text} ~ ${end.text}"
        }
        return "Manual rest:\n" + slotTimes.joinToString("\n")
    }

    private fun setupManualSlotInputs() {
        val container = findViewById<LinearLayout>(R.id.slotInputContainer)
        for (i in 1..10) {
            val row = layoutInflater.inflate(R.layout.slot_row, container, false)
            val startEditText = row.findViewById<EditText>(R.id.startTimeEditText)
            val endEditText = row.findViewById<EditText>(R.id.endTimeEditText)
            container.addView(row)
            timeSlots.add(Pair(startEditText, endEditText))
        }
    }
}
