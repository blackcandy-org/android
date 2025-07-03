package org.blackcandy.shared.api

class ApiException(val code: Int?, message: String?) : Exception(message)
