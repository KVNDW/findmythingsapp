package fhnw.ws6c.findmi.model

import androidx.annotation.DrawableRes
import fhnw.ws6c.R


enum class Screen(val title: String, @DrawableRes val resId: Int) {
    Home     ("Home",R.drawable.favourite),
    Detail("Detail Ansicht",R.drawable.favourite),
    List("Liste",R.drawable.favourite),
    Add("Neues Ding",R.drawable.favourite),
}

