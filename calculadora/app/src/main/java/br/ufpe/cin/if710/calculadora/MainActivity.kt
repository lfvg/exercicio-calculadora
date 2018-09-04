package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : Activity() {
    //variavel que armazena a expressao
    var exp = ""
    //funcao para atualizar a expressao e o que eh exibido na view 'text_calc'
    fun updateExp(n: String){
        exp += n
        text_calc.setText(exp)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todos os botoes que nao sao '=' ou 'C' chamam a funcao updateExp passando
        //cada um a sua respectiva modificacao
         btn_0.setOnClickListener({
            updateExp("0")
        })
        btn_1.setOnClickListener({
            updateExp("1")
        })
        btn_2.setOnClickListener({
            updateExp("2")
        })
        btn_3.setOnClickListener({
            updateExp("3")
        })
        btn_4.setOnClickListener({
            updateExp("4")
        })
        btn_5.setOnClickListener({
            updateExp("5")
        })
        btn_6.setOnClickListener({
            updateExp("6")
        })
        btn_7.setOnClickListener({
            updateExp("7")
        })
        btn_8.setOnClickListener({
            updateExp("8")
        })
        btn_9.setOnClickListener({
            updateExp("9")
        })
        btn_Multiply.setOnClickListener({
            updateExp("*")
        })
        btn_Subtract.setOnClickListener({
            updateExp("-")
        })
        btn_Dot.setOnClickListener({
            updateExp(".")
        })
        btn_Add.setOnClickListener({
            updateExp("+")
        })
        btn_LParen.setOnClickListener({
            updateExp("(")
        })
        btn_RParen.setOnClickListener({
            updateExp(")")
        })
        btn_Power.setOnClickListener({
            updateExp("^")
        })
        btn_Divide.setOnClickListener({
            updateExp("/")
        })
        //o botao de clear limpa a variavel exp e o texto nas views 'text_info' e 'text_calc'
        btn_Clear.setOnClickListener({
            exp = ""
            text_info.text = ""
            text_calc.setText("")
        })
        //o botao de igual da um 'try' na funcao 'eval' passando a variavel 'exp' como parametro
        //e pega um 'RuntimeException' que eh jogado por 'eval' se a expressao estiver mal formatada
        //jogano um toast com a mensagem do erro
        btn_Equal.setOnClickListener({
            try {
                var resultado = eval(exp)
                text_info.text = resultado.toString()
            }catch (e: RuntimeException){
                toast(e.message!!)
            }

        })


    }
    //override da funcao 'onSaveInstanceState' para armazenar os valores da variavel 'exp' e da view
    // 'text_info'
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("INFO", text_info.text.toString())
        outState?.putString("EXP", exp)
        super.onSaveInstanceState(outState)
    }
    //override da funcao 'onRestoreInstanceState' para restaurar os valores da variavel 'exp' e das
    //views 'text_calc' e 'text_info'
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
