package hr.bosak.zvukovi;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    Context context;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return SongFragment.newInstance("","");
            case 1:
                return FolderFragment.newInstance("","");
        }
        return SongFragment.newInstance("","");
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return context.getString(R.string.songs);
        }
        else if(position == 1){
            return context.getString(R.string.folders);
        }
        return "";
    }
}
