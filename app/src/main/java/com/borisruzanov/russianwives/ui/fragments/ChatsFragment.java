package com.borisruzanov.russianwives.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.ChatsAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.mvp.model.interactor.ChatsInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter;
import com.borisruzanov.russianwives.mvp.view.ChatsView;
import com.borisruzanov.russianwives.ui.ChatMessageActivity;

import java.util.List;

public class ChatsFragment extends MvpAppCompatFragment implements ChatsView {

    RecyclerView recyclerChatsList;
    ChatsAdapter chatsAdapter;

    TextView emptyText;

    private View mMainView;

    @InjectPresenter
    ChatsPresenter chatsPresenter;

    @ProvidePresenter
    public ChatsPresenter provideChatsPresenter(){
        return new ChatsPresenter(new ChatsInteractor(new FirebaseRepository()));
    }

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false);

        emptyText = mMainView.findViewById(R.id.chats_empty_text);

        recyclerChatsList = mMainView.findViewById(R.id.friends_fragment_recycler_chats);
        recyclerChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChatsList.setHasFixedSize(true);
        recyclerChatsList.addItemDecoration(new DividerItemDecoration(recyclerChatsList.getContext(), DividerItemDecoration.VERTICAL));
        chatsAdapter = new ChatsAdapter(onItemClickCallback);
        recyclerChatsList.setAdapter(chatsAdapter);

        chatsPresenter.getUserChatList();
        return mMainView;
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {
        Log.d(Contract.TAG, "In onCLICK");
        chatsPresenter.openChat(position);

    };

    /**
     * Getting the list of user's chats and set it to adapter or show empty text
     * @param userChats
     */
    @Override
    public void showUserChats(List<UserChat> userChats) {
        if(!userChats.isEmpty()) {
            emptyText.setVisibility(View.GONE);
            recyclerChatsList.post(() -> chatsAdapter.setData(userChats));
        }
        else {
            recyclerChatsList.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    //TODO uncomment addUserActivity

    /**
     * Open clicked chat
     * @param uid
     * @param name
     * @param image
     */
    @Override
    public void openChat(String uid, String name, String image) {
        Intent chatIntent = new Intent(getContext(), ChatMessageActivity.class);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("name",name);
        chatIntent.putExtra("photo_url", image);
        startActivity(chatIntent);

        new FirebaseRepository().addUserToActivity(new FirebaseRepository().getUid(), uid);
    }


}
