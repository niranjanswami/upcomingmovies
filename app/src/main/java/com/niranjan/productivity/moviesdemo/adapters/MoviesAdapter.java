package com.niranjan.productivity.moviesdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niranjan.productivity.moviesdemo.R;
import com.niranjan.productivity.moviesdemo.listener.OnFragmentListItemSelectListener;
import com.niranjan.productivity.moviesdemo.model.Movie;
import com.niranjan.productivity.moviesdemo.utils.TransportConstants;

import java.util.List;

import static com.niranjan.productivity.moviesdemo.activities.HomeActivity.imageLoader;
import static com.niranjan.productivity.moviesdemo.activities.HomeActivity.options;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> movies;
    private Context context;
    OnFragmentListItemSelectListener mListener;

    public MoviesAdapter(Activity activity, List<Movie> deviceDetailsList) {
        this.movies = deviceDetailsList;
        this.context = activity;
    }

    public void setListener(OnFragmentListItemSelectListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.movie_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.text_movie_name.setText(movies.get(position).getTitle());
        holder.text_date.setText(movies.get(position).getReleaseDate());
        if (movies.get(position).isAdult()) {
            holder.text_adult.setText(R.string.adult);
        } else {
            holder.text_adult.setText(R.string.u_a);
        }
        imageLoader.displayImage(TransportConstants.IMAGE_URL + movies.get(position).getPosterPath(), holder.image_icon, options);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListItemSelected(position, movies.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mView;
        TextView text_movie_name;
        TextView text_date;
        TextView text_adult;
        ImageView image_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            text_movie_name = (TextView) itemView.findViewById(R.id.text_movie_name);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            text_adult = (TextView) itemView.findViewById(R.id.text_adult);
            image_icon = (ImageView) itemView.findViewById(R.id.image_icon);
        }

        @Override
        public void onClick(View view) {
            mListener.onListItemSelected(view.getId(), movies.get(getLayoutPosition()));
        }
    }
}