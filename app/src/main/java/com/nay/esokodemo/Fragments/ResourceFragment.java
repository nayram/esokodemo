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

import com.nay.esokodemo.Models.ListResource;
import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.R;
import com.nay.esokodemo.Util.App;
import com.nay.esokodemo.Util.ConnectionDetector;
import com.nay.esokodemo.adapters.ResourceAdapter;
import com.nay.esokodemo.adapters.UserAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nayram on 3/29/17.
 */

public class ResourceFragment extends Fragment {

    View rootView;


    RecyclerView recyclerView;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_resource,container,false);
        recyclerView=(RecyclerView) rootView.findViewById(R.id.recycler_view_res);
        progressDialog=new ProgressDialog(recyclerView.getContext());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Resources");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        if (new ConnectionDetector(recyclerView.getContext()).isConnectingToInternet()) {
            progressDialog.show();
            App.esokoDemoService.getResource(new Callback<ListResource>() {
                @Override
                public void success(ListResource listResource, Response response) {
                    progressDialog.dismiss();
                    recyclerView.setAdapter(new ResourceAdapter(listResource.data, recyclerView.getContext(), 0));
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Sorry failed to load users", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Check your internet connection!!", Toast.LENGTH_SHORT).show();
        }


        return rootView;

    }
}
