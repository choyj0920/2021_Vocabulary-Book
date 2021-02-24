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

data class UserData(
    @field:SerializedName("UserUid") private val UserUid: Int
)

class wordbookdata{
    @field:SerializedName("bookid")
    val bookid: Int=0
    @field:SerializedName("Rid")
    val Rid: Int?=null
    @field:SerializedName("Uid")
    val Uid: Int?=null
    @field:SerializedName("bookname")
    val bookname: String=""
}

class wordbooklistResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("booklist")
    val booklist : Array<wordbookdata>? =null
}

data class wordbookiddata(
        @field:SerializedName("bookId") private val bookId: Int?
)

class worddata{
    @field:SerializedName("Wordid")
    val Wordid: Int=0
    @field:SerializedName("word_eng")
    val word_eng: String?=null
    @field:SerializedName("mean")
    val mean: String?=null
}

class wordlistResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("wordlist")
    val wordlist : Array<worddata>? =null
}

interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginData?): Call<LoginResponse?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinData?): Call<JoinResponse?>?

    @POST("/user/wordbook")
    fun findFriend(@Body data: UserData): Call<wordbooklistResponse?>?

    @POST("/user/wordbook/word")
    fun getWordlist(@Body data: wordbookiddata): Call<wordlistResponse?>?

}