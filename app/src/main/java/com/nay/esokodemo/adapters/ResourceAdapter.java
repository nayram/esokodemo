package com.nay.esokodemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nay.esokodemo.Models.ListResource;
import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.R;
import com.nay.esokodemo.Util.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by nayram on 3/29/17.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    String TAG=getClass().getName();

    public ArrayList<ListResource.ResData> items;
    Context context;
    int type;

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView name,year,pantone_value ;
        public final ImageView imgFavRes;


        public final View mView;



        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            year = (TextView) itemView.findViewById(R.id.res_year);
            name = (TextView)itemView.findViewById(R.id.tvResName);
            pantone_value = (TextView)itemView.findViewById(R.id.resPantone);
            imgFavRes = (ImageView)itemView.findViewById(R.id.res_fav);

        }
    }


    public ResourceAdapter(ArrayList<ListResource.ResData> items, Context context,int type) {
        this.items = items;
        this.context = context;
        this.type=type;
    }
    public ListResource.ResData getValueAt(int position) {
        return items.get(position);
    }

    public void addData (ListResource.ResData item){
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public ResourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_res,parent,false);

        return new ResourceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ResourceAdapter.ViewHolder holder, final int position) {
       final ListResource.ResData res=items.get(position);

        holder.name.setText(res.getName());
        holder.year.setText(String.valueOf(res.getYear()));
        holder.pantone_value.setText(res.getPantone_value());

        if (type==1){
            holder.imgFavRes.setVisibility(View.INVISIBLE);
        }else{
            holder.imgFavRes.setVisibility(View.VISIBLE);
            boolean isFav= DBHelper.isFavRes(res.getId());
            if (isFav){
                Log.d(TAG,"FAV");
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

                Picasso.with(context)
                        .load(R.drawable.ic_select_fav)
                        .fit()
                        .into(holder.imgFavRes);
            }else{
                Log.d(TAG,"UN_FAV");

                Picasso.with(context)
                        .load(R.drawable.ic_action_fav)
                        .fit()
                        .into(holder.imgFavRes);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type !=1){

                    boolean isFav= DBHelper.isFavRes(res.getId());
                    if (isFav){
                        Log.d(TAG,"FAV");
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                        DBHelper.delFavRes(String.valueOf(res.getId()));
                        Picasso.with(context)
                                .load(R.drawable.ic_action_fav)
                                .fit()
                                .into(holder.imgFavRes);
                    }else{
                        Log.d(TAG,"UN_FAV");
                        DBHelper.saveFavRes(res);
                        Picasso.with(context)
                                .load(R.drawable.ic_select_fav)
                                .fit()
                                .into(holder.imgFavRes);
                    }
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
