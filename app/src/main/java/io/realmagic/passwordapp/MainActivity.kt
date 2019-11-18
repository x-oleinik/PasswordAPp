package io.realmagic.passwordapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val MIN_LENGTH = 8
    val magic = PasswordMagic(resources.getStringArray(R.array.russians), resources.getStringArray(R.array.english))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        copy_button.isEnabled = false
        copy_button2.isEnabled = false

        copy_button.setOnClickListener {
            val clipboardManager : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Password", result_text.text.toString()))
        }

        copy_button2.setOnClickListener {
            val clipboardManager : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Password", generated_password.text.toString()))
        }

        generate_button.setOnClickListener {
            generated_password.text = magic.generatePass(
                MIN_LENGTH + seek_bar.progress,
                capitals.isChecked,
                digits.isChecked,
                specials.isChecked)
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                updateCount(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        input_password.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!=null){
                    copy_button.isEnabled = p0.isNotEmpty()
                    result_text.text = (magic.convert(p0))
                    updateStrength()
                }
            }
        })
    }

    fun updateCount(progress : Int){
        val total = MIN_LENGTH + progress
        val added : String = resources.getQuantityString(R.plurals.symbols_count, progress)
        val result : String = resources.getQuantityString(R.plurals.symbols_count, total)
        pass_length.text = getString(R.string.length_format, progress, added, total, result)
    }

    fun updateStrength(){
        val password : String = input_password.text.toString()
        val strength : Int = magic.checkStrength(password)
        pass_strength_tv.text = resources.getStringArray(R.array.strengths)[strength]
        pass_strength_iv.drawable.level = strength * 1000
    }
}
