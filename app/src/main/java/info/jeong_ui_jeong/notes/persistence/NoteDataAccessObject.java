package info.jeong_ui_jeong.notes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import info.jeong_ui_jeong.notes.models.Note;

// 노트 데이터 접근 객체 인터페이스
@Dao
public interface NoteDataAccessObject {

    // 삽입
    @Insert
    //Note... "..."을 붙여서 노트 배열 []이것과 같다
    long[] insertNotes(Note... notes);

    // 조회
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Note> getNotesWithCustomQuery(String title);

//    "Elizebeth"
//    getNotesWithCustomQuery("eli*")


    // 삭제
    @Delete
    int delete(Note... notes);

    // 갱신
    @Update
    int update(Note... notes);




}
