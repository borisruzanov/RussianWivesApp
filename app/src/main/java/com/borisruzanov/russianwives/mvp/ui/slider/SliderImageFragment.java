package com.borisruzanov.russianwives.mvp.ui.slider;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.interactor.slider.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;

public class SliderImageFragment extends Fragment {

    Button btnChangeImage;

    SliderFragmentsPresenter sliderFragmentsPresenter;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog progressDialog;

    public SliderImageFragment() {
        // Required empty public constructor
    }

    public static SliderImageFragment newInstance() {
        SliderImageFragment fragment = new SliderImageFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_image, container, false);
        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new SliderRepository()));
        progressDialog = new ProgressDialog(getActivity());

        btnChangeImage = view.findViewById(R.id.fragment_slider_image_btn_save);
        btnChangeImage.setOnClickListener(view1 -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.add_your_photo)), GALLERY_PICK);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri imageUri = data.getData();
            if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
                Log.d(Contract.TAG, "Image URI " + imageUri);
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(getContext(), this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.d(Contract.TAG, "requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE in IF");
                if (resultCode == RESULT_OK) {
                    Log.d(Contract.TAG, "resultCode == RESULT_OK");
                    progressDialog.setTitle(getString(R.string.uploading_image));
                    progressDialog.setMessage(getString(R.string.please_wait));
                    progressDialog.show();


                    Uri resultUri = CropImage.getActivityResult(data).getUri();
                    Log.d(Contract.TAG, "resultUri is " + resultUri.toString());

                    new SliderRepository().uploadUserPhoto(resultUri, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                    });

                    Toast.makeText(getActivity(), getString(R.string.image_updated), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new StringEvent("next_page"));
                } else {
                    Toast.makeText(getActivity(), R.string.there_is_an_error, Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = CropImage.getActivityResult(data).getError();
                Log.d(Contract.TAG, "resultCode == CropImage.ERROR");

            }
        }

    }

}