package com.example.azim_pc.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AZIM - PC on 4/2/2016.
 */
//
public class ImageAdapter extends BaseAdapter {
    List<String> poster_paths;
    Context context;                                                       /////////// ////////
    public ImageAdapter(Context context,List<String> poster_paths){
        this.context=context;
        this.poster_paths=poster_paths;
    }
    @Override
    public int getCount() {
        return poster_paths.size();
    }                 //////////////////

   @Override
    public String getItem(int i) {
        return poster_paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        ImageView imageView;
    }

   // @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.iv_item);
            convertView.setTag(holder);
        }else{

            holder=(ViewHolder)convertView.getTag();
        }
        Picasso.with(context).load(getItem(i)).into(holder.imageView);
        return convertView;
    }

}

