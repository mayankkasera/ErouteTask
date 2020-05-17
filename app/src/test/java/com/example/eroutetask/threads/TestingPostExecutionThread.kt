package com.example.eroutetask.threads

import com.example.eroutetask.util.threads.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestingPostExecutionThread : PostExecutionThread {
    override val scheduler: Scheduler = Schedulers.trampoline()
}