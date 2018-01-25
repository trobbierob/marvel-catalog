package com.example.android.marvelcatalog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarvelAdapter extends RecyclerView.Adapter<MarvelAdapter.MarvelViewHolder>{

    private static final String TAG = MarvelAdapter.class.getSimpleName();

    private int mNumberItems;

    public MarvelAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @Override
    public MarvelViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.character_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MarvelViewHolder viewHolder = new MarvelViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MarvelViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class MarvelViewHolder extends RecyclerView.ViewHolder {

        public MarvelViewHolder(View itemView) {
            super(itemView);
        }
    }

}