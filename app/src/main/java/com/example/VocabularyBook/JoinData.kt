package com.example.VocabularyBook

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


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
class NormalResponse{
    @SerializedName("code")
    var code =0

    @SerializedName("message")
    var message: String? = null
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

data class getcheckwordinputdata(
        @field:SerializedName("Uid") private val Uid: Int?,
        @field:SerializedName("bookId") private val bookId: Int?
)
class checkworddata{
    @field:SerializedName("Wordid")
    val Wordid: Int=0
}
class checkwordResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("memoword")
    val memoword : Array<checkworddata>? =null
}

data class checkwordinputdata(
        @field:SerializedName("Uid") private val Uid: Int?,
        @field:SerializedName("Wordid") private val Wordid: Int?,
        @field:SerializedName("bookId") private val bookId: Int?

)


interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginData?): Call<LoginResponse?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinData?): Call<JoinResponse?>?

    @POST("/user/wordbook")
    fun findFriend(@Body data: UserData): Call<wordbooklistResponse?>?

    @POST("/user/wordbook/word")
    fun getWordlist(@Body data: wordbookiddata): Call<wordlistResponse?>?

    @POST("/user/wordbook/getcheckword")
    fun getCheckword(@Body data: getcheckwordinputdata): Call<checkwordResponse?>?

    @POST("/user/wordbook/checkword")
    fun Checkword(@Body data: checkwordinputdata): Call<NormalResponse?>?

    @POST("/user/wordbook/uncheckword")
    fun Uncheckword(@Body data: checkwordinputdata): Call<NormalResponse?>?

}
class BoxPoint{
    @field:SerializedName("0")
    val boxpoint0: Int=0
    @field:SerializedName("1")
    val boxpoint1: Int=0
}

class ImgtoTextdata{
    @field:SerializedName("boxes")
    val boxes: ArrayList<BoxPoint>?=null
    @field:SerializedName("recognition_words")
    val recognition_words: ArrayList<String>?=null
}

class ImgtoTextResponse{
    @SerializedName("result")
    val result:ArrayList<ImgtoTextdata>?=null
}

interface ServiceKakaoApi {
    @Multipart
    @POST("v2/vision/text/ocr")
    fun ImagetoText(
        @PartMap()  partMap:LinkedHashMap<String, RequestBody>,
        @Part name: List<MultipartBody.Part>
            ): Call<responseimgtotxt?>

}



data class Result(
    val boxes: ArrayList<ArrayList<Int>>,
    val recognition_words: ArrayList<String>
)
class responseimgtotxt(
    val result: ArrayList<Result>
)

class box(val y:Int,val x:Int,val text:String):Comparable<box>{

    override fun compareTo(other: box): Int {
        if (this.y>other.y){
            return 1
        }
        else if(this.y==other.y){
            if(this.x>other.y){
                return 1
            }
        }
        return -1
    }
}


class TranslateResponse {
    @SerializedName("message")
    val message : Translatemessage? =null

}
class Translatemessage{

    @SerializedName("type")
    val type : String? =null
    @SerializedName("service")
    val service: String? = null
    @SerializedName("version")
    val version: String? = null
    @SerializedName("result")
    val result:Messageresult? =null

}

class Messageresult{
    @SerializedName("srcLangType")
    val srcLangType : String? =null
    @SerializedName("tarLangType")
    val tarLangType: String? = null
    @SerializedName("translatedText")
    val translatedText: String? = null
}

interface ServicePapagoApi{
    @FormUrlEncoded
    @POST("v1/papago/n2mt")
    fun transferPapago(

        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<TranslateResponse?>
}