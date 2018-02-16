package com.borisruzanov.russianwives;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    //Layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private EditText mNewStatus;
    private Button mSaveBtn;
    private Button mImageBtn;
    private ProgressDialog mProgressDialog;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.v("---Inside Settings---", "<==============");

        mDisplayImage = (CircleImageView) findViewById(R.id.settings_profile_image);
        mName = (TextView) findViewById(R.id.settings_display_name);
        mStatus = (TextView) findViewById(R.id.settings_status);
        mStatus.setVisibility(View.VISIBLE);

        mNewStatus = (EditText) findViewById(R.id.status_input);
        mImageBtn = (Button) findViewById(R.id.settings_btn_set_image);
        mSaveBtn = (Button) findViewById(R.id.settings_btn_status_save);
        mSaveBtn.setVisibility(View.GONE);
        mNewStatus.setVisibility(View.GONE);


        mImageStorage = FirebaseStorage.getInstance().getReference();
        //Get the object of current user from Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //Listening data from Firebase
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if (!image.equals("default")) {
                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar).into(mDisplayImage);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        statusListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(500, 500)
                    .start(this);
            Log.v("====>", "URI = " + imageUri.toString());
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //TODO Загрузку образа в другой поток
                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                Uri resultUri = result.getUri();

                File thumbFilepath = new File(resultUri.getPath());

                String currentUserId = mCurrentUser.getUid();

                //Compress the image

                Bitmap thumbBitmap = null;
                try {
                    thumbBitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumbFilepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumbByte = baos.toByteArray();


                //Set path for image
                StorageReference filePath = mImageStorage.child("profile_images").child(currentUserId + ".jpg");
                final StorageReference thumbFilePath = mImageStorage.child("profile_images").child("thumbs").child(currentUserId + ".jpg");

                //TODO починить чтобы сохранялось не в разных папках и один файл обновлялся
                //Put image in reference
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        //TODO обработать ошибку если загрузка файла займет много времени
                        //TODO почему то сохраняет в разные папки и не ставит фикс квадрат
                        if (task.isSuccessful()) {

                            //Getting URL from Image whoch was uploaded in storage
                            final String downloadedUrl = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumbFilePath.putBytes(thumbByte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumbDownloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()){
                                        Map updateHashMap = new HashMap();
                                        updateHashMap.put("image", downloadedUrl);
                                        updateHashMap.put("thumb_image", thumbDownloadUrl);

                                        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    mProgressDialog.dismiss();
                                                } else {
                                                    //TODO Handle error загрузка образа
                                                    mProgressDialog.dismiss();
                                                    Log.v("====>", "Error in thumbnail");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            Log.v(" ====> ", "Error in uploading");
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void statusListener() {
        mStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus.setVisibility(View.GONE);
                mSaveBtn.setVisibility(View.VISIBLE);
                mNewStatus.setVisibility(View.VISIBLE);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Saving changes.");
                mProgressDialog.setMessage("Please wait while we are saving changes.");
                mProgressDialog.show();
                final String newStatus = mNewStatus.getText().toString();
                mUserDatabase.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            mStatus.setText(newStatus);
                        } else {
                            Toast.makeText(getApplicationContext(), "There was a mistake while saving changes", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                mSaveBtn.setVisibility(View.GONE);
                mNewStatus.setVisibility(View.GONE);
                mStatus.setVisibility(View.VISIBLE);
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Создать интент с камеры для селфи
                /*Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);*/

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
            }
        });


    }

}
