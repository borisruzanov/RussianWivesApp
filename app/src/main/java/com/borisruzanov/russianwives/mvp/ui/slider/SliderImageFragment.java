package com.borisruzanov.russianwives.mvp.ui.slider;


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
import com.borisruzanov.russianwives.mvp.model.interactor.slider.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_IMAGE_RATING;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class SliderImageFragment extends Fragment {

    Button btnChangeImage;
    String result;

    private StorageReference storageReference;

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

        new SliderRepository().getFieldFromCurrentUser("image", value -> result = value);

        btnChangeImage = view.findViewById(R.id.fragment_slider_image_btn_save);
        btnChangeImage.setOnClickListener(view1 -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
        });

        //TODO REFACTOR TO REPOSITORY
        storageReference = FirebaseStorage.getInstance().getReference();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Uri imageUri = data.getData();
            if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
                Log.d(Contract.TAG, "Image URI " + imageUri);
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(getContext(), this);
            }
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

                    // <-------------SAVING IMAGE-------------->
                    //TODO REFACTOR TO REPOSITORY
                    FirebaseRepository firebaseRepository = new FirebaseRepository();
                    StorageReference filePath = storageReference.child("profile_images").child(firebaseRepository.getUid()).child("profile_photo");
                    // <-------------SAVING IMAGE-------------->

                    filePath.putFile(resultUri).continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return filePath.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String download_url = task.getResult().toString();
                            Log.d("ImageDebug", "Image path is " + download_url);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(Consts.IMAGE, download_url);

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(getUid()).child(Consts.IMAGE).setValue(download_url);

                            new SliderRepository().updateFieldFromCurrentUser(hashMap, () -> {
                                if (result.equals(Consts.DEFAULT)) new RatingRepository().addRating(ADD_IMAGE_RATING);
                                if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                                    getActivity().onBackPressed();
                                }
                            });
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), R.string.there_is_an_error, Toast.LENGTH_LONG).show();
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.d(Contract.TAG, "resultCode == CropImage.ERROR");

                }
                Log.d(Contract.TAG, "requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE in ELSE");

            }


    }
}
