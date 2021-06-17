package com.madt.note_coding_comrades_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madt.note_coding_comrades_android.R;
import com.madt.note_coding_comrades_android.model.Note;
import com.madt.note_coding_comrades_android.model.NoteAppViewModel;
import com.madt.note_coding_comrades_android.utilities.NoteUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NoteListActivity extends AppCompatActivity {

    ImageView createNote;
    RecyclerView rcNotes;
    TextView sortAZ, sortZA, sortDate;

    private NoteAppViewModel noteAppViewModel;
    ArrayList<Note> noteList = new ArrayList<>();
    ArrayList<Note> filteredList = new ArrayList<>();
    public static final String CATEGORY_ID = "cate_id";
    SearchView searchView;
    private NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        createNote = findViewById(R.id.createNote);
        searchView = findViewById(R.id.searchView);
        rcNotes = findViewById(R.id.rcNotes);

        // Setup Recycler View Layout Manager
        rcNotes.setHasFixedSize(true);
        rcNotes.setLayoutManager(new LinearLayoutManager(this));

        createNote.setOnClickListener(v -> {

            Intent intent = new Intent(getBaseContext(), NoteDetailActivity.class);
            intent.putExtra(NoteListActivity.CATEGORY_ID,getIntent().getIntExtra(NoteListActivity.CATEGORY_ID, 0));
            startActivity(intent);
        });

        findViewById(R.id.imgBack).setOnClickListener(v -> {
          finish();
        });

        sortAZ = findViewById(R.id.sortAZ);
        sortZA = findViewById(R.id.sortZA);
        sortDate = findViewById(R.id.sortDate);

        sortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Note> compareByName = (Note n1, Note n2) ->
                        n1.getNoteName().compareTo( n2.getNoteName() );

                Collections.sort(noteList, compareByName);
                noteAdapter = new NoteAdapter(NoteListActivity.this, noteList);
                rcNotes.setAdapter(noteAdapter);
            }
        });

        sortZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Note> compareByName = (Note n1, Note n2) ->
                        n1.getNoteName().compareTo( n2.getNoteName() );

                Collections.sort(noteList, compareByName.reversed());
                noteAdapter = new NoteAdapter(NoteListActivity.this, noteList);
                rcNotes.setAdapter(noteAdapter);
            }
        });

        noteAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(NoteAppViewModel.class);


    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  noteAppViewModel.getAllNotes().observe(this, notes -> {
            noteList.clear();
            noteList.addAll(notes);

            NoteUtils.showLog("list size", noteList.size() + "");
            NoteUtils.showLog("db list size", noteList.size() + "");

            noteAdapter = new NoteAdapter(this, noteList);
            rcNotes.setAdapter(noteAdapter);
        });*/
        noteAppViewModel.getNotesByCategory(getIntent().getIntExtra(NoteListActivity.CATEGORY_ID, 0)).observe(this, notes -> {
            noteList.clear();
            noteList.addAll(notes);

            NoteUtils.showLog("list size", noteList.size() + "");
            NoteUtils.showLog("db list size", noteList.size() + "");

            noteAdapter = new NoteAdapter(this, noteList);
            rcNotes.setAdapter(noteAdapter);
        });
        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Calling a method to filter Note List
                filter(newText);
                return false;
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcNotes);

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (Note item : noteList) {
            // checking if the entered string matched with first name or last name  of contact list
            if (item.getNoteName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched , adding it to filtered contact list
                filteredList.add(item);
            }
        }


        // passing that filtered list to adapter class.
        noteAdapter.filterList(filteredList);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            NoteUtils.showLog("in", "swipe");
            NoteUtils.showLog("in", "swipe : " + getIntent().getIntExtra(NoteListActivity.CATEGORY_ID, 0));
            int position = viewHolder.getAdapterPosition();
            /*final Note[] contact = new Note[1];
            noteAppViewModel.getNotesByCategory(getIntent().getIntExtra(NoteListActivity.CATEGORY_ID, 0)).observe(this, notes -> {
                contact[0] = notes.get(position);
            });*/

            Note contact;
            if(filteredList.size() == 0){
                contact = noteList.get(position);
            } else {
                contact = filteredList.get(position);
            }
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    // confirmation dialog to ask user before delete contact
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteListActivity.this);
                    builder.setTitle("Are you sure you want to delete this contact?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {

                        noteAppViewModel.delete(contact);

                    });
                    builder.setNegativeButton("No", (dialog, which) -> noteAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setIconHorizontalMargin(1, 1)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

        private Activity activity;
        private ArrayList<Note> noteList;

        NoteAdapter(Activity activity, ArrayList<Note> noteList) {
            this.activity = activity;
            this.noteList = noteList;
            NoteUtils.showLog("adpter list size", noteList.size() + "");
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_notes, parent, false);

            return new NoteAdapter.ViewHolder(view);
        }

        // method for filtering our recyclerview items.
        public void filterList(ArrayList<Note> filterList) {
            // add filtered list in adapter list
            this.noteList = filterList;
            // notify adapter changes in list
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull NoteListActivity.NoteAdapter.ViewHolder holder, int position) {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
            String strDate = dateFormat.format(date);
            System.out.println("Converted String: " + strDate);

            holder.noteTitle.setText(noteList.get(position).getNoteName());
            holder.categoryName.setText("Catepgry");
            holder.noteCreationDate.setText(strDate);

            holder.bind(noteList.get(position));
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

            public void bind(Note note) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = new Intent(NoteListActivity.this, AddEditNoteActivity.class);
                        intent.putExtra("note_id", note.getNoteId());
                        intent.putExtra("noteCategoryId", note.getNoteCategoryId());
                        intent.putExtra("note_name", note.getNoteName());
                        intent.putExtra("note_detail", note.getNoteDetail());
                        intent.putExtra("note_image", note.getNoteImage());
                        startActivity(intent);
                    }
                });
            }

        }
    }

}