package com.example.matchinii.ui




import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.models.data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.contracts.contract


class RecyclerAdapter (private val UserList: ArrayList<User> , private val intent  : Intent) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_item , parent , false)
        return  ViewHolder(v)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int){
            var name =  UserList[position].login.substringBefore('@')
       holder.itemName.text = name
        var image = UserList[position].Image

        val login = intent.getStringExtra("login")
        val map2: HashMap<String, String> = HashMap()
        map2["login"] = login.toString()
        Log.e("=========",login.toString())
        holder.itemImage.setOnClickListener{
           apiservice.getObjectId(map2 ,UserList[position].login).enqueue(object :Callback<ArrayList<data>>{
               override fun onResponse(
                   call: Call<ArrayList<data>>,
                   response: Response<ArrayList<data>>
               ) {
                   val id = response.body()
                   if (id != null) {
                       val id1 = response.body()?.get(0)?.value
                       val id2 = response.body()?.get(1)?.value
                       Log.e("id1id1id1id1",id1.toString())
                       Log.e("id2id2id2id2",id2.toString())
                       apiservice.rome(id1!! , id2!!).enqueue(object :Callback<String>{
                           override fun onResponse(call: Call<String>, response: Response<String>) {
                               val romeName = response.body()
                               Log.e("===",response.body().toString())
                               if (romeName != null) {

                               }
                               val intent = Intent(holder.itemView.context, ChatRoomActivity::class.java)
                               intent.apply {
                                   putExtra("romee", response.body())
                                   putExtra("name", login.toString())
                                   putExtra("Image",image)
                                   putExtra("id1",id1)
                                   putExtra("id2",id2)
                                   putExtra("name",name)
                                   Log.e("romee", response.body().toString())
                                   Log.e("name", login.toString())
                               }
                               holder.itemView.context.startActivity(intent)
                           }


                           override fun onFailure(call: Call<String>, t: Throwable) {
                               println("non")
                           }
                       })
               }
               }
               override fun onFailure(call: Call<ArrayList<data>>, t: Throwable) {
                  println("ghn")
               }

           })
        }
        Glide.with(holder.itemImage.context).load(image).into(holder.itemImage)
        Log.e("tttttttt" , UserList.toString())

    }
    override fun getItemCount(): Int {
       return  UserList.size
    }
    inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView
        var itemName : TextView
        init {
            itemImage = itemView.findViewById<ImageView>(R.id.userProfileImage)
            itemName = itemView.findViewById<TextView>(R.id.textView9)
        }
    }


}