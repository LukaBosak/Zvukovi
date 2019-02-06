package hr.bosak.zvukovi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class HorizontalSlider extends ProgressBar {

    private OnProgressChangeListener listener;


    public HorizontalSlider(Context context) {
        super(context);
    }

    public HorizontalSlider(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.progressBarStyleHorizontal);
    }

    public HorizontalSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float width = getWidth();

            int progress = Math.round((float) getMax() * (x / width));
            if(progress < 0) progress = 0;
            if(progress > 100) progress = 100;

            setProgress(progress);

            if(listener != null){
                listener.onProgressChange(this, progress);
            }
        }
        return true;
    }

    public void setListener(OnProgressChangeListener listener){
        this.listener = listener;
    }
}
