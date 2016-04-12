package android.example.com.multitest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    public Context mContext;
    private String[] moviePathString;

    public ImageAdapter(Context c, ArrayList<String> moviePosterAddress) {
        mContext = c;
        //allows me to take movie info downloaded from MainActivity
        moviePathString = moviePosterAddress.toArray(new String[moviePosterAddress.size()]);
    }

    @Override
    public int getCount() {
        return moviePathString.length;
    }

    @Override
    public Object getItem(int position) {
        return moviePathString[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
// create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(-1, 585));
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + moviePathString[position])
                    .placeholder(R.drawable.comingsoon)
                    .fit()
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.CYAN);
        } else {
            imageView = (ImageView) convertView;
        }
        Log.e("Url: ", "http://image.tmdb.org/t/p/w780/" + moviePathString[position]);
        return imageView;
    }

}