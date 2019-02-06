package hr.bosak.zvukovi;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Locale;


public class FolderFragment extends Fragment {

    ArrayList<Folder> foldersList;
    RecyclerView folderRecyclerViewRV;
    View view;
    File[] files;


    public FolderFragment() {

    }


    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        //loadAudio();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String root_sd = Environment.getExternalStorageDirectory().toString() + "/Music/";

        view = inflater.inflate(R.layout.fragment_folder, container, false);
        initWidgets();
        FolderAdapter adapter = new FolderAdapter(foldersList, getContext().getApplicationContext());
        folderRecyclerViewRV.setAdapter(adapter);
        folderRecyclerViewRV.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void initWidgets(){
        folderRecyclerViewRV = view.findViewById(R.id.folderRecycleViewRV);
    }


    public void loadAudio(){

        ContentResolver contentResolver = getContext().getContentResolver();

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/Music/");



        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            foldersList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));




                foldersList.add(new Folder(title));
            }
        }
        cursor.close();
    }

}
