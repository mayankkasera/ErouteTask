package com.example.eroutetask.ui.scan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.eroutetask.threads.TestingPostExecutionThread
import com.example.eroutetask.threads.TestingThreadExecutor
import com.example.eroutetask.util.threads.PostExecutionThread
import com.example.eroutetask.util.threads.ThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import java.util.concurrent.Callable


@RunWith(JUnit4::class)
class ScanViewModelTest {

    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var scanHelper: ScanHelper

    @Mock
    private lateinit var observer: Observer<ScanState>


    lateinit var scanViewmodel : ScanViewmodel

    private lateinit var threadExecutor: ThreadExecutor

    private lateinit var postExecutionThread: PostExecutionThread




    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        threadExecutor = TestingThreadExecutor()
        postExecutionThread = TestingPostExecutionThread()
        scanViewmodel = ScanViewmodel(postExecutionThread,threadExecutor,scanHelper)
        scanViewmodel?.mutableLiveData?.observeForever(observer)
    }

    @Test
    fun testScanSeccess(){
        val input = StringBuffer("Your Transaction Id is MW782333.")
        val result = StringBuffer("MW782333")

        `when`(scanHelper.scan(input))
            .thenReturn(Single.just(result))

        scanViewmodel.scan(input)

        Mockito.verify(observer).onChanged(ScanState.ScanSuccess(result))
    }

    @Test
    fun testScanNoData(){
        val input = StringBuffer("Your Transaction Id is 782333.")
        val result = StringBuffer("")

        `when`(scanHelper.scan(input))
            .thenReturn(Single.just(result))


        scanViewmodel.scan(input)

        Mockito.verify(observer).onChanged(ScanState.NoData)
    }

}