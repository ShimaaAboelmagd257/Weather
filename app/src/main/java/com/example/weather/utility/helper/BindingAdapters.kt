package com.example.weather.utility.helper

object BindingAdapters {

   /* *//*@JvmStatic
    @BindingAdapter("onItemSelected")
    fun setOnItemSelectedListener(autoCompleteTextView: AutoCompleteTextView, viewModel: SettingsViewModel) {
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedLocation = parent?.adapter?.getItem(position).toString()
            viewModel.onItemSelected(selectedLocation)
        }
    }*//*
    @JvmStatic
    @BindingAdapter("onItemSelected")
    fun setOnItemSelectedListener(autoCompleteTextView: AutoCompleteTextView, listener: AdapterView.OnItemSelectedListener) {
        autoCompleteTextView.onItemSelectedListener = listener
    }
    @JvmStatic
    @BindingAdapter("app:location")
    fun setLocation(autoCompleteTextView: AutoCompleteTextView, location: String?) {
        autoCompleteTextView.setText(location)
    }*/
}