package info.jeong_ui_jeong.notes.persistence;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import info.jeong_ui_jeong.notes.models.Note;

// 노트 로컬 데이터 베이스 클래스
// 데이터 베이스에 컬럼이나 이런것들이 추가되거나 삭제될 경우에 대비해 version을 넣어준다.
@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    // 데이터 베이스 인스턴스를 만들기 위해 싱글턴 패턴 적용
    private static NoteDatabase instance;


    // 싱글턴 패턴이란 -> 객체의 인스턴스를 만드는 것.
    // 이유 -> 같은 인스턴스를 여러개 만들어서 메모리가 낭비되는 것을 막는다.

    // 싱글턴 패턴이 적용된 객체의 인스턴스를 가져오는 메소드
    static NoteDatabase getInstance(final Context context){
        // 인스턴스 데이터베이스 객체가 비어있다면
        if (instance == null){
            // 데이터베이스 객체를 인스턴스화 한다.
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    // 노트 DAO 를 가져온다.
    public abstract NoteDataAccessObject getNoteDao();


}
