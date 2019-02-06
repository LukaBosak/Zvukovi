package hr.bosak.zvukovi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    ArrayList<Audio> songList;
    Context context;

    public SongAdapter(ArrayList<Audio> songList, Context context){
        this.songList = songList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.cardSongTV.setText(songList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cardSongTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSongTV = itemView.findViewById(R.id.cardSongTV);

        }
    }


}
