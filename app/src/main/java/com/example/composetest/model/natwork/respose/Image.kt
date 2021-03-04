package com.example.composetest.model.natwork.respose

import java.util.*

/**
 * 서버로 부터 받은 이미지 정보를 파싱합니다
 */
data class Result(
    val documents: List<Image>?,
    val meta: Pagination
)

data class Image(
    val collection: String, // "blog",
    val datetime: Date, //"2018-12-21T20:46:05.000+09:00",
    val display_sitename: String, // "티스토리",
    val doc_url: String, //"http://heathersummer.tistory.com/74",
    val height: Int, //573,
    val image_url: String, //"http://cfile5.uf.tistory.com/image/99AA874D5C1CCD7D129E65",
    val thumbnail_url: String, //"https://search3.kakaocdn.net/argon/130x130_85_c/Acj7WdO98cj",
    val width: Int, //580
)

data class Pagination(
    val is_end: Boolean,
    val total_count: Int,
    val pageable_count: Int,
)