package com.weiyou.attractions.ui

import javax.inject.Inject

class MainRepository @Inject constructor() {
    // 定義數據操作方法
    fun getExampleData(): String {
        return "這是來自 Repository 的數據"
    }
}