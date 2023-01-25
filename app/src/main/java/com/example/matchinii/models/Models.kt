package com.example.matchinii.models

data class Message (val userName : String, var messageContent : String, val roomName: String, var viewType : Int)
data class initialData (val userName : String, val roomName : String)
data class SendMessage(val userName : String, val messageContent: String, val roomName: String)