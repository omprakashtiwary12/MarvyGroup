package com.zap.marvygroup.util

import io.reactivex.Scheduler

interface RxSchedulerProvider {
  val main: Scheduler
  val io: Scheduler
}