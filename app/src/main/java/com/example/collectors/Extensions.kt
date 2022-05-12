package com.example.collectors

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun EditText.textToFlow(): Flow<CharSequence?>{
    return callbackFlow {
        val listener = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) = Unit
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                offer(text)
            }
        }
        addTextChangedListener(listener)
        awaitClose{
            removeTextChangedListener(listener)
        }
    }
}