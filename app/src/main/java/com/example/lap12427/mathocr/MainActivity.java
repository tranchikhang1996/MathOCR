package com.example.lap12427.mathocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import utils.Constants;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_result)
    TextView mResult;
    @BindView(R.id.image)
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Optional
    @OnClick(R.id.btn_choose_image)
    public void onChooseImageClick(View v) {
        startOpenGallery();
    }

    void startOpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),Constants.REQUEST_OPEN_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_OPEN_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        handleAfterPickingImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void handleAfterPickingImage(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        mImage.setImageBitmap(bitmap);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    mResult.setText(firebaseVisionText.getText());
                })
                .addOnFailureListener(e -> {
                    mResult.setText(e.getMessage());
                });
    }
}
