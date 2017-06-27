package com.niranjan.productivity.moviesdemo.model;

import java.util.ArrayList;
import java.util.List;


public class MoviesResponse {
    private List<Movie> results;
    private ArrayList<Movie> backdrops;
    private ArrayList<Movie> posters;

    public ArrayList<Movie> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Movie> backdrops) {
        this.backdrops = backdrops;
    }

    public ArrayList<Movie> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Movie> posters) {
        this.posters = posters;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
