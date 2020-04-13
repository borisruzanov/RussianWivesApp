package com.borisruzanov.russianwives.mvp.ui.complain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComplainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplainFragment extends MvpAppCompatDialogFragment {

    public static final String TAG = "COMPLAIN_TAG";
    private EditText mInput;
    private Button mSendButton;
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAnalytics firebaseAnalytics;
    private Prefs mPrefs;

    public ComplainFragment() {
        // Required empty public constructor
    }

    public static ComplainFragment newInstance() {
        ComplainFragment fragment = new ComplainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complain, container, false);
        mInput = view.findViewById(R.id.complain_input);
        mSendButton = view.findViewById(R.id.complain_send_button);
        Intent intent = getActivity().getIntent();
        String uid = intent.getStringExtra(Consts.UID);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("complain_shown", null);
        mPrefs = new Prefs(getActivity());

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAnalytics.logEvent("complain_sent", null);
                mPrefs.setValue(Consts.REVIEW_STATUS, Consts.CONFIRMED);
                String result = mInput.getText().toString();
                realtimeReference.child("Complains").child(uid).setValue(result);
                dismiss();
            }
        });
        return view;
    }
}
