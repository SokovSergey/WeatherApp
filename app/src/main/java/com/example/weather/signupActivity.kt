package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weather.databinding.ActivitySignupBinding

class signupActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupBinding
    lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)

        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.signupButton.setOnClickListener( View.OnClickListener(){

            val userName : String = binding.signupUserName.text.toString()
            val email : String = binding.signupEmail.text.toString()
            val password : String = binding.signupPassword.text.toString()
            val confirmPassword : String = binding.signupConfirm.text.toString()

            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                Toast.makeText(this, "Всё поля обязательны для заполнения", Toast.LENGTH_SHORT).show()
            else
            {
                if(password.equals(confirmPassword))
                {

                    val checkUserName = dbHelper.checkUserName(userName)
                    if(checkUserName)
                    {
                        Toast.makeText(this, "Пользователь с таким именем уже зарегистрирован.",Toast.LENGTH_SHORT ).show()
                    }

                    val checkUserEmail = dbHelper.checkEmail(email)
                    if(!checkUserEmail)
                    {
                        val insert = dbHelper.insertData(email, userName, password)

                        if(insert) {
                            Toast.makeText(this, "Аккаунт зарегистрирован",Toast.LENGTH_SHORT ).show()
                            super.finish()
                            //val intent = Intent(this, LoginActivity::class.java)
                            //startActivity(intent)

                        }
                        else
                        {
                            Toast.makeText(this, "Ошибка регистрации",Toast.LENGTH_SHORT ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Почта уже используется",Toast.LENGTH_SHORT ).show()
                    }
                }
                else {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()

                }
            }

        })

        binding.loginRedirectText.setOnClickListener( View.OnClickListener {
            super.finish()
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent)
        } )


        }


}
