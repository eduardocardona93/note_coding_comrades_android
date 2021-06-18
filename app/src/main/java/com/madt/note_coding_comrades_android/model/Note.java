package com.madt.note_coding_comrades_android.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "note", foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "cat_id",
        childColumns = "noteCategoryId",
        onDelete = CASCADE))
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;

    @NonNull
    private int noteCategoryId;

    @NonNull
    @ColumnInfo(name = "note_name")
    private String noteName;

    @NonNull
    @ColumnInfo(name = "note_detail")
    private String noteDetail;

    @ColumnInfo(name = "note_image", typeAffinity = ColumnInfo.BLOB)
    private byte[] noteImage;

    @ColumnInfo(name = "note_recording_path")
    private String noteRecordingPath;

    @NonNull
    @ColumnInfo(name = "note_latitude")
    private Double noteLatitude;

    @NonNull
    @ColumnInfo(name = "note_longitude")
    private Double noteLongitude;

    
    public Note(){}

    public Note(@NonNull int noteCategoryId, @NonNull String noteName, @NonNull String noteDetail, byte[] noteImage, String noteRecordingPath,@NonNull Double noteLatitude,@NonNull Double noteLongitude) {
        this.noteCategoryId = noteCategoryId;
        this.noteName = noteName;
        this.noteDetail = noteDetail;
        this.noteImage = noteImage;
        this.noteRecordingPath = noteRecordingPath;
        this.noteLatitude = noteLatitude;
        this.noteLongitude = noteLongitude;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteCategoryId() {
        return noteCategoryId;
    }

    public void setNoteCategoryId(int noteCategoryId) {
        this.noteCategoryId = noteCategoryId;
    }

    @NonNull
    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(@NonNull String noteName) {
        this.noteName = noteName;
    }

    @NonNull
    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(@NonNull String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public byte[] getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(byte[] noteImage) {
        this.noteImage = noteImage;
    }

    public String getNoteRecordingPath() {
        return noteRecordingPath;
    }

    public void setNoteRecordingPath(String noteRecordingPath) {
        this.noteRecordingPath = noteRecordingPath;
    }

    public Double getNoteLatitude() {
        return noteLatitude;
    }

    public void setNoteLatitude(Double noteLatitude) {
        this.noteLatitude = noteLatitude;
    }

    public Double getNoteLongitude() {
        return noteLongitude;
    }

    public void setNoteLongitude(Double noteLongitude) {
        this.noteLongitude = noteLongitude;
    }


    @Override
    public String toString() {
        return noteName;
    }
}
