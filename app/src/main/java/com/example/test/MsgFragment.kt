package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.msg.view.*
import kotlinx.android.synthetic.main.msg_item.view.*
import java.util.*

class User(val userName: String, val userIconId: Int)

class Msg(val user: User, val msgContent: String)

class MsgAdapter(val msgList: List<Msg>) : RecyclerView.Adapter<MsgAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val userIcon = view.userIcon
        val userName = view.userName
        val contentView = view.contentView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.msg_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = msgList[position]
        holder.userIcon.setImageResource(msg.user.userIconId)
        holder.userName.text = msg.user.userName
        holder.contentView.text = msg.msgContent
    }

    override fun getItemCount() = msgList.size
}


class MsgFragment() : Fragment() {
    private val msgList = LinkedList<Msg>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.msg, container, false)
        repeat(5){
            msgList.add(Msg(User("兔兔", R.drawable.rabbit), "兔子很可爱"))
            msgList.add(Msg(User("帅弟弟", R.drawable.cool_boy), "弟弟非常帅"))
            msgList.add(Msg(User("酷姐姐", R.drawable.cool_girl), "这个人很酷"))
            msgList.add(Msg(User("温柔大姐姐", R.drawable.gentle_girl), "姐姐会疼人"))
        }
        view.msg_rv.adapter = MsgAdapter(msgList)
        view.msg_rv.layoutManager = LinearLayoutManager(activity)
        return view
    }
}