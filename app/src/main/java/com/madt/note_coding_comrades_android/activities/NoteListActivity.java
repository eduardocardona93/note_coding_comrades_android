package com.madt.note_coding_comrades_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madt.note_coding_comrades_android.R;
import com.madt.note_coding_comrades_android.model.Category;
import com.madt.note_coding_comrades_android.model.Note;
import com.madt.note_coding_comrades_android.model.NoteAppViewModel;
import com.madt.note_coding_comrades_android.utilities.NoteUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NoteListActivity extends AppCompatActivity {

    ImageView createNote;
    RecyclerView rcNotes;
    private NoteAppViewModel noteAppViewModel;
    ArrayList<Note> noteList = new ArrayList<>();

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

        noteAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(NoteAppViewModel.class);



    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAppViewModel.getAllNotes().observe(this, notes -> {
            noteList.clear();
            noteList.addAll(notes);

            NoteUtils.showLog("list size",noteList.size()+"");
            NoteUtils.showLog("db list size",noteList.size()+"");

            rcNotes = findViewById(R.id.rcNotes);
            rcNotes.setAdapter(new NoteAdapter(this, noteList));
        });
    }

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

        private Activity activity;
        private ArrayList<Note> noteList;

        NoteAdapter(Activity activity, ArrayList<Note> noteList) {
            this.activity = activity;
            this.noteList = noteList;
            NoteUtils.showLog("adpter list size",noteList.size()+"");
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_notes, parent, false);

            return new NoteAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteListActivity.NoteAdapter.ViewHolder holder, int position) {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy ");
            String strDate = dateFormat.format(date);
            System.out.println("Converted String: " + strDate);

            holder.noteTitle.setText(noteList.get(position).getNoteName());
            holder.categoryName.setText("Catepgry");
            holder.noteCreationDate.setText(strDate);
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView noteTitle;
            TextView categoryName;
            TextView noteCreationDate;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                noteTitle = itemView.findViewById(R.id.noteTitle);
                categoryName = itemView.findViewById(R.id.categoryName);
                noteCreationDate = itemView.findViewById(R.id.noteCreationDate);
            }
        }
    }

}