package com.example.azim_pc.movieapp;

/**
 * Created by AZIM - PC on 4/27/2016.
 */
//position,DateItem, IdItem, OverviewItem,NameItem,PosterItem,MovieJsonStr
public interface NameListener {
     void setTabJsonAndPosition(int MenuItemId,int postion,String tiltle,String Date,String OverviewItem,String Id,String poster, String MovieJsonString);

}
