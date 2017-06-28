package com.niranjan.productivity.moviesdemo.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niranjan.productivity.moviesdemo.R;
import com.niranjan.productivity.moviesdemo.utils.TransportConstants;
import com.niranjan.productivity.moviesdemo.adapters.MoviesAdapter;
import com.niranjan.productivity.moviesdemo.listener.OnFragmentListItemSelectListener;
import com.niranjan.productivity.moviesdemo.model.Movie;
import com.niranjan.productivity.moviesdemo.model.MoviesResponse;
import com.niranjan.productivity.moviesdemo.services.TransportManager;
import com.niranjan.productivity.moviesdemo.services.ApiServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niranjan.productivity.moviesdemo.activities.HomeActivity.updateToolBar;


public class UpcomingMoviesFragment extends BaseFragment implements OnFragmentListItemSelectListener {

    private View rootView;
    static UpcomingMoviesFragment fragment;
    private static MoviesAdapter moviesAdapter;
    RecyclerView recyclerView;
    List<Movie> movies;

    public static UpcomingMoviesFragment getInstance() {
        if (fragment == null) {
            fragment = new UpcomingMoviesFragment();
        }
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolBar(getString(R.string.upcoming_movies));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_upcoming_movies, container, false);
        displayProgress();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ApiServices apiService = TransportManager.getAPIService();

        Call<MoviesResponse> call = apiService.getTopRatedMovies(TransportConstants.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movies = response.body().getResults();
                updateList(movies);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
            }
        });
        return rootView;
    }

    public void updateList(List<Movie> mo) {
        moviesAdapter = new MoviesAdapter(getActivity(), mo);
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setListener(this);

        dismissProgress();
    }

    @Override
    public void onListItemSelected(int itemId, Object data) {
        final Movie movie = (Movie) data;
        int movieId = movie.getId();
        mListener.onFragmentInteraction(TransportConstants.FRAGMENT_MOVIE_DETAILS, movieId);
    }

    @Override
    public void onListItemLongClicked(int itemId, Object data, View view) {

    }

    ProgressDialog progressDialog;

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
        }
        progressDialog.show();
    }


    private void dismissProgress() {

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
