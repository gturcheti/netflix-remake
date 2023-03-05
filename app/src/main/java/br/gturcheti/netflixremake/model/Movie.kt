package br.gturcheti.netflixremake.model

data class Movie (
    val id: String,
    val coverUrl: String,
    val title: String = "",
    val description: String = "",
    val cast: String = "",
    )