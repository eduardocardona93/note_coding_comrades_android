package com.madt.note_coding_comrades_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.madt.note_coding_comrades_android.R;

public class NoteListActivity extends AppCompatActivity {

    ImageView createNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        createNote = findViewById(R.id.createNote);

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), NoteDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}