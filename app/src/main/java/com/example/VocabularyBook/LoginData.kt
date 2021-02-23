package com.example.VocabularyBook

import com.google.gson.annotations.SerializedName


class  friendrelation(
        @field:SerializedName("User1Uid")  val user1uid: Int,
        @field:SerializedName("User1Name")  val user1name: String,
        @field:SerializedName("User2Uid")  val user2uid: Int,
        @field:SerializedName("User2Name")  val user2name: String
)
class FriendlistResponse {
    @SerializedName("code")
    val code = 0

    @SerializedName("message")
    val message: String? = null

    @SerializedName("userlist")
    val userlist : Array<friendrelation>? =null

}