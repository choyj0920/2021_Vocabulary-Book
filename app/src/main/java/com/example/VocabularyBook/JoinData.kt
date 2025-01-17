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
data class addwordinputdata(
    @field:SerializedName("word_eng")  val word_eng: String?,
    @field:SerializedName("mean") val mean: String?,
    @field:SerializedName("bookid") val bookid: Int?
)
data class editwordinputdata(
        @field:SerializedName("word_eng")  val word_eng: String?,
        @field:SerializedName("mean") val mean: String?,
        @field:SerializedName("bookid") val bookid: Int?,
        @field:SerializedName("wordid") val wordid: Int?
)
data class delwordinputdata(
        @field:SerializedName("bookid") val bookid: Int?,
        @field:SerializedName("wordid") val wordid: Int?
)

data class AddWordbookInput(
        @field:SerializedName("Uid") val Uid: Int?,
        @field:SerializedName("Rid") val Rid: Int?,
        @field:SerializedName("bookname") val bookname: String  ?
)
class AddwordbookResponse{
    @SerializedName("code")
    var code =0
    @SerializedName("message")
    var message: String? = null
    @SerializedName("bookid")
    var bookid: Int = 0
}

// getstudy input output
class studydata{

    @field:SerializedName("Rid")
    val Rid: Int=0
    @field:SerializedName("room_name")
    val room_name: String=""
    @field:SerializedName("host")
    val host: Int?=null
    @field:SerializedName("notice")
    val notice: String=""
    @field:SerializedName("msg")
    val msg: String?=null

}
class studylistResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("studylist")
    val studylist : Array<studydata>? =null
}


// add study 인풋 아웃풋 데이터
data class addStudyinput(
        @field:SerializedName("room_name") val room_name: String,
        @field:SerializedName("host") val host: Int?
)
class addStudyResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("Rid")
    val Rid : Int =0
}

// getstudybookid 인풋 아웃풋 데이터
data class studybookidinput(
    @field:SerializedName("Rid") val Rid: Int?
)
class studybookidResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("bookid")
    val bookid : Int =0
}

data class participatestudy(
    @field:SerializedName("Rid") val Rid: Int?,
    @field:SerializedName("Uid") val Uid: Int?,
    @field:SerializedName("ishost") val ishost: Boolean
)


interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginData?): Call<LoginResponse?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinData?): Call<JoinResponse?>?

    @POST("/user/addwordbook")
    fun AddWordbook(@Body data: AddWordbookInput) : Call<AddwordbookResponse?>?

    @POST("/user/wordbook")
    fun getmyWordbook(@Body data: UserData): Call<wordbooklistResponse?>?

    @POST("/user/wordbook/word")
    fun getWordlist(@Body data: wordbookiddata): Call<wordlistResponse?>?

    @POST("/user/wordbook/getcheckword")
    fun getCheckword(@Body data: getcheckwordinputdata): Call<checkwordResponse?>?

    @POST("/user/wordbook/checkword")
    fun Checkword(@Body data: checkwordinputdata): Call<NormalResponse?>?

    @POST("/user/wordbook/uncheckword")
    fun Uncheckword(@Body data: checkwordinputdata): Call<NormalResponse?>?

    @POST("/user/wordbook/addword")
    fun AddWord(@Body data :addwordinputdata): Call<NormalResponse?>?

    @POST("/user/wordbook/editword")
    fun EditWord(@Body data :editwordinputdata): Call<NormalResponse?>?

    @POST("/user/wordbook/delword")
    fun DelWord(@Body data :delwordinputdata): Call<NormalResponse?>?

    @POST("/user/study")
    fun getmyStudy(@Body data: UserData): Call<studylistResponse?>?

    @POST("/user/addstudy")
    fun addStudy(@Body data: addStudyinput) : Call<addStudyResponse?>?

    @POST("/study/wordbook")
    fun getStudybookid(@Body data: studybookidinput) : Call<studybookidResponse?>?

    @POST("/study/participate")
    fun particiapateStudy(@Body data: participatestudy): Call<NormalResponse?>?

    @POST("/study/updatemsg")
    fun updatestudymsg(@Body data: Updatestudymsginputdata): Call<NormalResponse?>?

    @POST("/study/reject")
    fun rejectstudy(@Body data: Rejectstudyinputdata): Call<NormalResponse?>?

    @POST("/study/getrank")
    fun getstudyRank(@Body data :getStudyRankinputdata): Call<getStudyRankResponse?>?

    @POST("/study/getmsg")
    fun getstudymsg(@Body data :getstudymsginputdata): Call<getStudyMsgResponse?>?

    @POST("/study/updatenotice")
    fun updatenotice(@Body data : updatenoticeinputdata) : Call<NormalResponse?>?

    @POST("/user/getuid")
    fun getUid(@Body data : getuidinputdata) : Call<getuidResponse?>?

}
data class getuidinputdata(
    @field:SerializedName("email") val email: String
)

class getuidResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("Uid")
    val Uid : Int? =null
}

data class updatenoticeinputdata(
        @field:SerializedName("notice") val notice: String,
        @field:SerializedName("Rid") val Rid:Int?
)

data class getstudymsginputdata(
    @field:SerializedName("Rid") val Rid: Int?,
    @field:SerializedName("flag") val flag: Boolean=false
)

class studymsg{
    @field:SerializedName("username")
    val username: String=""
    @field:SerializedName("Uid")
    val Uid: Int=-1
    @field:SerializedName("msg")
    val msg: String?=null
}

class getStudyMsgResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("msglist")
    val msglist : Array<studymsg>? =null
}
//
data class Updatestudymsginputdata(
        @field:SerializedName("Rid") val Rid: Int?,
        @field:SerializedName("Uid") val Uid: Int?,
        @field:SerializedName("msg") val msg: String  ?=null
)
data class Rejectstudyinputdata(
        @field:SerializedName("Rid") val Rid: Int?,
        @field:SerializedName("Uid") val Uid: Int?
)

// 스터디랭크
data class getStudyRankinputdata(
        @field:SerializedName("bookid") val bookid: Int?
)

class studyrank{
    @field:SerializedName("username")
    val username: String=""
    @field:SerializedName("count")
    val count: Int=0
}

class getStudyRankResponse{
    @SerializedName("code")
    val code = 0
    @SerializedName("message")
    val message: String? = null
    @SerializedName("rank")
    val rank : Array<studyrank>? =null
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