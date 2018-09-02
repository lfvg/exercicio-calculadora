package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    var exp = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_0.setOnClickListener({
            exp += "0"
            text_calc.setText(exp)
        })
        btn_1.setOnClickListener({
            exp += "1"
            text_calc.setText(exp)
        })
        btn_2.setOnClickListener({
            exp += "2"
            text_calc.setText(exp)
        })
        btn_3.setOnClickListener({
            exp += "3"
            text_calc.setText(exp)
        })
        btn_4.setOnClickListener({
            exp += "4"
            text_calc.setText(exp)
        })
        btn_5.setOnClickListener({
            exp += "5"
            text_calc.setText(exp)
        })
        btn_6.setOnClickListener({
            exp += "6"
            text_calc.setText(exp)
        })
        btn_7.setOnClickListener({
            exp += "7"
            text_calc.setText(exp)
        })
        btn_8.setOnClickListener({
            exp += "8"
            text_calc.setText(exp)
        })
        btn_9.setOnClickListener({
            exp += "9"
            text_calc.setText(exp)
        })
        btn_Multiply.setOnClickListener({
            exp += "*"
            text_calc.setText(exp)
        })
        btn_Subtract.setOnClickListener({
            exp += "-"
            text_calc.setText(exp)
        })
        btn_Dot.setOnClickListener({
            exp += "."
            text_calc.setText(exp)
        })
        btn_Add.setOnClickListener({
            exp += "+"
            text_calc.setText(exp)
        })
        btn_LParen.setOnClickListener({
            exp += "("
            text_calc.setText(exp)
        })
        btn_RParen.setOnClickListener({
            exp += ")"
            text_calc.setText(exp)
        })
        btn_Power.setOnClickListener({
            exp += "^"
            text_calc.setText(exp)
        })
        btn_Divide.setOnClickListener({
            exp += "/"
            text_calc.setText(exp)
        })
        btn_Clear.setOnClickListener({
            exp = ""
            text_info.text = ""
            text_calc.setText("")
        })
        btn_Equal.setOnClickListener({
            try {
                var resultado = eval(exp)
                text_info.text = resultado.toString()
            }catch (e: RuntimeException){
                //inserir codigo para jogar um toast
            }

        })


    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("INFO", text_info.text.toString())
        outState?.putString("EXP", exp)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        exp = savedInstanceState?.getString("EXP")!!
        text_calc.setText(exp)
        text_info.text = savedInstanceState.getString("INFO")

        super.onRestoreInstanceState(savedInstanceState)
    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
