package com.example.countrieslist.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countrieslist.data.CountryData
import com.example.countrieslist.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias CountryRequest = (suspend () -> List<CountryData>)

@HiltViewModel
class CountriesListViewModel @Inject constructor(
private val repo: CountriesRepo
): ViewModel() {

    private var lastRequest: CountryRequest? = null
    private val mutableUiState = MutableLiveData<UiState<List<CountryListItem>>>(UiState.Loading())
    val uiState: LiveData<UiState<List<CountryListItem>>> by ::mutableUiState

    init {
        requestAllCountries()
    }

    fun requestAllCountries() {
        Log.d("CountriesListViewModel", "requestCountries() called")
        requestCountries { repo.getAllCountries() }
    }

    fun searchByName(name: String) {
        Log.d("CountriesListViewModel","searchByName() called with: name = $name")
        requestCountries { repo.searchByCountryName(name) }
    }

    private fun requestCountries(block: CountryRequest) {
        Log.d("CountriesListViewModel","requestCountries() called")
        lastRequest = block
        mutableUiState.value = UiState.Loading()
        viewModelScope.launch {
            mutableUiState.value = try {
                val data = block().map { CountryListItem(it) }
                UiState.Content(data)
            } catch (e: Throwable) {
                Log.d("requestCountries: can't load data", e.message!!)
                if (e is CancellationException) throw e
                else UiState.Error(e)
            }
        }
    }

}