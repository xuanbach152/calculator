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

    private lateinit var textExpression: TextView
    private lateinit var textResult: TextView
    private var currentExpression = ""
    private var lastOperator = ""
    private var lastNumber = 0.0
    private var isNewOperation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calc_layout)

        textExpression = findViewById(R.id.textExpression)
        textResult = findViewById(R.id.textResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onDigitClick(it as Button) }
        }

        findViewById<Button>(R.id.btnCE).setOnClickListener { clearEntry() }
        findViewById<Button>(R.id.btnC).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnBS).setOnClickListener { backspace() }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { onOperatorClick("×") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { onOperatorClick("÷") }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.buttonDecimal).setOnClickListener { addDecimalPoint() }
    }

    private fun onDigitClick(button: Button) {
        if (isNewOperation) {
            currentExpression = ""
            isNewOperation = false
        }
        currentExpression += button.text
        textExpression.text = currentExpression
    }

    private fun onOperatorClick(operator: String) {
        if (currentExpression.isNotEmpty() && !isOperator(currentExpression.last())) {
            currentExpression += " $operator "
            textExpression.text = currentExpression
        }
    }

    private fun calculateResult() {
        if (currentExpression.isNotEmpty()) {
            try {
                val result = evaluateExpression(currentExpression)
                val formattedResult = if (result % 1 == 0.0) {
                    result.toInt().toString()
                } else {
                    String.format("%.2f", result)
                }
                textResult.text = formattedResult
                isNewOperation = true
            } catch (e: Exception) {
                textResult.text = "Lỗi"
            }
        }
    }

    private fun clearEntry() {
        currentExpression = ""
        textExpression.text = ""
        textResult.text = "0"
    }

    private fun clearAll() {
        currentExpression = ""
        lastOperator = ""
        lastNumber = 0.0
        textExpression.text = ""
        textResult.text = "0"
    }

    private fun backspace() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
            textExpression.text = currentExpression
        }
    }

    private fun addDecimalPoint() {
        if (currentExpression.isEmpty() || isOperator(currentExpression.last())) {
            currentExpression += "0."
        } else if (!currentExpression.split(" ").last().contains(".")) {
            currentExpression += "."
        }
        textExpression.text = currentExpression
    }

    private fun isOperator(char: Char): Boolean {
        return char == '+' || char == '-' || char == '×' || char == '÷'
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ")
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> numbers.add(token.toDouble())
                token.length == 1 -> operators.add(token[0])
            }
        }

        while (operators.isNotEmpty()) {
            val opIndex = operators.indexOfFirst { it == '×' || it == '÷' }
            val index = if (opIndex != -1) opIndex else 0

            val num1 = numbers[index]
            val num2 = numbers[index + 1]
            val operator = operators[index]

            val result = when (operator) {
                '×' -> num1 * num2
                '÷' -> if (num2 != 0.0) num1 / num2 else Double.NaN
                '+' -> num1 + num2
                '-' -> num1 - num2
                else -> throw IllegalArgumentException("Unknown operator")
            }

            numbers[index] = result
            numbers.removeAt(index + 1)
            operators.removeAt(index)
        }

        return numbers.first()
    }
}





