package com.nay.esokodemo.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;

import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.R;
import com.nay.esokodemo.Util.App;
import com.nay.esokodemo.Util.ConnectionDetector;
import com.nay.esokodemo.adapters.UserAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by nayram on 3/29/17.
 */

public class UsersFragment extends Fragment {

    View rootView;


    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    int PAGE_SIZE=4;


    boolean isLoading=true;
    boolean isLastPage=true;
    UserAdapter userAdapteradapter;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int current_page = 1;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView=(RecyclerView) rootView.findViewById(R.id.recycler_view_user);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        progressDialog=new ProgressDialog(recyclerView.getContext());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Users");
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserContent();
            }
        });
        if (new ConnectionDetector(recyclerView.getContext()).isConnectingToInternet()) {
            swipeRefreshLayout.setRefreshing(true);
            loadUserContent();
        }
        else {
            Toast.makeText(getActivity(), "Check your internet connection!!", Toast.LENGTH_SHORT).show();
        }




        return rootView;

    }

    void loadUserContent(){
        App.esokoDemoService.getUsers("1", new Callback<ListUsers>() {
            @Override
            public void success(ListUsers listUsers, Response response) {

                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                PAGE_SIZE = listUsers.total;
                visibleThreshold=listUsers.total;

                userAdapteradapter = new UserAdapter(listUsers.data, recyclerView.getContext(), 0);
                recyclerView.setAdapter(userAdapteradapter);
            }

            @Override
            public void failure(RetrofitError error) {
//                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Sorry failed to load users", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadMore(int page){
        App.esokoDemoService.getUsers(String.valueOf(page), new Callback<ListUsers>() {
            @Override
            public void success(ListUsers listUsers, Response response) {
                progressDialog.dismiss();

                for (ListUsers.Users user:listUsers.data ){
                    userAdapteradapter.addData(user);
                }
//                recyclerView.setAdapter(new UserAdapter(listUsers.data,recyclerView.getContext(),0));
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Sorry failed to load users", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            /*int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();*/



            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();


            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }

            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached

                // Do something
                current_page++;
                loading = true;
                loadMore(current_page);


            }

            /*if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMore();
                }
            }*/

//            if (current_page <= PAGE_SIZE){
//                loadMore();
//            }


        }
    };


}
