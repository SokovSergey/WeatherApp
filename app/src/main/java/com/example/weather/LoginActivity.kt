package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weather.databinding.ActivityLoginBinding
import java.io.File
import java.io.FileOutputStream

class LoginActivity : AppCompatActivity() {


    lateinit var binding : ActivityLoginBinding
    lateinit var  dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val username = binding.loginUserName.text.toString()
            val password = binding.loginPassword.text.toString()

            if(username.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(dbHelper.checkUserPassword(username, password))
                {
                    Toast.makeText(this, "Вход выполенен" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)

                    val settings = "$username"
                    val path : File = this.applicationContext.filesDir
                    val writer : FileOutputStream = FileOutputStream(File(path,"settings.txt"))
                    writer.write(settings.toByteArray())

                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Не удалось выполнить вход" , Toast.LENGTH_SHORT).show()
                }

            }
        })

        binding.signupRedirectText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        })

    }
}