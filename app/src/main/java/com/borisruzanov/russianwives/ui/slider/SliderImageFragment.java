package com.borisruzanov.russianwives.ui.slider;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.interactor.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SliderFragmentsPresenter;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import durdinapps.rxfirebase2.RxFirestore;

import static android.app.Activity.RESULT_OK;

public class SliderImageFragment extends Fragment {

    Button btnChangeImage;
    Button btnClose;
    Button btnNext;

    private StorageReference storageReference;

    SliderFragmentsPresenter sliderFragmentsPresenter;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog progressDialog;

    public SliderImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_image, container, false);
        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());
        btnChangeImage = (Button) view.findViewById(R.id.fragment_slider_image_btn_save);
        btnClose = (Button) view.findViewById(R.id.fragment_slider_image_btn_close);
        btnNext = (Button) view.findViewById(R.id.fragment_slider_image_btn_next);
        if (getActivity().getIntent().getExtras().getString("field_id").equals("image")) {
            btnClose.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        } else {
            Log.d("tag", "Inside ELSE " + getActivity().getIntent().getExtras().getString("field_id"));

        }
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        //TODO REFACTOR TO REPOSITORY
        storageReference = FirebaseStorage.getInstance().getReference();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Log.d(Contract.TAG, "resultCode == RESULT_OK");

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();


                Uri resultUri = result.getUri();
                Log.d(Contract.TAG, "resultUri is " + resultUri.toString());
//                sliderFragmentsPresenter.insertImageInStorage(resultUri);

                // <-------------SAVING IMAGE-------------->
                //TODO REFACTOR TO REPOSITORY
                FirebaseRepository firebaseRepository = new FirebaseRepository();
                StorageReference filePath = storageReference.child("profile_images").child(firebaseRepository.getUid()).child("profile_photo");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Photo was updated", Toast.LENGTH_LONG).show();
                            String download_url = task.getResult().getDownloadUrl().toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(Consts.IMAGE, download_url);

                            new FirebaseRepository().updateFieldFromCurrentUser(hashMap, new UpdateCallback() {
                                @Override
                                public void onUpdate() {
                                    getActivity().onBackPressed();
                                }
                            });
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                        }

                    }
                    // <-------------SAVING IMAGE-------------->


                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(Contract.TAG, "resultCode == CropImage.ERROR");

            }
            Log.d(Contract.TAG, "requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE in ELSE");

        }
    }
}
