package com.borisruzanov.russianwives.mvp.ui.usersearch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.data.SystemRepository;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.google.firebase.analytics.FirebaseAnalytics;

public class DialogUserSearch extends Dialog {

    private FirebaseAnalytics firebaseAnalytics;
    private Button mSearchBtn;
    private EditText mEditText;
    private DialogUserSearchPresenter mPresenter;

    public DialogUserSearch(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);
        mPresenter = new DialogUserSearchPresenter(new MainInteractor(new UserRepository(new Prefs(getContext())), new HotUsersRepository(new Prefs(getContext())), new SystemRepository(new Prefs(getContext()))));
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mEditText = (EditText) findViewById(R.id.dialog_search_edit);
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        mSearchBtn = (Button) findViewById(R.id.dialog_search_button);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText.getText().length() == 10) {
                    Bundle bundle = new Bundle();
                    bundle.putString("searched_user", mEditText.getText().toString().trim());
                    firebaseAnalytics.logEvent("user_search", bundle);

                    closeKeyboard();
                    mPresenter.searchUser(mEditText.getText().toString().trim());
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.dialog_search_quantity_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeKeyboard();
    }



    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
