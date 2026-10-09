package com.example.vkproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextMessage = findViewById<EditText>(R.id.editTextMessage)
        val buttonOpenSecond = findViewById<Button>(R.id.buttonOpenSecond)
        val buttonCallFriend = findViewById<Button>(R.id.buttonCallFriend)
        val buttonShareText = findViewById<Button>(R.id.buttonShareText)

        buttonOpenSecond.setOnClickListener {
            val textToSend = editTextMessage.text.toString().trim()

            if (textToSend.isEmpty()) {
                editTextMessage.error = "Поле не должно быть пустым"
                Toast.makeText(this, "Введите текст для отправки", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("TEXT_KEY", textToSend)
                startActivity(intent)
            }
        }

        buttonCallFriend.setOnClickListener {
            val inputTarget = editTextMessage.text.toString().trim()

            val phoneRegex = "^\\+?[0-9]{3,15}\$".toRegex()

            if (inputTarget.isEmpty()) {
                editTextMessage.error = "Введите номер телефона"
                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show()
            } else if (!inputTarget.matches(phoneRegex)) {
                editTextMessage.error = "Некорректный формат номера телефона"
                Toast.makeText(this, "Номер должен состоять только из цифр", Toast.LENGTH_LONG).show()
            } else {
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$inputTarget")
                }
                startActivity(callIntent)
            }
        }

        buttonShareText.setOnClickListener {
            val textToShare = editTextMessage.text.toString().trim()

            if (textToShare.isEmpty()) {
                editTextMessage.error = "Нечего отправлять"
                Toast.makeText(this, "Введите текст для шаринга", Toast.LENGTH_SHORT).show()
            } else {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textToShare)
                    type = "text/plain"
                }
                val chooserIntent = Intent.createChooser(sendIntent, "Поделиться текстом через:")
                startActivity(chooserIntent)
            }
        }
    }
}