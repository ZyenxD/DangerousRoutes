package com.personal.neycasilla.dangerousroutes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.neycasilla.dangerousroutes.R;
import com.personal.neycasilla.dangerousroutes.model.Comments;

import java.util.List;

/**
 * Created by Ney Casilla on 8/18/2017.
 */

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder> {

    private Context context;
    private List<Comments> comentses;

    public ZoneAdapter(Context context, List<Comments> commentsList) {
        this.context = context;
        this.comentses = commentsList;
    }

    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        ZoneViewHolder holder = new ZoneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {
        if(comentses!= null){
            holder.textViewDate.setText(comentses.get(position).getDateMade().toString());
            holder.textViewDescription.setText(comentses.get(position).getCommentText());
        }
    }

    @Override
    public int getItemCount() {
        if(comentses!= null) {
            return comentses.size();
        }
        return 0;
    }

    public void addItem(Comments comment){
        if(!this.comentses.contains(comment)){
            this.comentses.add(comment);
        }
        this.notifyDataSetChanged();
    }
    public class ZoneViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDescription;
        private TextView textViewDate;
        public ZoneViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textview_message);
            textViewDate = itemView.findViewById(R.id.textview_date);

        }
    }
}
