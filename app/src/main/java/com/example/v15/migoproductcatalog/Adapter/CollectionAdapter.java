package com.example.v15.migoproductcatalog.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.v15.migoproductcatalog.Model.Books;
import com.example.v15.migoproductcatalog.Model.Movies;
import com.example.v15.migoproductcatalog.Model.Music;
import com.example.v15.migoproductcatalog.Model.Products;
import com.example.v15.migoproductcatalog.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by V15 on 19/03/2016.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    ArrayList<Products> data = new ArrayList<>();
    Context context;
    Boolean isList;

    public CollectionAdapter(Context context, ArrayList<Products> data, Boolean isList){
        this.data= new ArrayList<>(data);
        this.context=context;
        this.isList=isList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(isList ? R.layout.collection_view_row : R.layout.collection_view_grid, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Products current = data.get(position);
        //holder.icon.setImageDrawable(current.getDrawable());
        Log.d("CURRENT ITEM",current.getTitle());
        Log.d("CURRENT ITEMEXTERNAL", ""+current.getExternalFlag());
        switch (current.getExternalFlag()){
            case 0:
                Picasso.with(context).load(current.getResId()).fit().centerInside().into(holder.icon);
                break;
            case 1:
                Picasso.with(context).load(new File(current.getFileName())).fit().centerInside().into(holder.icon);
        }

        //holder.icon.setImageResource(current.getResId());
        holder.firstLine.setText(current.getTitle());

        if( current instanceof Books) {
            holder.secondLine.setText(((Books) current).getAuthor()+", "+current.getGenreName());
        }else if(current instanceof Music){
            holder.secondLine.setText(((Music) current).getArtist()+", "+current.getGenreName());
        }else if(current instanceof Movies){
            holder.secondLine.setText(((Movies) current).getYear()+", "+current.getGenreName());
        }else {
            holder.secondLine.setText(current.getGenreName());
        }
    }

    public void setData(ArrayList<Products> newData){
        this.data = new ArrayList<>(newData);
    }


    public Products removeItem(int position) {
        final Products product = data.remove(position);
        notifyItemRemoved(position);
        return product;
    }

    public void addItem(int position, Products product) {
        data.add(position, product);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Products product = data.remove(fromPosition);
        data.add(toPosition, product);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Products> products) {
        applyAndAnimateRemovals(products);
        applyAndAnimateAdditions(products);
        applyAndAnimateMovedItems(products);
    }

    private void applyAndAnimateRemovals(ArrayList<Products> newProducts) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final Products product = data.get(i);
            if (!newProducts.contains(product)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Products> newProducts) {
        for (int i = 0, count = newProducts.size(); i < count; i++) {
            final Products product = newProducts.get(i);
            if (!data.contains(product)) {
                addItem(i, product);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Products> newProducts) {
        for (int toPosition = newProducts.size() - 1; toPosition >= 0; toPosition--) {
            final Products product = newProducts.get(toPosition);
            final int fromPosition = data.indexOf(product);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView firstLine;
        TextView secondLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageicon);
            firstLine = (TextView) itemView.findViewById(R.id.firstLine);
            secondLine = (TextView) itemView.findViewById(R.id.secondLine);
        }
    }

}
