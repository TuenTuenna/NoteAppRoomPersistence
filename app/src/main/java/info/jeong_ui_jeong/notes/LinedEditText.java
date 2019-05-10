package info.jeong_ui_jeong.notes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

// 커스텀 에딧 텍스트 클래스
// EditText 를 익스텐즈 해서 사용할 수도 있지만
// AppCompatEditText를 사용하는 이유는
// 보다 훨씬 버전호환이 잘되기 때문이다.

public class LinedEditText extends AppCompatEditText {

    // 렉트 객체
    private Rect mRect;

    // 페인트 객체
    private Paint mPaint;

    // 커스텀 뷰를 사용할때는 AttributeSet 이 들어간 생성자 메소드를 사용해야 한다.
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //
        mRect = new Rect();
        mPaint = new Paint();
        // 페인트의 스타일을 정한다.
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFF00B7A8);

    }

    // 전체 줄 이 몇줄인지 구한다.

    // 뷰를 그릴때
    @Override
    protected void onDraw(Canvas canvas) {

        // 뷰의 전체 높이를 가져온다.
        // 에딧 텍스트의 부모의 높이를 가져온다.
        int height = ((View)this.getParent()).getHeight();

        // 한 줄의 길이를 가져온다.
        int lineHeight = getLineHeight();

        // 뷰의 전체 높이에서 한줄의 길이를 나는다.
        // 즉 총 그릴 줄의 갯수가 나온다.
        int numberOfLines = height / lineHeight;

        // 렉트 객체와 패인트 객체를 가져온다.
        Rect rect = mRect;
        Paint paint = mPaint;

        // 베이스 라인을 가져온다.
        // 여기서 베이스 라인은 선을 그을 출발점이다.
        int baseline = getLineBounds(0, rect);

        for(int i = 0; i < numberOfLines; i++) {

            // 캔버스로 그린다.
            canvas.drawLine(rect.left, baseline + 10, rect.right, baseline + 10, paint);

            baseline += lineHeight;

        }



        super.onDraw(canvas);
    }



















} // LinedEditText 클래스
