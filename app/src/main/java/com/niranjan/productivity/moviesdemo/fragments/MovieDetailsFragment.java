package com.niranjan.productivity.moviesdemo.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.niranjan.productivity.moviesdemo.R;
import com.niranjan.productivity.moviesdemo.adapters.PageViewerAdapter;
import com.niranjan.productivity.moviesdemo.model.Movie;
import com.niranjan.productivity.moviesdemo.model.MoviesResponse;
import com.niranjan.productivity.moviesdemo.services.ApiServices;
import com.niranjan.productivity.moviesdemo.services.TransportManager;
import com.niranjan.productivity.moviesdemo.utils.TransportConstants;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.niranjan.productivity.moviesdemo.activities.HomeActivity.updateToolBar;

public class MovieDetailsFragment extends BaseFragment {

    private View rootView;
    private static MovieDetailsFragment fragment;
    private static int movieId;
    private TextView text_description, text_title;
    private RatingBar ratingBar;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Movie> ImagesArray;
    private ArrayList<Movie> responseImages = new ArrayList<>();

    public static MovieDetailsFragment getInstance(int id) {
        if (fragment == null) {
            fragment = new MovieDetailsFragment();
        }
        movieId = id;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolBar(getString(R.string.movie_details));
        displayProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        text_title = (TextView) rootView.findViewById(R.id.text_title);
        text_description = (TextView) rootView.findViewById(R.id.text_description);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);

        final ApiServices apiService = TransportManager.getUPIService();

        Call<Movie> call = apiService.getMovieDetails(movieId, TransportConstants.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                updateToolBar(response.body().getTitle());
                text_title.setText(response.body().getTitle());
                text_description.setText(response.body().getOverview());
                ratingBar.setRating(response.body().getPopularity());

                dismissProgress();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        Call<MoviesResponse> callImage = apiService.getImages(movieId, TransportConstants.API_KEY);
        callImage.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                ImagesArray = response.body().getBackdrops();
                pageViewer();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        return rootView;
    }

    private void pageViewer() {
        responseImages.clear();

        if (ImagesArray.size() < 5) {
            for (int i = 0; i < ImagesArray.size(); i++) {
                responseImages.add(ImagesArray.get(i));
                NUM_PAGES = i+1;
            }
        } else {
            for (int i = 0; i < ImagesArray.subList(0, 5).size(); i++) {
                responseImages.add(ImagesArray.get(i));
                NUM_PAGES = i+1;
            }
        }
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(new PageViewerAdapter(getActivity(), responseImages));
        CirclePageIndicator indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
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
