/*
 * Created by Weiyi Li on 2020-05-02.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import java.io.IOException

data class ApiException(val statusCode: Int,
                        val errorCode: Int,
                        val errorMessage: String) : IOException(errorMessage)