package com.example.weather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityFirstLoginBinding
import com.example.weather.databinding.ActivityLoginBinding
import com.example.weather.databinding.ActivitySignupBinding

class FirstLoginActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) , AdapterView.OnItemSelectedListener{


    lateinit var binding : ActivityFirstLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        binding = ActivityFirstLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var text : String = parent?.getItemAtPosition(position).toString();
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}