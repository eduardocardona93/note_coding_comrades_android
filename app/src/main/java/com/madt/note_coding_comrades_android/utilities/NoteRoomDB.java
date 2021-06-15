package com.madt.note_coding_comrades_android.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.madt.note_coding_comrades_android.data.NoteDao;
import com.madt.note_coding_comrades_android.model.Category;
import com.madt.note_coding_comrades_android.model.Note;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Note.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDB extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile NoteRoomDB INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecuter = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NoteRoomDB getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (NoteRoomDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDB.class, "note_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecuter.execute(()-> {
                         NoteDao noteDao = INSTANCE.noteDao();
                         noteDao.deleteAllNotes();
                    });
                }
            };
}
