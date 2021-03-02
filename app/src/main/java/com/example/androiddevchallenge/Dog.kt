package com.example.androiddevchallenge

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dog(
    val id: String?,
    val name: String? = "...",
    val age: Int? = (1..5).random(),
    val description: String? = "...",
    val urls: Urls?,
    val likes: Int?,
    val alt_description: String? = "..."
):Parcelable

@Parcelize
data class Urls(val thumb: String?, val full: String?):Parcelable