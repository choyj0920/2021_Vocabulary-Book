package com.example.VocabularyBook

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginData(
    @field:SerializedName("email") private val email: String,
    @field:SerializedName("password") private val password: String
)

class LoginResponse {
    @SerializedName("code")
    var code =0

    @SerializedName("message")
    var message: String? = null

    @SerializedName("Uid")
    var Uid = 0

}

data class UserData(
    @field:SerializedName("useruid") private val useruid: Int
)

data class JoinData(
    @field:SerializedName("username") private val username: String,
    @field:SerializedName("email") private val email: String,
    @field:SerializedName("password") private val password: String,
    @field:SerializedName("picture") private  val picture: String
)

class JoinResponse {
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
}

interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginData?): Call<LoginResponse?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinData?): Call<JoinResponse?>?

    @POST("/user/friends")
    fun findFriend(@Body data: UserData): Call<FriendlistResponse?>?

}