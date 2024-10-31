package com.example.unitconversion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // Define units in array of string
    private val length_units = arrayOf("m", "km", "cm", "Inch", "Feet")
    private val temperature_units = arrayOf("Centigrade", "Fahrenheit")
    private lateinit var input_spinner: Spinner
    private lateinit var output_spinner: Spinner
    private lateinit var input_field: EditText
    private lateinit var output_field: TextView
    private lateinit var radio_group: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get all widgets
        input_spinner = findViewById(R.id.input_spinner)
        output_spinner = findViewById(R.id.output_spinner)
        radio_group = findViewById(R.id.radio_group)
        input_field  = findViewById(R.id.input)
        output_field = findViewById(R.id.output)

        // Check which radio is initially selected and update spinner
        check_radio()
        radio_group.setOnCheckedChangeListener {_, checkedId->check_radio()}
        input_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val (value1, value2) = get_spinner_val()
                Toast.makeText(this@MainActivity, "Spinner 1: $value1, Spinner 2: $value2", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        })

    }


    // Check which radio button is checked and select units according to that
    private fun check_radio() {
        if (radio_group.checkedRadioButtonId == R.id.length_radio) {
            update_spinner(length_units)
        }
        else if (radio_group.checkedRadioButtonId == R.id.temperature_radio) {
            update_spinner(temperature_units)
        }
    }


    // Update spinner data
    private fun update_spinner(units: Array<String>) {
        val adapter_input = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter_input.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        input_spinner.adapter = adapter_input
        output_spinner.adapter = adapter_input
    }


    // Get value from both spinners
    private fun get_spinner_val(): Pair<String?, String?> {
        val val1 = input_spinner.selectedItem as? String
        val val2 = output_spinner.selectedItem as? String
        return Pair(val1, val2)
    }
}