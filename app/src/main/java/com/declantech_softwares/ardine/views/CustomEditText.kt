package com.declantech_softwares.ardine.views

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

class CustomEditText : EditText {
    constructor(cntx : Context) : super(cntx)
    constructor(cntx : Context, attributeSet: AttributeSet) : super(cntx, attributeSet)
    constructor(cntx: Context, attributeSet: AttributeSet, defStyleAttr : Int) : super(cntx, attributeSet, defStyleAttr)
    constructor(cntx: Context, attributeSet: AttributeSet, defStyleAttr : Int, defStyleRes: Int)
            : super(cntx, attributeSet, defStyleAttr, defStyleRes)

    init {
        TODO("Set Typeface")
    }
}