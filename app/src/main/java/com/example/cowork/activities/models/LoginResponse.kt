package com.example.cowork.activities.models

data class LoginResponse(
    val status: Int,
    val result: Result,
    val message:String = ""
)

data class Result(val logger: User,
                  val token: String)