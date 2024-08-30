package com.weiyou.attractions.data.repository

import javax.inject.Inject

class HomeRepository @Inject constructor() {
    // 定義數據操作方法
    fun getHomeData(): String {
        return "這是來自 HomeRepository 的數據"
    }
}