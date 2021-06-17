package com.madt.note_coding_comrades_android.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.madt.note_coding_comrades_android.R;

public class AddEditNoteActivity extends AppCompatActivity {

    EditText edit_noteTitle, edit_noteDetail;
    ImageView edit_uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        edit_noteTitle = findViewById(R.id.edit_noteTitle);
        edit_noteDetail = findViewById(R.id.edit_noteDetail);
        edit_uploadImage = findViewById(R.id.edit_uploadImage);

        edit_noteTitle.setText(getIntent().getStringExtra("note_name"));
        edit_noteDetail.setText(getIntent().getStringExtra("note_detail"));

        byte[] imageByteArray = getIntent().getByteArrayExtra("note_image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        edit_uploadImage.setImageBitmap(bitmap);
    }
}