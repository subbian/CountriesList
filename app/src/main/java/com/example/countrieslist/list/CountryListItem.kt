package com.example.countrieslist.list

import com.example.countrieslist.data.CountryData

data class CountryListItem(
    val countryData:CountryData
) {
    val pngFlag: String? get() = countryData.pngFlag
    val name: String get() = (countryData.commonName ?: countryData.officialName).orEmpty()
    val capital: String get() = countryData.capitals.joinToString(separator = ",")
    val population: Long get() = countryData.population ?: 0
}