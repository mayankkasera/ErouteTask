package com.example.eroutetask.threads

import com.example.eroutetask.util.threads.ThreadExecutor
import io.reactivex.schedulers.Schedulers

class TestingThreadExecutor : ThreadExecutor {
    override fun execute(command: Runnable?) {
        Schedulers.trampoline().scheduleDirect(command!!)
    }
}