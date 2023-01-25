/**
 * @author Joyce Hong
 * @email soja0524@gmail.com
 * @created 2019-09-03
 * @desc
 */

package com.example.matchinii.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matchinii.R
import com.example.matchinii.models.Message
import com.example.matchinii.models.Messages
import com.example.matchinii.models.data
import com.example.matchinii.models.data2
import com.example.matchinii.viewmodels.MessagesInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomAdapter(val context : Context, val chatList : ArrayList<Message> , private val intent  : Intent) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>(){

    val CHAT_MINE = 0
    val CHAT_PARTNER = 1
    val USER_JOIN = 2
    val USER_LEAVE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("chatlist size",chatList.size.toString())
        var view : View? = null
        when(viewType){

            0 ->{
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_user,parent,false)
                Log.d("user inflating","viewType : ${viewType}")
            }

            1 ->
            {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_partner,parent,false)
                Log.d("partner inflating","viewType : ${viewType}")
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
        }

        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageData  = chatList[position]
        val userName = messageData.userName;
        var content = messageData.messageContent;
        val viewType = messageData.viewType;
        Log.e("yoyoyoyoy" , content)
//        var messages = MessagesInterface.create()
//        val map: HashMap<String, String> = HashMap()
//        var romee = intent.getStringExtra("romee").toString()
//        map["RommeName"] = romee
//        Log.e(";;;;;;;" ,map.toString() )
//        messages.showmessage(map).enqueue(object : Callback<Messages>{
//            @SuppressLint("SuspiciousIndentation")
//
//            override fun onResponse(call: Call<Messages>, response: Response<Messages>) {
//                println("yooooo")
//                content = response.body()!!.messageUser1
//
//            }
//            override fun onFailure(call: Call<Messages>, t: Throwable) {
//                println("failed")
//            }
//        })

        when(viewType) {

            CHAT_MINE -> {
                holder.message.text = content
            }
            CHAT_PARTNER ->{
             holder.message.text= content
             holder.userName.text = userName

            }
            USER_JOIN -> {
                val text = "${userName} has entered the room"
                holder.text.text = text
            }
            USER_LEAVE -> {
                val text = "${userName} has leaved the room"
                holder.text.text = text
            }
        }
    }
    inner class ViewHolder(itemView : View):  RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.username)
        val message = itemView.findViewById<TextView>(R.id.message)
        val text = itemView.findViewById<TextView>(R.id.text)
    }

}