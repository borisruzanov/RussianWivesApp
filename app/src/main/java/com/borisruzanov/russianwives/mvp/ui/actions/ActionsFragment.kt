package com.borisruzanov.russianwives.mvp.ui.actions


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.mvp.ui.actions.adapter.ActionsAdapter
import com.borisruzanov.russianwives.OnItemClickListener
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.models.ActionItem
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity

import javax.inject.Inject

class ActionsFragment : MvpAppCompatFragment(), ActionsView {

    @Inject
    @InjectPresenter
    lateinit var actionsPresenter: ActionsPresenter

    private lateinit var recyclerActivitiesList: RecyclerView

    private lateinit var emptyText: RelativeLayout

    private val onItemClickCallback
            = OnItemClickListener.OnItemClickCallback{ _: View, position: Int -> actionsPresenter.openFriendProfile(position) }

    private val actionsAdapter = ActionsAdapter(onItemClickCallback)

    @ProvidePresenter
    fun provideActionsPresenter(): ActionsPresenter = actionsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main_tab_activities, container, false)

        // todo: use syntetic
        recyclerActivitiesList = view.findViewById(R.id.friends_fragment_recycler_activities)
        recyclerActivitiesList.layoutManager = LinearLayoutManager(activity)
        recyclerActivitiesList.setHasFixedSize(true)
        recyclerActivitiesList.addItemDecoration(DividerItemDecoration(recyclerActivitiesList.context,
                DividerItemDecoration.VERTICAL))

        recyclerActivitiesList.adapter = actionsAdapter

        emptyText = view.findViewById(R.id.activities_empty_text)

        //actionsPresenter.setActionsList()

       /* FirebaseDatabase.getInstance().reference.child("Likes").child(getUid())
                .addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                Log.d("ActionsBack", "in onChildAdded")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                Log.d("ActionsBack", "in onChildChanged")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("ActionsBack", "in onChildRemoved")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })*/

        // Inflate the layout for this fragment
        return view
    }

    override fun showUserActions(actionItems: List<ActionItem>) {
        if (actionItems.isNotEmpty()) {
            emptyText.visibility = View.GONE
            recyclerActivitiesList.post { actionsAdapter.setData(actionItems) }
        } else {
            recyclerActivitiesList.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        }
    }

    override fun updateUserActions(newActionItems: List<ActionItem>) {
        if (newActionItems.isNotEmpty()) {
            emptyText.visibility = View.GONE
            recyclerActivitiesList.post { actionsAdapter.clearAndUpdateData(newActionItems) }
        } else {
            recyclerActivitiesList.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        }
    }

    override fun openFriendProfile(friendUid: String) {
        val friendProfileIntent = Intent(activity, FriendProfileActivity::class.java)
        friendProfileIntent.putExtra("uid", friendUid)
        startActivity(friendProfileIntent)
    }

    override fun onResume() {
        super.onResume()

        actionsPresenter.updateActionsList()
    }

}