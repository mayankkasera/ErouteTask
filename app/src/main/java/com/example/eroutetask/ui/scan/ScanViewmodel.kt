package com.example.eroutetask.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eroutetask.util.threads.PostExecutionThread
import com.example.eroutetask.util.threads.ThreadExecutor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class ScanViewmodel(
    val postExecutionThread: PostExecutionThread,
    val threadExecutor: ThreadExecutor,
    var scanHelper: ScanHelper
): ViewModel()  {

    private var compositeDisposable = CompositeDisposable()
    var mutableLiveData: MutableLiveData<ScanState> = MutableLiveData()
    var scanState : ScanState = ScanState.NoData
        set(value) {
            field = value
            publishState(value)
        }


    fun scan(input :StringBuffer){
        compositeDisposable.add(
            scanHelper.scan(input)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
                .subscribe({
                    if(it.length>0){
                        scanState = ScanState.ScanSuccess(it)
                    }
                    else{
                        scanState = ScanState.NoData
                    }
                },{
                    scanState = ScanState.ScanSuccess(StringBuffer(it.message!!))
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