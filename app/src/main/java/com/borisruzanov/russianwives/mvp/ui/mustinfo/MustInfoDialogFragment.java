package com.borisruzanov.russianwives.mvp.ui.mustinfo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.ui.messagedialog.MessageDialogFragment;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.theartofdev.edmodo.cropper.CropImage;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MustInfoDialogFragment extends MvpAppCompatDialogFragment implements MustInfoDialogView, AdapterView.OnItemSelectedListener {

    public static final String TAG = "Must_Info_TAG";

    private String image = Consts.DEFAULT;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog progressDialog;

    @Inject
    @InjectPresenter
    MustInfoDialogPresenter presenter;

    @ProvidePresenter
    MustInfoDialogPresenter provideMustInfoDialogPresenter() {
        return presenter;
    }

    @BindView(R.id.spinner_age_mi)
    Spinner ageSpinner;

    @BindView(R.id.spinner_country_mi)
    Spinner countrySpinner;

    @BindView(R.id.button_ok_mi)
    Button confirmButton;

    @BindView(R.id.add_photo_btn_mi)
    Button addPhotoButton;

    private FirebaseAnalytics firebaseAnalytics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        App application = (App)getActivity().getApplication();
        application.getComponent().inject(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("must_info_appear", null);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d =  super.onCreateDialog(savedInstanceState);
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_must_info, container, false);
        ButterKnife.bind(this, view);
        ageSpinner.setOnItemSelectedListener(this);
        countrySpinner.setOnItemSelectedListener(this);
        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    @OnClick(R.id.add_photo_btn_mi)
    public void onPhotoAddBtnClicked() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(messageDialogFragment,
                MessageDialogFragment.TAG).commit();
        new Handler().postDelayed(() -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(messageDialogFragment).commit();
            startActivityForResult(Intent.createChooser(
                    galleryIntent, getString(R.string.add_your_photo)), GALLERY_PICK);
        }, 1000);
    }

    @OnClick(R.id.button_ok_mi)
    public void onButtonClicked() {
        String age = ageSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        presenter.saveValues(age, country, image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK) {
                CropImage.activity(data.getData())
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(getContext(), this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    showProgress();
                    Uri photoUri = CropImage.getActivityResult(data).getUri();
                    image = photoUri.toString();
                    presenter.uploadPhoto(photoUri);
                    firebaseAnalytics.logEvent("put_photo_OK", null);
                } else {
                    Exception exception = CropImage.getActivityResult(data).getError();
                    if (exception != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("put_photo_error_type", exception.getMessage());
                        firebaseAnalytics.logEvent("put_photo_ERROR", null);

                        // add error to analytic
                    }
                    showMessage(R.string.something_went_wrong);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        highlightConfirmButton();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void closeDialog() {
        dismiss();
    }

    @Override
    public void highlightConfirmButton() {
        highlightButton(ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    @Override
    public void showProgress() {
        progressDialog.setTitle(getString(R.string.uploading_image));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logSuccessMustInfo() {
        firebaseAnalytics.logEvent("must_info_OK", null);
    }

    private void highlightButton(int tint) {
        if (!ageSpinner.getSelectedItem().toString().equals(Consts.DEFAULT) && !image.equals(Consts.DEFAULT) &&
                !countrySpinner.getSelectedItem().toString().equals(Consts.DEFAULT)) {
            Drawable buttonDrawable = confirmButton.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, tint);
            confirmButton.setBackground(buttonDrawable);
        }
    }

}
