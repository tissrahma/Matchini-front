package com.example.matchinii.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.matchinii.R
import com.example.matchinii.models.*
import com.example.matchinii.viewmodels.MessagesInterface
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatRoomActivity : AppCompatActivity(), View.OnClickListener {

   var messages = MessagesInterface.create()

    val TAG = ChatRoomActivity::class.java.simpleName

    lateinit var send: ImageView
    lateinit var leave: ImageView
    lateinit var editText :EditText;
    lateinit var mSocket: Socket;
    lateinit var userName: String;
    private lateinit var roomName: String;
    lateinit var user: TextView
    private lateinit var imageIntent :String;
    lateinit var reture: ImageView
    private lateinit var imageuser: ImageView;
    var id1intent : String? = null
    var id2intent : String? = null
    var msg : Message? = null
    val gson: Gson = Gson()

    val chatList: ArrayList<Message> = arrayListOf();
    lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        send = findViewById(R.id.send)
        leave = findViewById(R.id.leave)
        editText = findViewById(R.id.editText)
        send.setOnClickListener(this)
        leave.setOnClickListener(this)
        user = findViewById<TextView>(R.id.partnerName)
        imageuser= findViewById(R.id.imageusr)
        reture=findViewById(R.id.leave)
        reture.setOnClickListener(){
            startActivity(Intent(this@ChatRoomActivity, ChatActivity::class.java))

        }

        //Get the nickname and roomname from entrance activity.
        /*    try {
            userName = intent.getStringExtra("userName")!!
            roomName = intent.getStringExtra("roomName")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        var k=intent.getStringExtra("name")!!
        userName = k
        imageIntent =intent.getStringExtra("Image").toString()
         id1intent =intent.getStringExtra("id1").toString()
        id2intent =intent.getStringExtra("id2").toString()
        Glide.with(applicationContext).load(imageIntent).into(imageusr)
         user.setText(userName)
       /*  roomName=intent.getStringExtra("romee")!!
         val map: HashMap<String, String> = HashMap()
          map["UserM1"] = id1intent.toString()
          map["UserM2"] = id2intent.toString()
          map["RommeName"] = roomName
        map["messageUser1"] = "jjjj"
         messages.addmessage(id1intent.toString(),map).enqueue(object : Callback<Messages>{
              override fun onResponse(call: Call<Messages>, response: Response<Messages>) {
                  Log.e("idintent" , id1intent.toString())
                  Log.e("idintent" , id2intent.toString())
              }
              override fun onFailure(call: Call<Messages>, t: Throwable) {
                  println("failed")
              }
          })*/
        roomName=intent.getStringExtra("romee")!!
          var messages = MessagesInterface.create()
        val map3: HashMap<String, String> = HashMap()
        map3["RommeName"] = roomName
        Log.e("namechat",map3.toString())
        messages.showmessage(map3).enqueue(object  : Callback<ArrayList<data>>{
            override fun onResponse(call: Call<ArrayList<data>>, response: Response<ArrayList<data>>) {
                Log.e("jhgjkjhgbj" , response.body().toString())
                for( i in response.body()!!.indices) {
                    if( response.body()!![i].key=="user1"){
                    msg = Message(userName , response.body()!![i].value , roomName , 1)}
                    else{
                        msg = Message(userName , response.body()!![i].value , roomName , 0)
                    }


                    chatList.add(msg!!)
                    Log.e("======1======" ,chatList.toString())
                    chatRoomAdapter = ChatRoomAdapter(this@ChatRoomActivity, chatList , Intent(this@ChatRoomActivity, ChatRoomAdapter::class.java).putExtra("romee" , roomName));
                    recyclerView.adapter = chatRoomAdapter;
                }
            }
            override fun onFailure(call: Call<ArrayList<data>>, t: Throwable) {
                println("failed")
                Log.e("cause" , t.cause.toString())
            }
        })

        Log.e("======2======" ,chatList.toString())
        chatRoomAdapter = ChatRoomAdapter(this, chatList , Intent(this@ChatRoomActivity, ChatRoomAdapter::class.java).putExtra("romee" , roomName));
        recyclerView.adapter = chatRoomAdapter;
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        try {
            mSocket = IO.socket("http://10.0.2.2:3000")
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("newUserToChatRoom", onNewUser)
        mSocket.on("updateChat", onUpdateChat)
        mSocket.on("userLeftChatRoom", onUserLeft)
    }

    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat: Message = Message(leftUserName, "", "", MessageType.USER_LEAVE.index)
        addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        chat.viewType = MessageType.CHAT_PARTNER.index
        addItemToRecyclerView(chat)
    }

    var onConnect = Emitter.Listener {
        val data = initialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)
    }

    var onNewUser = Emitter.Listener {
        val name = it[0] as String //This pass the userName!
        val chat = Message(name, "", roomName, MessageType.USER_JOIN.index)
        addItemToRecyclerView(chat)
        Log.d(TAG, "on New User triggered.")
    }


    private fun sendMessage() {
        val content = editText.text.toString()
        val sendData = SendMessage(userName, content, roomName)
        val jsonData = gson.toJson(sendData)
        mSocket.emit("newMessage", jsonData)

        val message = Message(userName, content, roomName, MessageType.CHAT_MINE.index)
        addItemToRecyclerView(message)
        val map: HashMap<String, String> = HashMap()
        map["UserM1"] = id1intent.toString()
        map["UserM2"] = id2intent.toString()
        map["messageUser1"] = content
        map["RommeName"] = roomName
        Log.e("id1intent" , id1intent.toString())
        Log.e("id2intent" , id2intent.toString())
        Log.e("idintent" , content)
        messages.addmessage(id1intent.toString(),roomName,map).enqueue(object : Callback<Messages>{
            override fun onResponse(call: Call<Messages>, response: Response<Messages>) {
                Log.e("idintent" , id1intent.toString())
                Log.e("idintent" , id2intent.toString())
                Log.e("idintent" , content)
            }
            override fun onFailure(call: Call<Messages>, t: Throwable) {
                t
            }
        })

    }
    private fun addItemToRecyclerView(message: Message) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!
        runOnUiThread {
            chatList.add(message)
            chatRoomAdapter.notifyItemInserted(chatList.size)
            editText.setText("")
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
            R.id.leave -> onDestroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = initialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()
    }

}