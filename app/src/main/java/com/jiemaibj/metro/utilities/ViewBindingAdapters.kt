package com.jiemaibj.metro.utilities

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("topDrawable")
fun setTopDrawable(view: TextView?, @DrawableRes resId: Int) {
    view?.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0)
}