package com.example.eroutetask.util.threads

import io.reactivex.schedulers.Schedulers

class SchedulersThread : ThreadExecutor {

    override fun execute(command: Runnable) {
        Schedulers.newThread().scheduleDirect(command)
    }
}