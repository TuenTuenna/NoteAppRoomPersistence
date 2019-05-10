package info.jeong_ui_jeong.notes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import info.jeong_ui_jeong.notes.R;
import info.jeong_ui_jeong.notes.models.Note;
import info.jeong_ui_jeong.notes.util.Utility;

// 리사이클러뷰 어답터 클래스
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder>{

    private static final String TAG = "노트";

    // 데이터 구조
    private ArrayList<Note> mNotes = new ArrayList<>();

    private OnNoteListener mOnNoteListener;

    // 어답터 생성자 메소드
    public NotesRecyclerAdapter(ArrayList<Note> mNotes, OnNoteListener onNoteListener) {
        this.mNotes = mNotes;
        this.mOnNoteListener = onNoteListener;
    }

    // 뷰가 생성되어질 때
    // 아이템 레이아웃 연결
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_list_item, viewGroup, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    // 리소스 아이디 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        
        try{
            // 앞에 2글자만 가져온다. 01 -> 1월
            String month = mNotes.get(i).getTimestamp().substring(0,2);
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(i).getTimestamp().substring(3); // 3번째 부터 끝까지 가져오기
            String timestamp = month + " " + year;
            viewHolder.timestamp.setText(timestamp);
            viewHolder.title.setText(mNotes.get(i).getTitle());
        } catch(NullPointerException e){
            Log.e(TAG, "onBindViewHolder: NullpointerException: "+ e.getMessage());
        }

        // 온바인드 뷰 홀더에서도 온 클릭 리스너를 달수가 있지만 성능에 좋지 않다.

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }



    // 각 아이템
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, timestamp;
        OnNoteListener onNoteListener;

        // 뷰홀더 생성자메소드
        // 리소스 아이디 연결
        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_title);
            timestamp = (TextView) itemView.findViewById(R.id.note_timestamp);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // 인터페이스 메소드를 발동시킨다.
            // getAdapterPosition() 어답터의 현재 위치를 알수 있다. 즉 선택된 아이템 위치
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

    // 리사이클러뷰 아이템에 클릭 리스너를 달기 위해 인터페이스를 정의한다.
    public interface OnNoteListener {
        // 노트가 클릭되었을때
        void onNoteClick(int position);
    }


} // NotesRecyclerAdapter 클래스
