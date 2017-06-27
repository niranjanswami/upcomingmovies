package com.niranjan.productivity.moviesdemo.services;

import com.niranjan.productivity.moviesdemo.model.Movie;
import com.niranjan.productivity.moviesdemo.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiServices {

    @GET("movie/upcoming")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/images")
    Call<MoviesResponse> getImages(@Path("id") int id, @Query("api_key") String apiKey);
}
