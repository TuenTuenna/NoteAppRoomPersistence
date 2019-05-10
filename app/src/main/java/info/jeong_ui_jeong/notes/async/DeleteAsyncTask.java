package info.jeong_ui_jeong.notes.async;

import android.os.AsyncTask;
import android.util.Log;

import info.jeong_ui_jeong.notes.models.Note;
import info.jeong_ui_jeong.notes.persistence.NoteDataAccessObject;


// 룸 데이터베이스에 삽입 처리를 하는 async task 클래스
// public abstract class AsyncTask<Params, Progress, Result>
public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG = "노트";

    // 노트 dao
    private NoteDataAccessObject mNoteDao;

    // 기본 생성자 메소드
    public DeleteAsyncTask(NoteDataAccessObject dao) {
        mNoteDao = dao;
    }

    // 백그라운드 쓰레드에서 이뤄지는 작업
    @Override
    protected Void doInBackground(Note... notes) {

        Log.d(TAG, "doInBackground: 쓰레드: " + Thread.currentThread().getName());

        // dao 에 쿼리 정의 해둔 delete 를 부른다.
        mNoteDao.delete(notes);

        return null;
    }


}
