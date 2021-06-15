package com.madt.note_coding_comrades_android.model;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryHavingNotes {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "cat_id",
            entityColumn = "note_category_id"
    )
    public List<Note> notes;
}
