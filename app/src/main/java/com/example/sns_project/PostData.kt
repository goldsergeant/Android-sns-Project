package com.example.sns_project

data class PostData (var uid : String? = null,//유저 정보
                     var userId: String? = null,

                     var posttext : String? = null,//글
                     var imageUrl : String? = null,//그림

                     var timestamp : Long? = null,//포스팅 시간

                     var likecount : Int = 0,//좋아요 수
                     var likes : MutableMap<String,Boolean> = HashMap())//중복좋아요 방지
{
    data class Comment(var uid : String? = null,//유저 정보
                       var userId: String? = null,

                       var comment: String? = null, //댓글
                       var timestamp: Long? = null) //댓글 게시 시간
}
