package com.example.instabuginternshiptask.domain.use_case

import com.example.instabuginternshiptask.data.services.AppStatus

class CheckDeviceStatus(
    private var status: AppStatus
) {
    operator fun invoke(): Boolean = status.isOnline

}