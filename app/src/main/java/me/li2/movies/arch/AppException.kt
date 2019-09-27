package me.li2.movies.arch

data class AppException(val exceptIn: Throwable?) : Exception()