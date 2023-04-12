package com.example.weather

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.weather.databinding.ActivityLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.android.material.navigation.NavigationBarView
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var dbHelper : DatabaseHelper

    var dialog : Dialog? = null

    private lateinit var currentUserName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this.baseContext)

        currentUserName = getCurrentUsername()

        if (!dbHelper.userHasPersonalParameters(currentUserName)) {
            showFirstSettingsDialog()

            //val intent = Intent(this, FirstLoginActivity::class.java)
            //startActivity(intent)
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menu : Menu = bottomNavigation.menu

        bottomNavigation?.setOnItemSelectedListener {
            // do stuff
           when(it.itemId)
           {
               R.id.ic_home -> {
                   val menuItem : MenuItem = menu.getItem(0)
                   menuItem.setChecked(true)
               }
               R.id.ic_settings -> {
                   val intent = Intent(this, SettingsActivity::class.java)
                   startActivity(intent)
               }
           }
        return@setOnItemSelectedListener false
        }
    }
    fun showFirstSettingsDialog()
    {
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.activity_first_login)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var spinner : Spinner = dialog!!.findViewById<Spinner>(R.id.genderSpinner)
        var adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        fun Boolean.toInt() = if (this) 1 else 0

        var saveButton : Button? = null
        saveButton = dialog!!.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener( View.OnClickListener {
            try{
                val second_name  = dialog!!.findViewById<EditText>(R.id.second_name).text.toString()
                val first_name  = dialog!!.findViewById<EditText>(R.id.first_name).text.toString()
                val family_name  = dialog!!.findViewById<EditText>(R.id.family_name_editText).text.toString()



                val age = dialog!!.findViewById<EditText>(R.id.ageTextEdit).text.toString()

                var ageInt : Int
                try {
                    ageInt = age.toInt()
                }catch (e : Exception)
                {
                    ageInt = -1
                }

                val gender = spinner.selectedItem.toString()

                val numStarts : Int = dialog!!.findViewById<RatingBar>(R.id.ratingBar).numStars

                val ch1 = dialog!!.findViewById<CheckBox>(R.id.firstCB).isChecked.toInt()
                val ch2 = dialog!!.findViewById<CheckBox>(R.id.secondCB).isChecked.toInt()
                val ch3 = dialog!!.findViewById<CheckBox>(R.id.thirdCB).isChecked.toInt()

                val summ = ch1 + ch2 + ch3

                if(save(second_name, first_name, family_name, ageInt, gender, numStarts, summ))
                {
                    dialog!!.hide()
                }
                else
                {
                    Toast.makeText(this, "Не все поля заполнены" , Toast.LENGTH_SHORT).show()

                }
            }
            catch (e : Exception)
            {
                println(e.stackTraceToString())
            }

        })

        dialog!!.show()
    }


    fun save(s_name : String, first_name : String, family_name : String, age : Int , gender : String, num_stars : Int, summaryVal : Int) : Boolean {


        var failed : Boolean = false

        if (s_name.isEmpty()) {
            dialog!!.findViewById<EditText>(R.id.second_name).setBackgroundColor(Color.RED)
            failed = true
        }
        else
            dialog!!.findViewById<EditText>(R.id.second_name).setBackgroundColor(Color.WHITE)

        if (first_name.isEmpty()) {
            dialog!!.findViewById<EditText>(R.id.first_name).setBackgroundColor(Color.RED)
            failed = true
        }
        else
            dialog!!.findViewById<EditText>(R.id.first_name).setBackgroundColor(Color.WHITE)

        if (family_name.isEmpty()) {
            dialog!!.findViewById<EditText>(R.id.family_name_editText).setBackgroundColor(Color.RED)
            failed = true
        }
        else
            dialog!!.findViewById<EditText>(R.id.family_name_editText).setBackgroundColor(Color.WHITE)

        if(age < 0 || age > 100)
        {
            dialog!!.findViewById<EditText>(R.id.ageTextEdit).setBackgroundColor(Color.RED)
            failed = true
        }
        else
            dialog!!.findViewById<EditText>(R.id.ageTextEdit).setBackgroundColor(Color.WHITE)


        if (gender.isEmpty())
        {
            failed = true
        }

        if(num_stars < 0 || num_stars > 5)
        {
            failed = true
        }

        if(summaryVal < 0 || summaryVal > 3)
        {
            failed = true
        }

        if(failed)
            return false

        if(!dbHelper.savePersonalParameters(currentUserName, s_name, first_name, family_name, age, gender, num_stars, summaryVal))
            return false

        return true

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

    }


