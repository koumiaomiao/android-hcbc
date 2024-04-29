package com.kmj.hcbc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String?,
    var title: String?,
    var author: String?,
    var publicationYear: String?,
    var isbn: String?
): Parcelable
