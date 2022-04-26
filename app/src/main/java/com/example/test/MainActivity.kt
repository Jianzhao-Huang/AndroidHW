package com.example.test

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.wrap.*


class Video(val path: Uri, var likeNum :Int, var commentNum: Int, var collectNum: Int, var shareNum: Int)

class VideoAdapter(val videoList: List<Video>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val likeNum = view.likeNum
        val commentNum = view.commentNum
        val collectNum = view.collectNum
        val shareNum = view.shareNum
        val videoView = view.videoView
        val stopIcon = view.stopIcon
    }


    private  var holder: ViewHolder ?= null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList[position]
        holder.videoView.setVideoURI(video.path)
        holder.likeNum.text = video.likeNum.toString()
        holder.commentNum.text = video.commentNum.toString()
        holder.collectNum.text = video.collectNum.toString()
        holder.shareNum.text = video.shareNum.toString()
    }

    override fun getItemCount() = videoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false))
        val videoView = viewHolder.videoView
        val stopIcon = viewHolder.stopIcon
        videoView.setOnCompletionListener{ mPlayer ->
            mPlayer.start()
            mPlayer.isLooping = true
        }
        videoView.setOnClickListener{
            if(videoView.isPlaying){
                videoView.pause()
                stopIcon.visibility = View.VISIBLE
            }
            else{
                videoView.start()
                stopIcon.visibility = View.INVISIBLE
            }
        }
        return viewHolder
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.videoView.start()
        holder.stopIcon.visibility = View.INVISIBLE
        this.holder = holder
        holder.videoView.requestFocus()
    }

    fun getHolder(): ViewHolder?{
        return holder
    }

}

class FeedFragment() : Fragment(){
    private val videoList = ArrayList<Video>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home, container, false)
        videoList.add(Video(Uri.parse("android.resource://${activity?.packageName}/${R.raw.video1}"),10000, 2, 3, 4))
        videoList.add(Video(Uri.parse("android.resource://${activity?.packageName}/${R.raw.video2}"),3, 200, 3, 4))
        val recyclerView = view.findViewById<RecyclerView>(R.id.myre)
        recyclerView.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = VideoAdapter(videoList)
        }
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        return view
    }

    override fun onResume() {
        super.onResume()
        val videoView = (view?.findViewById<RecyclerView>(R.id.myre)?.adapter as VideoAdapter).getHolder()?.videoView
        videoView?.start()
        videoView?.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        (view?.findViewById<RecyclerView>(R.id.myre)?.adapter as VideoAdapter).getHolder()?.stopIcon?.visibility = View.INVISIBLE

    }

}

class PersonFragment() : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.person, container, false)
}

class SubscribeFragment() : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.subscribe, container, false)
}

class WrapFragment() : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.wrap, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        wrap_viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = 2
            override fun createFragment(position: Int) =
                when(position){
                    0 -> SubscribeFragment()
                    1 -> FeedFragment()
                    else -> FeedFragment()
                }
        }
        TabLayoutMediator(view.findViewById(R.id.tabLayout), wrap_viewPager){ tab, position ->
            when(position){
                0 -> tab.text = "关注"
                1 -> tab.text = "推荐"
            }
        }.attach()
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = 2
            override fun createFragment(position: Int) =
                when(position){
                    0 -> WrapFragment()
                    1 -> MsgFragment()
                    else -> PersonFragment()
                }
        }
    }
}