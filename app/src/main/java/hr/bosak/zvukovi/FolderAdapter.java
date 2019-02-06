package hr.bosak.zvukovi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolderFolder> {

    ArrayList<Folder> folderList;
    Context context;

    public FolderAdapter(ArrayList<Folder>folderList, Context context ){
        this.folderList = folderList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderFolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        ViewHolderFolder holder = new ViewHolderFolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderFolder holder, int i) {
        holder.cardSongTV.setText(folderList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return 0; //folderList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolderFolder extends RecyclerView.ViewHolder{

        TextView cardSongTV;



        public ViewHolderFolder(@NonNull View itemView) {
            super(itemView);
            cardSongTV = itemView.findViewById(R.id.cardSongTV);

        }
    }
}


