package com.example.mathiq

data class Questions(
    val question: String,
    val options: List<String>,
    val correctOption: Int
)