package me.li2.movies.data.remote

import java.io.IOException

data class ApiException(val statusCode: Int, val errorMessage: String) : IOException(errorMessage)