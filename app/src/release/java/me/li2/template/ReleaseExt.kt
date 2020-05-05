package me.li2.movies

import me.li2.movies.util.doNothing

import okhttp3.OkHttpClient

fun App.configDebugBuild() {
    doNothing()
}

fun OkHttpClient.Builder.configDebugBuild() = this

fun setupPersistenceBuild() {
    doNothing()
}