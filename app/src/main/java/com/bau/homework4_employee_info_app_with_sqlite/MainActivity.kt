package com.bau.homework4_employee_info_app_with_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var editTextID: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextSalary: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button
    private lateinit var textViewEmployees: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextID = findViewById(R.id.editTextId)
        editTextName = findViewById(R.id.editTextName)
        editTextSalary = findViewById(R.id.editTextSalary)
        btnAdd = findViewById(R.id.btnAdd)
        btnRemove = findViewById(R.id.btnRemove)
        textViewEmployees = findViewById(R.id.textViewEmployees)

        dbHelper = DatabaseHelper(this)

        btnAdd.setOnClickListener {
            val id = editTextID.text.toString().toIntOrNull()
            val name = editTextName.text.toString().trim()
            val salary = editTextSalary.text.toString().toDoubleOrNull()
            if(id != null && name.isNotEmpty() && salary != null){
                val success = dbHelper.addEmployee(id, name, salary)
                if (success) {
                    Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()
                    displayEmployees()
                }else {
                    Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Please enter valid name and salary", Toast.LENGTH_SHORT).show()
            }
        }

        btnRemove.setOnClickListener {
            val id = editTextID.text.toString().toIntOrNull()
            if(id != null){
                val success = dbHelper.removeEmployee(id)
                if(success) {
                    Toast.makeText(this, "Employee removed successfully", Toast.LENGTH_SHORT).show()
                    displayEmployees()
                }else {
                    Toast.makeText(this, "Failed to remove employee", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Please enter valid employee ID", Toast.LENGTH_SHORT).show()
            }
        }
        displayEmployees()
    }

    private fun displayEmployees() {
        val cursor = dbHelper.getAllEmployees()
        val stringBuilder = StringBuilder()
        cursor?.use {
            val idIndex = it.getColumnIndex(DatabaseHelper.Companion.COLUMN_ID)
            val nameIndex = it.getColumnIndex(DatabaseHelper.Companion.COLUMN_NAME)
            val salaryIndex = it.getColumnIndex(DatabaseHelper.Companion.COLUMN_SALARY)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val name = it.getString(nameIndex)
                val salary = it.getDouble(salaryIndex)

                stringBuilder.append("ID: $id, Name: $name, Salary: $salary\n")
            }
        }
        textViewEmployees.text = stringBuilder.toString()
        cursor?.close()
        setEditTextsToDefault()
    }

    private fun setEditTextsToDefault() {
        editTextID.setText("")
        editTextName.setText("")
        editTextSalary.setText("")
    }
}