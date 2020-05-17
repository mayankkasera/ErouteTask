package com.example.eroutetask.util.threads

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}