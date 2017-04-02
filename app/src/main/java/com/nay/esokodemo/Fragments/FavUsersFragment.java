package com.nay.esokodemo.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.R;
import com.nay.esokodemo.Util.App;
import com.nay.esokodemo.Util.DBHelper;
import com.nay.esokodemo.adapters.UserAdapter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nayram on 3/29/17.
 */

public class FavUsersFragment extends Fragment {


    View rootView;


    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_user);
        progressDialog = new ProgressDialog(recyclerView.getContext());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Users");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        progressDialog.show();
        ArrayList<ListUsers.Users> datas = DBHelper.getFavUsers();

        if (datas != null) {
            progressDialog.dismiss();
            recyclerView.setAdapter(new UserAdapter(datas, recyclerView.getContext(),1));
        } else {
            progressDialog.dismiss();
            Toast.makeText(recyclerView.getContext(), "No users found", Toast.LENGTH_SHORT).show();
        }
        return  rootView;


    }

}
