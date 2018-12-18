package com.borisruzanov.russianwives.mvp.ui.neccessaryinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.utils.Consts;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class NecessaryInfoDialogFragment extends MvpAppCompatDialogFragment {

    public static String TAG = "Necessary info";

    @BindView(R.id.spinner_age_ni)
    Spinner ageSpinner;
    @BindView(R.id.spinner_gender_ni)
    Spinner genderSpinner;

    @BindView(R.id.photo_add_button)
    Button photoAddButton;

    @BindView(R.id.gender_layout)
    RelativeLayout genderLayout;
    @BindView(R.id.age_layout)
    RelativeLayout ageLayout;

    @BindView(R.id.confirm_button_ni)
    Button confirmButton;
    @BindView(R.id.cancel_button_ni)
    Button cancelButton;

    String image = Consts.DEFAULT;
    private int GALLERY_PICK = 7;

    NecessaryInfoListener listener;

    public static NecessaryInfoDialogFragment newInstance(String image, String gender, String age) {
        NecessaryInfoDialogFragment fragment = new NecessaryInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString(Consts.IMAGE, image);
        args.putString(Consts.GENDER, gender);
        args.putString(Consts.AGE, age);
        fragment.setArguments(args);
        return fragment;
    }

    public interface NecessaryInfoListener {
        void setInfo(String image, String gender, String age);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NecessaryInfoListener) {
            listener = (NecessaryInfoListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_necessary_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().getString(Consts.GENDER) != null && !getArguments().getString(Consts.GENDER).equals(Consts.DEFAULT)) {
            genderLayout.setVisibility(View.GONE);
        }
        if (getArguments().getString(Consts.AGE) != null && !getArguments().getString(Consts.AGE).equals(Consts.DEFAULT)) {
            ageLayout.setVisibility(View.GONE);
        }
        if (getArguments().getString(Consts.IMAGE) != null && getArguments().getString(Consts.IMAGE).equals(Consts.DEFAULT)) {
            photoAddButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.photo_add_button)
    public void onPhotoAddButton() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.select_image)), GALLERY_PICK);
    }

    @OnClick(R.id.confirm_button_ni)
    public void onConfirmClicked() {
        String gender = genderSpinner.getSelectedItem().toString();
        String age = ageSpinner.getSelectedItem().toString();
        listener.setInfo(image, gender, age);
        dismiss();
    }

    @OnClick(R.id.cancel_button_ni)
    public void onCancelClicked() {
        dismiss();
    }

    private int getIndexOfElement(@ArrayRes int resId, String value) {
        List<String> list = Arrays.asList((getResources().getStringArray(resId)));
        return list.indexOf(value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                CropImage.activity(selectedImageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(300, 300)
                        .start(getContext(), this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.d(Contract.TAG, "requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE in IF");
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Log.d(Contract.TAG, "resultCode == RESULT_OK");

                    image = result.getUri().toString();
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.d(Contract.TAG, "resultCode == " + error.getMessage());
                }
            }
        }
    }

        @Override
        public void onResume () {
            super.onResume();
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
