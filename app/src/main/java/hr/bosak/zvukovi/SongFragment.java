package hr.bosak.zvukovi;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class SongFragment extends Fragment {

    ArrayList<Audio> audioList;
    RecyclerView recyclerViewRV;
    boolean isBackgroundFirst;
    boolean isBackgroundSecond;
    boolean isBackgroundThird;

    View view;


    public Intent intent;

    public static final String POSITION = "position";
    public static final String COVER = "cover";
    public static final String COVER2 = "cover2";

    public static final int REQUEST_CODE = 0;



    public SongFragment() {

    }


    public static SongFragment newInstance(String param1, String param2) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        loadAudio();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        initWidgets();
        SongAdapter adapter = new SongAdapter(audioList, getContext().getApplicationContext());
        recyclerViewRV.setAdapter(adapter);
        recyclerViewRV.setLayoutManager(new LinearLayoutManager(getContext()));
        setUpRecycleView();

        return view;
    }

    public void initWidgets(){
        recyclerViewRV = view.findViewById(R.id.recycleViewRV);
        //songsFragmentLL = view.findViewById(R.id.songsFragmentLL);
    }

    public void loadAudio(){

        ContentResolver contentResolver = getContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if(cursor != null && cursor.getCount() > 0){
            audioList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();
    }

    public void setUpRecycleView() {
        recyclerViewRV.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), recyclerViewRV,
                new RecycleViewItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MainActivity getVariable = (MainActivity) getActivity();
                        isBackgroundFirst = getVariable.firstBackground;
                        isBackgroundSecond = getVariable.secondBackground;
                        isBackgroundThird = getVariable.thirdBackground;
                        intent = new Intent(getActivity(), PlayActivity.class);
                        intent.putExtra(POSITION, position);

                        if (isBackgroundSecond){
                            intent.putExtra(COVER,"");
                        } else if(isBackgroundThird){
                            intent.putExtra(COVER2, "");
                        }
                        startActivityForResult(intent, REQUEST_CODE);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        String info = audioList.get(position).getArtist() + ", " + audioList.get(position).getAlbum();
                        Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
                    }

                }));

    }


}
