package com.nay.esokodemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nay.esokodemo.Models.Data;
import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.R;
import com.nay.esokodemo.Util.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by nayram on 3/29/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {

    public ArrayList<ListUsers.Users> items;
    Context context;
    String TAG=getClass().getName();
    int type;

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public final ImageView profilePic,imgFavUser;
        public final TextView name ;



        public final View mView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            profilePic = (ImageView)itemView.findViewById(R.id.profilePic);
            imgFavUser = (ImageView)itemView.findViewById(R.id.imgFavUser);
            name = (TextView)itemView.findViewById(R.id.tvName);



        }
    }

    public UserAdapter(ArrayList<ListUsers.Users> items, Context context,int type) {
        this.items = items;
        this.context = context;
        this.type=type;
    }

    public ListUsers.Users getValueAt(int position) {
        return items.get(position);
    }

    public void addData (ListUsers.Users item){
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final ListUsers.Users user=items.get(position);
        Picasso.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.ic_action_user)
                .fit()
                .into(holder.profilePic);
        holder.name.setText(user.getFirst_name()+" "+user.getLast_name());
        /*if (type==1){
            Log.d(TAG,String.valueOf(type));
            holder.imgFavUser.setVisibility(View.GONE);
        }else{
            holder.imgFavUser.setVisibility(View.VISIBLE);
            boolean isFav= DBHelper.isFavUser(user.getId());
            if (isFav){
                Log.d(TAG,"FAV "+user.getId());
                Picasso.with(context)
                        .load(R.drawable.ic_select_fav)
                        .fit()
                        .into(holder.imgFavUser);
            }else{
                Log.d(TAG,"UN_FAV "+user.getId());
                Picasso.with(context)
                        .load(R.drawable.ic_action_fav)
                        .fit()
                        .into(holder.imgFavUser);
            }
        }*/



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type !=1){
                    boolean isFav= DBHelper.isFavUser(user.getId());
                    if (isFav){
                        Log.d(TAG,"FAV");
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                        DBHelper.delFavUser(String.valueOf(user.getId()));
                        Picasso.with(context)
                                .load(R.drawable.ic_user_fav)
                                .fit()
                                .into(holder.imgFavUser);
                    }else{
                        Log.d(TAG,"UN_FAV");
                        DBHelper.saveFavUser(user);
                        Picasso.with(context)
                                .load(R.drawable.ic_user_select_fav)
                                .fit()
                                .into(holder.imgFavUser);
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
