package hr.bosak.zvukovi;

import android.view.View;

public interface RecycleViewItemClickListener {

    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
