package com.example.abschlussprojektandroide.data.dataclass.model.api

data class Quote(
    val quote: String,
    val length: String,
    val author: String,
    val tags: List<String>,
    val category: String,
    val date: String,
    val title: String,
    val background: String,
    val id: String
)
