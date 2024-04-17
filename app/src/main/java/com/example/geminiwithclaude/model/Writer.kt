package com.example.geminiwithclaude.model

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentId



private const val TITLE_MAX_SIZE = 30

data class Writer(
    @DocumentId val id: String = "",
    val title:String = "",
    val inputtext: String = "",
    val outputtext: String = "",
    val userId: String = ""
    //data binding problem. I need a TextView type

)



/*fun Writer.getTitle(): String {
    val isLongText = this.text.length > TITLE_MAX_SIZE
    val endRange = if (isLongText) TITLE_MAX_SIZE else this.text.length - 1
    return this.text.substring(IntRange(0, endRange))
}*/