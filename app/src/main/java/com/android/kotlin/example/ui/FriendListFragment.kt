package com.android.kotlin.example.ui


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.kotlin.example.R
import com.android.kotlin.example.data.MockFriendsApi
import com.android.kotlin.example.data.RetrofitClientMock
import com.android.kotlin.example.data.UserResponse
import kotlinx.android.synthetic.main.fragment_friend_list.*
import kotlinx.android.synthetic.main.item_friend.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendListFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_friend_list

    var mFriendListAdapter : FriendListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO init recycle view
        recycle_view.layoutManager = LinearLayoutManager(context)

        mFriendListAdapter = FriendListAdapter()
        recycle_view.adapter = mFriendListAdapter

        // TODO call api
        val mockFriendsApi = RetrofitClientMock.getInstance().create(MockFriendsApi::class.java)
        val request = mockFriendsApi.getAllFriends()
        request.enqueue(object : Callback<List<UserResponse>>{
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                val friendList = response.body()
                if (friendList != null) {
                    mFriendListAdapter!!.setItemList(friendList)
                }
            }

        })
    }

    class FriendListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var mFriendList : List<UserResponse> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return FriendViewHolder(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is FriendViewHolder){
                holder.bind(mFriendList.get(position))
            }
        }

        override fun getItemCount(): Int {
            return mFriendList.size
        }

        fun setItemList(friendList: List<UserResponse>) {
            mFriendList = friendList
            notifyDataSetChanged()
        }
    }

    class FriendViewHolder : RecyclerView.ViewHolder {

        companion object {
            fun getView(parent: ViewGroup): View {
                return LayoutInflater.from(parent?.context).inflate(R.layout.item_friend, null);
            }
        }

        constructor(parent: ViewGroup) : super(getView(parent))

        fun bind(userResponse: UserResponse?) {
            Log.d("TEST", ">>> bind userResponse?.name " + userResponse?.name)
            itemView.tv_name.text = userResponse?.name
        }
    }
}
