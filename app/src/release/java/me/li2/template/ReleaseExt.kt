package me.li2.movies

import okhttp3.OkHttpClient

fun App.configDebugBuild() {
    // Do nothing
}

fun OkHttpClient.Builder.configDebugBuild() = this