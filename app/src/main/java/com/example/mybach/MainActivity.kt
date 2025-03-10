package com.example.mybach

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

        private lateinit var display: TextView
        private var currentInput = ""
        private var lastOperator = ""
        private var lastNumber = 0.0
        private var isNewOperation = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.calc_layout)

            display = findViewById(R.id.textView) // Thay ID đúng của TextView

            // Lấy tất cả button
            val buttons = listOf(
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
            )

            for (id in buttons) {
                findViewById<Button>(id).setOnClickListener { onDigitClick(it as Button) }
            }

            // Gán sự kiện cho các nút đặc biệt
            findViewById<Button>(R.id.btnCE).setOnClickListener { clearEntry() }
            findViewById<Button>(R.id.btnC).setOnClickListener { clearAll() }
            findViewById<Button>(R.id.btnBS).setOnClickListener { backspace() }

            findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperatorClick("+") }
            findViewById<Button>(R.id.btnSub).setOnClickListener { onOperatorClick("-") }
            findViewById<Button>(R.id.btnMul).setOnClickListener { onOperatorClick("*") }
            findViewById<Button>(R.id.btnDiv).setOnClickListener { onOperatorClick("/") }
            findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
            findViewById<Button>(R.id.buttonDecimal).setOnClickListener { addDecimalPoint() }

        }

        private fun onDigitClick(button: Button) {
            if (isNewOperation) {
                currentInput = ""
                isNewOperation = false
            }
            currentInput += button.text
            display.text = currentInput
        }

        private fun onOperatorClick(operator: String) {
            if (currentInput.isNotEmpty()) {
                lastNumber = currentInput.toDouble()
                lastOperator = operator
                isNewOperation = true
            }
        }

    private fun calculateResult() {
        if (currentInput.isNotEmpty() && lastOperator.isNotEmpty()) {
            val secondNumber = currentInput.toDouble()
            val result = when (lastOperator) {
                "+" -> lastNumber + secondNumber
                "-" -> lastNumber - secondNumber
                "*" -> lastNumber * secondNumber
                "/" -> if (secondNumber != 0.0) lastNumber / secondNumber else Double.NaN
                else -> Double.NaN
            }

            // Hiển thị số nguyên nếu không có phần thập phân, nếu không thì làm tròn 1 chữ số
            val formattedResult = if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                String.format("%.2f", result)
            }

            display.text = formattedResult
            currentInput = formattedResult
            lastOperator = ""
            isNewOperation = true
        }
    }


    private fun clearEntry() {
            currentInput = ""
            display.text = "0"
        }

        private fun clearAll() {
            currentInput = ""
            lastOperator = ""
            lastNumber = 0.0
            display.text = "0"
        }

        private fun backspace() {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                display.text = if (currentInput.isEmpty()) "0" else currentInput
            }
        }
    private fun addDecimalPoint() {
        if (!currentInput.contains(".")) { // Kiểm tra đã có dấu chấm chưa
            currentInput = if (currentInput.isEmpty()) "0." else currentInput + "."
            display.text = currentInput
        }
    }

}




