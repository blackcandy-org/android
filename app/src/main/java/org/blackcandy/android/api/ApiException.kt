package org.blackcandy.android.api

class ApiException(val code: Int?, message: String?) : Exception(message)
