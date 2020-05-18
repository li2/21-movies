/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.data.model

data class CreditListUI(
        val casts: List<CastUI>,
        val crews: List<CrewUI>,
        val movieId: Int)

interface CreditUI {
    val characterOrJob: String
    val creditId: String
    val gender: Int?
    val id: Int
    val name: String
    val profileUrl: String?
}

data class CastUI(
        val castId: Int,
        override val characterOrJob: String,
        override val creditId: String,
        override val gender: Int?,
        override val id: Int,
        override val name: String,
        val order: Int,
        override val profileUrl: String?): CreditUI

data class CrewUI(
        override val characterOrJob: String,
        override val creditId: String,
        val department: String,
        override val gender: Int?,
        override val id: Int,
        override val name: String,
        override val profileUrl: String?): CreditUI

/*
data class CastUI(
        val castId: Int,
        val character: String,
        val creditId: String,
        val gender: Int?,
        val id: Int,
        val name: String,
        val order: Int,
        val profileUrl: String?)

data class CrewUI(
        val creditId: String,
        val department: String,
        val gender: Int?,
        val id: Int,
        val job: String,
        val name: String,
        val profileUrl: String?)*/
