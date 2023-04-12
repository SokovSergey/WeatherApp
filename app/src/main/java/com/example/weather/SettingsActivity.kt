package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityFirstLoginBinding
import com.example.weather.databinding.ActivitySettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class SettingsActivity: AppCompatActivity() {

    lateinit var dbHelper : DatabaseHelper
    lateinit var settingsBinding : ActivitySettingsBinding

    private val KEY_ID = "_id"
    private val KEY_FIO = "FIO"
    private val KEY_GENDER = "gender"
    private val KEY_AGE = "age"
    private val KEY_SLEVEL = "symptomsLevel"
    private val KEY_OVERALLSLEVEL = "overallSensitivityLevel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        dbHelper = DatabaseHelper(this.baseContext)

        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(settingsBinding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val menu : Menu = bottomNavigation.menu
        val menuItem : MenuItem = menu.getItem(1)
        menuItem.setChecked(true)

        bottomNavigation?.setOnItemSelectedListener {
            // do stuff
            when(it.itemId)
            {
                R.id.ic_home -> {
                    super.finish()
                }
            }
            return@setOnItemSelectedListener true
        }

        updateSettingsView()

    }

    fun getCurrentUsername() : String {
        val path: File = this.applicationContext.filesDir
        val readFrom: File = File(path, "settings.txt")
        val stream: FileInputStream = FileInputStream(readFrom)

        var inputStreamReader : InputStreamReader = InputStreamReader(stream)
        val bufferedStreamReader : BufferedReader = BufferedReader(inputStreamReader)

        var text : String? = null
        val stringBuilder = StringBuilder()


        while ({text = bufferedStreamReader.readLine(); text}() != null)
        {
            stringBuilder.append(text)
        }

        var result : String = ""
        try{
            result = stringBuilder.toString()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return result
    }

    fun updateSettingsView() : Boolean
    {
        val currentUserName = getCurrentUsername()

        val currentUserParams = dbHelper.getUserPersonalParameters(currentUserName)

        if(currentUserParams.isEmpty())
        {
            return false
        }

        for(key in currentUserParams.keys)
        {
            when(key)
            {
                KEY_FIO ->
                {
                    settingsBinding.FIOTextEdit.text = currentUserParams.getValue(key).toString()
                }

                KEY_GENDER ->
                {
                    settingsBinding.genderTextEdit.text = currentUserParams.getValue(key).toString()
                }

                KEY_AGE ->
                {
                    settingsBinding.ageTextEdit.text = currentUserParams.getValue(key).toString()
                }
                KEY_SLEVEL ->
                {
                    settingsBinding.ratingBar.text = currentUserParams.getValue(key).toString()
                }
                KEY_OVERALLSLEVEL ->
                {
                    settingsBinding.geomagnitismEditText.setText(currentUserParams.getValue(key)).toString()
                }

            }
        }

        return true
    }

}
