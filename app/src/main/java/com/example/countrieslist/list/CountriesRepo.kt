package com.example.countrieslist.list

import com.example.countrieslist.data.CountryData

interface CountriesRepo {
    suspend fun getAllCountries(): List<CountryData>

    suspend fun searchByCountryName(name: String): List<CountryData>
}