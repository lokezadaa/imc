package com.example.pdm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pdm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonResultado.setOnClickListener {
            validarEntrada()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarEntrada() {
        val pesoStr = binding.txtPeso.text.toString().trim()
        val alturaStr = binding.txtAlt.text.toString().trim()

        if (pesoStr.isEmpty() || alturaStr.isEmpty()) {
            binding.txtResultado.text = "Erro! Preencha todos os campos."
            return
        }

        if (!pesoStr.contains(".") || !alturaStr.contains(".")) {
            binding.txtResultado.text = "Erro! Use ponto (.) para casas decimais. Ex: 70.5"
            return
        }

        try {
            val peso = pesoStr.toFloat()
            val altura = alturaStr.toFloat()

            if (peso <= 0 || altura <= 0) {
                binding.txtResultado.text = "Erro! Insira valores válidos."
                return
            }

            calcularIMC(altura, peso)

        } catch (e: NumberFormatException) {
            binding.txtResultado.text = "Erro! Insira valores numéricos corretos."
        }
    }

    private fun calcularIMC(altura: Float, peso: Float) {
        val valores = IMC(peso, altura)
        val imc = valores.calculoIMC()
        val descricao = valores.descricaoIMC()
        binding.txtResultado.text = "IMC: %.2f - %s".format(imc, descricao)
    }

    class IMC(private val peso: Float, private val altura: Float) {
        fun calculoIMC(): Float = peso / (altura * altura)

        fun descricaoIMC(): String {
            val imc = calculoIMC()
            return when {
                imc < 18.5 -> "Abaixo do peso"
                imc < 24.9 -> "Peso normal"
                imc < 29.9 -> "Sobrepeso"
                imc < 34.9 -> "Obesidade grau I"
                imc < 39.9 -> "Obesidade grau II"
                else -> "Obesidade grau III"
            }
        }
    }
}