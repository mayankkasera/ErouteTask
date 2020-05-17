package com.example.eroutetask.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class ScanViewmodel(var scanHelper: ScanHelper): ViewModel()  {

    private var compositeDisposable = CompositeDisposable()
    var mutableLiveData: MutableLiveData<ScanState> = MutableLiveData()
    var scanState : ScanState = ScanState.ScanSuccess(StringBuilder())
        set(value) {
            field = value
            publishState(value)
        }


    public fun scan(input :StringBuilder){
        compositeDisposable.add(
            scanHelper.scan(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    scanState = ScanState.ScanSuccess(it)
                },{
                    scanState = ScanState.ScanSuccess(StringBuilder(it.message!!))
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun publishState(state: ScanState) {
        mutableLiveData.postValue(state)
    }

}