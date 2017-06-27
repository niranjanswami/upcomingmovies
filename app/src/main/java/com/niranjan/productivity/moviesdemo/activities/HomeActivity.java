package com.niranjan.productivity.moviesdemo.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niranjan.productivity.moviesdemo.R;
import com.niranjan.productivity.moviesdemo.fragments.InformationFragment;
import com.niranjan.productivity.moviesdemo.fragments.MovieDetailsFragment;
import com.niranjan.productivity.moviesdemo.fragments.UpcomingMoviesFragment;
import com.niranjan.productivity.moviesdemo.listener.OnFragmentInteractionListener;
import com.niranjan.productivity.moviesdemo.utils.TransportConstants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private String fragmentTag;
    private int currentFragment;
    private static TextView toolbar_title;
    private ImageView image_icon;
    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(HomeActivity.this));

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        image_icon = (ImageView) findViewById(R.id.image_icon);

        image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentInteraction(TransportConstants.FRAGMENT_INFORMATION, null);
            }
        });
        onFragmentInteraction(TransportConstants.FRAGMENT_UPCOMING_MOVIES, null);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void updateToolBar(String title) {
        toolbar_title.setText(title);
    }

    @Override
    public void onFragmentInteraction(int fragmentId, Object data) {
        fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentId;
        fragmentTag = String.valueOf(fragmentId);

        switch (fragmentId) {
            case TransportConstants.FRAGMENT_UPCOMING_MOVIES:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_main, UpcomingMoviesFragment.getInstance(),
                                fragmentTag).commit();
                break;
            case TransportConstants.FRAGMENT_MOVIE_DETAILS:
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(fragmentTag)
                        .replace(R.id.fragment_main, MovieDetailsFragment.getInstance((int) data),
                                fragmentTag).commit();
                break;
            case TransportConstants.FRAGMENT_INFORMATION:
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(fragmentTag)
                        .replace(R.id.fragment_main, InformationFragment.getInstance(),
                                fragmentTag).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            closeApp();
        } else {
            super.onBackPressed();
        }
    }

    public void closeApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.quit_message));
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
