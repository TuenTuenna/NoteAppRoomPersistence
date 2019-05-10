package info.jeong_ui_jeong.notes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Update;
import android.content.Context;

import java.util.List;

import info.jeong_ui_jeong.notes.async.DeleteAsyncTask;
import info.jeong_ui_jeong.notes.async.InsertAsyncTask;
import info.jeong_ui_jeong.notes.async.UpdateAsyncTask;
import info.jeong_ui_jeong.notes.models.Note;

// 룸 로컬 데이터베이스와 액티비티 혹은 프래그먼트 간 데이터가 교류 되도록 해주는 레포지토리 클래스
public class NoteRepository {

    // 데이터 베이스
    private NoteDatabase mNoteDatabase;

    // 생성자 메소드
    // 데이터 베이스를 시작하려면 매개변수에 context 를 넣어줘야한다.
    public NoteRepository(Context context) {
        // 노트데이터 베이스 클래스에서 싱글턴 패턴으로 만든 getInstance 메소드로 인스턴스를 만든다.
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    // 삽입 처리 메소드 INSERT
    public void insertNoteTask(Note note){

        // 에이싱크 테스크를 새로 만들고 실행한다.
        // 노트 하나의 배열을 넣었다. ... -> 배열 []과 동일
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);

    }

    // 갱신 메소드 UPDATE
    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    // 조회 메소드 GET
    public LiveData<List<Note>> retrieveNotesTask(){

        // 데이터 베이스에 접근 -> DAO를 가져온다. -> 노트를 가져온다.(쿼리)
        return mNoteDatabase.getNoteDao().getNotes();

    }

    // 삭제 메소드 DELETE
    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }















} // NoteRepository 클래스
