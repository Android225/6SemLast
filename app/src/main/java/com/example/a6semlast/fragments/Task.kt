package com.example.a6semlast

data class Task(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var priority: Int = 0,
    var completed: Boolean = false
)
