package info.jeong_ui_jeong.notes;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import info.jeong_ui_jeong.notes.models.Note;
import info.jeong_ui_jeong.notes.persistence.NoteRepository;
import info.jeong_ui_jeong.notes.util.Utility;

// 노트 액티비티 클래스
public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher
{

    private static final String TAG = "노트";

    // 상수로 수정 모드 여부를 판단한다.
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;



    // ui components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;



    // vars
    private boolean mIsNewNote;

    // 처음 시작하는 노트
    private Note mInitialNote;

    // 제스쳐 디텍터
    private GestureDetector mGestureDetector;
    private int mMode;

    // 리포지토리 클래스
    private NoteRepository mNoteRepository;
    private Note mFinalNote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // 리소스 아이디 연결
        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        mNoteRepository = new NoteRepository(this);

        // 들어오는 인텐트가 있다면
        // 즉 새 노트라면
        if(getIncomingIntent()){
            // 수정 모드
            // this is a new note, (EDIT MODE)

            // 새로운 노트의 속성값들을 설정한다.
            setNewNoteProperties();
            // 수정모드로 만든다.
            enableEditMode();
        }
        else {
            // 일기 모드
            // this is NOT a new note (VIEW )

            // 노트의 속성값들을 설정한다.
            setNoteProperties();
            // 노트 내용을 수정하지 못하게 한다.
            disableContentInteraction();

        }

        // 리스너들을 설정한다.
        setListeners();



    }

    // 노트 내용에 포커스 가지 않도록 하는 메소드
    private void disableContentInteraction(){

        // 라인 에딧텍스트에 키 리스너를 없앤다.
        mLinedEditText.setKeyListener(null);
        // 포커스가 되지 않도록 한다.
        mLinedEditText.setFocusable(false);
        // 터치 포커스 없앤다.
        mLinedEditText.setFocusableInTouchMode(false);
        // 커서가 보이지 않도록 한다.
        mLinedEditText.setCursorVisible(false);
        // 포커스를 없앤다.
        mLinedEditText.clearFocus();

    }

    // 노트 내용에 포커스가 가도록 하는 메소드
    private void enableContentInteraction(){

        // 라인 에딧텍스트에 키 리스너를 설정한다.
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        // 포커스가 되지 않도록 한다.
        mLinedEditText.setFocusable(true);
        // 터치 포커스 없앤다.
        mLinedEditText.setFocusableInTouchMode(true);
        // 커서가 보이지 않도록 한다.
        mLinedEditText.setCursorVisible(true);
        // 포커스를 요청한다.
        mLinedEditText.requestFocus();

    }



    // 수정 모드가 되도록 하는 메소드
    private void enableEditMode(){
        // 뒤로가기 버튼이 보이지 않도록 한다.
        mBackArrowContainer.setVisibility(View.GONE);
        // 체크 버튼이 보이도록 한다.
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        // 모드를 설정한다.
        mMode = EDIT_MODE_ENABLED;

        // 내용도 수정할 수 있도록 한다.
        enableContentInteraction();

    }



    // 읽기 모드가 되도록 하는 메소드
    private void disableEditMode(){
        // 뒤로가기 버튼이 보이도록 한다.
        mBackArrowContainer.setVisibility(View.VISIBLE);
        // 체크 버튼이 보이지 않도록 한다.
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        // 모드를 설정한다.
        mMode = EDIT_MODE_DISABLED;

        // 내용을 수정할 수 없도록 만든다.
        disableContentInteraction();

        // final 노트가 initail 노트와 다른지 여부 확인
        String temp = mLinedEditText.getText().toString();
        // 줄바꿈을 비운다.
        temp = temp.replace("\n", "");
        // 스페이스를 비운다.
        temp = temp.replace(" ","");

        // 글 내용이 있으면
        if(temp.length() > 0){
            // note 모델 인스턴스 설정
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mFinalNote.setTimestamp(timestamp);

            // 노트에 변경사항이 있다면
            // 노트 제목도 다르고, 내용도 다르다면

            if(!mFinalNote.getContent().equals(mInitialNote.getContent()) || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){

                Log.d(TAG, "disableEditMode: mFinalNote.getContent() " + mFinalNote.getContent());
                Log.d(TAG, "disableEditMode: mInitialNote.getContent() " + mInitialNote.getContent());

                Log.d(TAG, "disableEditMode: mFinalNote.getTitle() " + mFinalNote.getTitle());
                Log.d(TAG, "disableEditMode: mInitialNote.getTitle() " + mInitialNote.getTitle());

                // 변경사항을 저장한다.
                saveChanges();
            }
//            !mFinalNote.getTitle().equals(mInitialNote.getTitle())

        }

    }


    // 소프트 키보드를 숨기는 메소드
    private void hideSoftKeyboard(){
        // 인풋 메소드 메니져를 가져온다.
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // 현재 뷰를 가져온다.
        View view = this.getCurrentFocus();

        // 뷰가 비어있다면
        if(view == null){
            view = new View(this);
        }

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }



    // 리스너들을 설정하는 메소드
    private void setListeners(){

        // 라인 에딧텍스트에 터치 리스터를 붙인다.
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);

        // 제스쳐 디텍터랑 라인 애딧 텍스트를 연결해준다. 왜? 서로 모르기 때문이다.

        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);

        // 뒤 화살표 버튼에 온 클릭 리스너를 설정한다.
        mBackArrow.setOnClickListener(this);

        // 에딧텍스트 타이틀에 텍스트체인지 리스너를 설정한다.
        mEditTitle.addTextChangedListener(this);

    }



    // 인텐트를 가져오는 메소드
    private boolean getIncomingIntent(){
        // 넘어왔다면
        if(getIntent().hasExtra("selected_note")){
            // 인텐트에서 인스턴스를 가져온다.
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            mFinalNote.setId(mInitialNote.getId());

//            mFinalNote = getIntent().getParcelableExtra("selected_note");
//            Log.d(TAG, "getIncomingIntent: " + mInitialNote.toString());

            // 모드를 설정한다.
            mMode = EDIT_MODE_DISABLED;

            mIsNewNote = false;
            return false;
        }

        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    // 변경사항을 저장하는 메소드
    private void saveChanges(){
        // 새노트이면
        if(mIsNewNote){
            // 새노트를 저장한다.
            saveNewNotes();
        }
        else { // 새노트가 아니면
            // 노트를 업데이트한다.
            updateNote();
        }

    }

    // 노트를 업데이트하는 메소드
    private void updateNote(){
        mNoteRepository.updateNote(mFinalNote);
    }


    // 새 노트를 저장하는 메소드
    private void saveNewNotes(){
        // 룸 데이터 베이스에 마지막으로 변경된 노트를 삽입한다.
        mNoteRepository.insertNoteTask(mFinalNote);

    }


    // 기존 노트 속성값들을 설정하는 메소드
    private void setNoteProperties(){

        mViewTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mLinedEditText.setText(mInitialNote.getContent());

    }


    // 새로운 노트 속성값들을 설정하는 메소드
    private void setNewNoteProperties(){

        mViewTitle.setText("노트 제목");
        mEditTitle.setText("노트 제목");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("노트 제목");
        mFinalNote.setTitle("노트 제목");

    }

    // 터치가 이루어질때
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

//        Log.d(TAG, "onTouch: 터치되었다.");

        // 제스쳐 디텍터에 모션이벤트 반환한다.
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: 더블탭!");

        // 수정 모드로 변경한다.
        enableEditMode();

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    // 클릭을 했을때
    @Override
    public void onClick(View view) {

        // 클릭된 뷰의 아이디가
        switch(view.getId()){

            // 툴바 체크버튼이라면
            case R.id.toolbar_check:{
                // 소프트 키보드를 숨긴다.
                hideSoftKeyboard();
                // 읽기모드로 변경한다.
                disableEditMode();
                break;
            }
            // 툴바 체크버튼이라면
            case R.id.note_text_title:{
                // 수정 모드로 변경한다.
                enableEditMode();
                // 초점을 요청한다.
                mEditTitle.requestFocus();
                // 글의 제일 마지막으로 커서 위치를 설정한다.
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
            // 툴바 체크버튼이라면
            case R.id.toolbar_back_arrow:{
                // 해당 액티비티를 종료한다.
                finish();
                break;
            }

        }

    }

    // 뒤로가기 버튼이 눌러졌을 때
    @Override
    public void onBackPressed() {
        // 수정 모드이면
        if(mMode == EDIT_MODE_ENABLED){

            // 온클릭 메소드와 같이 행동한다.
            // 즉 체크버튼을 누른것과 동일하게 행동한다.
            onClick(mCheck);
        }
        else {
            super.onBackPressed();
        }
    }

    // 화면 회전이 이루어질때 기존 액티비티는 없어지고 다시 뷰를 그리는 것이다.

    // 액티비티가 paused 되었을 때
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 모드를 저장해 둔다.
        outState.putInt("mode", mMode);

    }

    //
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // 모드를 가져온다.
        mMode = savedInstanceState.getInt("mode");

        // 편집 모드 이라면
        if(mMode == EDIT_MODE_ENABLED){
            // 편집모드로 설정한다.
           enableEditMode();
        }


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 뷰 타이틀 변경
        mViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
} // NoteActivity
