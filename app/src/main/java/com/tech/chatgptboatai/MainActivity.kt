package com.tech.chatgptboatai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.tech.chatgptboatai.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.setAnimation(R.raw.robot)
        lottieAnimationView.playAnimation()

        binding.generateImage.setOnClickListener {
            startActivity(Intent(this,ImageActivity::class.java))
        }

        binding.chatWithBoat.setOnClickListener {
            startActivity(Intent(this,ChatActivity::class.java))
        }
    }
}