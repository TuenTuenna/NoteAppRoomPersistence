package info.jeong_ui_jeong.notes;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import info.jeong_ui_jeong.notes.adapters.NotesRecyclerAdapter;
import info.jeong_ui_jeong.notes.models.Note;
import info.jeong_ui_jeong.notes.persistence.NoteRepository;
import info.jeong_ui_jeong.notes.util.VerticalSpacingItemDecorator;

public class NotesListActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener
{

    private static final String TAG = "노트";



    // Ui components
    private RecyclerView mRecyclerView;


    // vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    // 노트 리포지토리 객체 선언
    private NoteRepository mNoteRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 플로팅 액션버튼에 온 클릭 메소드를 붙인다.
        findViewById(R.id.floatingActionButton).setOnClickListener(this);


//        Log.d(TAG, "onCreate: ");
//
//        // 빈 생성자
//        Note note = new Note("some title", "some content", "some timestamp");
//
////        Note note2 = new Note();
////        note2.setTitle("some other title");
////        note2.setContent("some other content");
////        note2.setTimestamp("");
//
//        Log.d(TAG, "onCreate: my note: "+ note.toString());

        // 노트 리포지토리 객체 인스턴스화
        mNoteRepository = new NoteRepository(this);

        // 리사이클러뷰를 설정한다.
        initRecyclerView();
        // 데이터를 가져온다.
        retrieveNotes();


//        insertFakeNotes();

//        Log.d(TAG, "onCreate: thread: " + Thread.currentThread().getName());
//        mNoteRepository.insertNoteTask(new Note());

        // 툴바 리소스 아이디 연결
        Toolbar toolbar = (Toolbar)findViewById(R.id.notes_toolbar);
        // 커스텀 툴바을 설정한다.
        setSupportActionBar(toolbar);
        setTitle("의정 노트");

    }

    // 모든 노트를 가져오는 메소드
    private void retrieveNotes(){
        // 노트 리포지토리에서 노트를 가져온다.
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {

            // 데이터베이스 라이브데이터에 변화가 감지되면
            // 해당 메소드가 발동된다.
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // 변화가 있을때 마다 메모리스택에 올려져있던 어레이리스트를 지우고
                // 다시 새로 가져온다.

                // 노트가 있으면
                if(mNotes.size() > 0){
                    // 노트를 지운다.
                    mNotes.clear();
                }

                // 룸 데이터 베이스에서 가져온 notes 가 비어있지 않으면
                if(notes != null){
                    //모든 notes를 mNotes에 추가한다.
                    mNotes.addAll(notes);
                }

                // 리사이클러뷰 어뎁터에 데이터가 변경되었다고 알려준다.
                mNotesRecyclerAdapter.notifyDataSetChanged();
                // 리사이클러뷰 위치 맨 마지막으로 옮기기
                mRecyclerView.scrollToPosition(mNotes.size()-1);


            }
        });


    }


    // 리사이클러뷰를 만드는 메소드
    private void initRecyclerView(){

        // 리니어 레이아웃 메니져
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 아이템 데코레이터를 추가한다.
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

        // 리사이클러뷰에 아이템 터치 핼퍼 인스턴스를 붙인다.
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNotesRecyclerAdapter);


    }

    // 더미 데이터를 넣는 메소드
    private void insertFakeNotes(){

        for(int i = 0; i < 1000; i++){
            Note note = new Note();
            note.setTitle("title # " + i);
            note.setContent("content #: " + i);
            note.setTimestamp("Jan 2019");
            mNotes.add(note);
        }

        // 리사이클러뷰 어답터에 데이터가 변경되었다고 알려준다.
        mNotesRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
    }

    // 노트가 클릭이 되어졌을때
    @Override
    public void onNoteClick(int position) {

//        // 선택된 노트를 가져온다.
        Note note = mNotes.get(position);

        // 액티비티를 띄운다.
        Intent intent = new Intent(this, NoteActivity.class);
//        intent.putExtra("note_object",note.toString());

        // 인텐트에 담에서 선택된 객체를 보낸다.
        intent.putExtra("selected_note", note);

        startActivity(intent);

        Log.d(TAG, "onNoteClick: clicked! position: "+position);



        // Parcelable
        // 어레이 리스트 같은 큰 파일은 번들에 넣을수 없다. - 성능에 좋지 않다.


        
    }


    @Override
    public void onClick(View v) {
        // 새로운 노트 액티비티를 연다
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    // 해당 노트를 어레이 리스트에서 지우는 메소드
    private void deleteNote(Note note){

        // 노트 어레이 리스트에서 매개변수로 들어온 노트를 지운다.
        mNotes.remove(note);
        // 리사이클러뷰 어답터에 데이터가 변경되었다고 알려준다.
        mNotesRecyclerAdapter.notifyDataSetChanged();

        // 룸 데이터 베이스에 해당 노트를 삭제한다.
        mNoteRepository.deleteNote(note);

    }


    // 리사이클러뷰 아이템 스와이프 하기 위한 아이템 터치 핼퍼 클래스
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        // 움직일때
        // 재정렬 할때 사용
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        // 스와이프할때
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            // 스와이프한 노트를 가져온다.
            Note noteToBeDeleted = mNotes.get(viewHolder.getAdapterPosition());
            // 스와이프한 노트를 지운다.
            deleteNote(noteToBeDeleted);
        }
    };
















} // NotesListActivity
