package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var newOperator = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View) {
        if(newOperator) {
            binding.tvMathEquation.text = ""
        }
        newOperator = false;
        if(view is Button)
        {
            binding.tvMathEquation.append(view.text)
        }
    }

    fun operationAction(view: View) {
        if(view is Button)
        {
            binding.tvMathEquation.append(view.text)

        }
    }

    fun clearAll(view: View) {
        binding.tvMathEquation.text = "0"
        binding.tvResult.text = ""
        newOperator = true;
    }

    fun backSpace(view: View) {
        val length = binding.tvMathEquation.length();
        if(length > 0) {
            val subSeq = binding.tvMathEquation.text.subSequence(0, length - 1)

            if(subSeq.length > 0) {
                binding.tvMathEquation.text = subSeq
            } else {
                binding.tvMathEquation.text = "0"
                newOperator = true;
            }
        }
    }

    fun equalsAction(view: View) {
        binding.tvResult.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val multiDivPowCalc = multiDivPowCalculator(digitsOperators)
        if(multiDivPowCalc.isEmpty()) return ""

        val result = addAndSubstractionCalculator(multiDivPowCalc)
        return result.toString()
    }

    private fun multiDivPowCalculator(passedDigitsList: MutableList<Any>): MutableList<Any> {
        var list = passedDigitsList
        while (list.contains('^') || list.contains('×') || list.contains('÷'))
        {
            list = multiDivPowCalc(list)
        }
        return list
    }

    private fun multiDivPowCalc(passedDigitsList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var size = passedDigitsList.size

        for(indice in passedDigitsList.indices)
        {
            // By saying "is Char" we check if it's an operator or not
            if(passedDigitsList[indice] is Char && indice != passedDigitsList.lastIndex && indice < size)
            {
                val operator = passedDigitsList[indice]
                val prevDigit = passedDigitsList[indice - 1] as Float
                val nextDigit = passedDigitsList[indice + 1] as Float

                when(operator)
                {
                    '^' ->
                    {
                        val result = Math.pow(prevDigit.toDouble(), nextDigit.toDouble())
                        newList.add(result.toFloat())
                        size = indice + 1
                    }
                    '×' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        size = indice + 1
                    }
                    '÷' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        size = indice + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(indice > size) {
                newList.add(passedDigitsList[indice])
            }
        }

        return newList
    }

    private fun addAndSubstractionCalculator(passedDigitsList: MutableList<Any>): Float {

        var result = passedDigitsList[0] as Float

        for(i in passedDigitsList.indices)
        {
            if(passedDigitsList[i] is Char && i != passedDigitsList.lastIndex)
            {
                val operator = passedDigitsList[i]
                val nextDigit = passedDigitsList[i + 1] as Float

                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '-') {
                    result -= nextDigit
                }
            }
        }

        return result
    }


    // Function to break up the equation into an array with floats and operators
    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.tvMathEquation.text)
        {
            if(character.isDigit() || character == '.') {
                currentDigit += character
            } else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "") {
            list.add(currentDigit.toFloat())
        }

        return list
    }
}