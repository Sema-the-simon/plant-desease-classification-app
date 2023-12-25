package com.example.plantdisease.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.convertToDateFormat(): String =
    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this)