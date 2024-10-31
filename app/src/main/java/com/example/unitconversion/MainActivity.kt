package com.example.unitconversion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var conversion: String

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
                check_values()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
        output_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                check_values()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        input_field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                check_values()
            }
        })

    }


    // Check which radio button is checked and select units according to that
    private fun check_radio() {
        if (radio_group.checkedRadioButtonId == R.id.length_radio) {
            update_spinner(length_units)
            conversion = "length"
        }
        else if (radio_group.checkedRadioButtonId == R.id.temperature_radio) {
            update_spinner(temperature_units)
            conversion = "temperature"
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


    private fun check_values() {
        val (input_unit, output_unit) = get_spinner_val()
        val input_data = input_field.text.toString().toFloatOrNull()
        var output_data: Float? = 0.0F

        if (conversion == "length") {
            output_data = convert_length(input_data, input_unit, output_unit)
        }
        else if (conversion == "temperature") {
            output_data = convert_temperature(input_data, input_unit, output_unit)
        }
        output_field.text = String.format("%.7f", output_data)
    }


    private fun convert_length(input_data: Float?, input_unit: String?, output_unit: String?): Float? {
        val length_obj = input_data?.let { Length(this@MainActivity, it, input_unit) }
        var output_data = input_data

        if (length_obj != null) {
            output_data = when (output_unit) {
                "km" -> length_obj.to_km()
                "cm" -> length_obj.to_cm()
                "Inch" -> length_obj.to_inches()
                "Feet" -> length_obj.to_feet()
                else -> length_obj.value_meters
            }
        }
        return output_data
    }


    private fun convert_temperature(input_data: Float?, input_unit: String?, output_unit: String?): Float? {
        var output_data = input_data
        if (input_unit == "Centigrade" && output_unit == "Fahrenheit") {
            if (input_data != null) {
                output_data = (input_data * 9/5) + 32
            }
        }
        else if (input_unit == "Fahrenheit" && output_unit == "Centigrade") {
            if (input_data != null) {
                output_data = (input_data - 32) * 5/9
            }
        }
        return output_data
    }
}