package android.example.com.multitest;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    Movie details = new Movie();
    String trailerLink;
    Button fButton;
    ArrayList<String> reviews = new ArrayList<String>();
    ArrayAdapter<String> rAdapter;

    public static DetailsFragment newInstance(Movie movieInfo) {
        DetailsFragment f = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("movieInfo", movieInfo);
        f.setArguments(args);

        return f;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View mDetailsView = inflater.inflate(R.layout.details_view, container, false);
        //  check if coming from intent
        if (getArguments() == null) {
            details = getActivity().getIntent().getParcelableExtra("movieInfo");
        } else {
            details = getArguments().getParcelable("movieInfo");
        }
        rAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, reviews);

        ListView reviewsList = (ListView) mDetailsView.findViewById(R.id.reviewsListView);
        reviewsList.setOnTouchListener(new View.OnTouchListener() {
            /**
             * Called when a touch event is dispatched to a view. This allows listeners to
             * get a chance to respond before the target view.
             *
             * @param v     The view the touch event has been dispatched to.
             * @param event The MotionEvent object containing full information about
             *              the event.
             * @return True if the listener has consumed the event, false otherwise.
             */
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        TextView mTitle = (TextView) mDetailsView.findViewById(R.id.original_title_detail);
        mTitle.setText(details.getTitle());
        mTitle.setShadowLayer(25, 0, 0, Color.BLACK);

        ImageView mBackdrop = (ImageView) mDetailsView.findViewById(R.id.backdropImageView);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/" + details.getBackdrop()).into(mBackdrop);
        Log.e("backdrop url", "http://image.tmdb.org/t/p/w780/" + details.getBackdrop());
        ImageView mPoster = (ImageView) mDetailsView.findViewById(R.id.mPoster);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/" + details.getPoster())
                .placeholder(R.drawable.comingsoon).into(mPoster);
        TextView mOverview = (TextView) mDetailsView.findViewById(R.id.synopsis);
        mOverview.setText(details.getSynopsis());
        TextView mRelease = (TextView) mDetailsView.findViewById(R.id.releaseDate);
        mRelease.setText("Released: " + details.getReleaseDate().substring(0, 4));
        TextView mRating = (TextView) mDetailsView.findViewById(R.id.ratingDetail);

        fButton = (Button) mDetailsView.findViewById(R.id.favButton);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite(v);
            }
        });

        RatingBar mRatingBar = (RatingBar) mDetailsView.findViewById(R.id.ratingBar);
        mRatingBar.setRating(Float.valueOf(details.getRating()));
        mRating.setText("Rating: " + details.getRating() + "/10");
        TextView trailerText = (TextView) mDetailsView.findViewById(R.id.trailerLink);
        trailerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchTrailer(v);
            }
        });
        reviewsList.setAdapter(rAdapter);
        new getTrailerOrReviews(DetailsFragment.this).execute(0, null, null);
        favoriteCheck();

        return mDetailsView;
    }

    public void addFavorite(View v) {

        ContentValues fav = new ContentValues();

        fav.put(MovDBContract.MovieEntry.COLUMN_MOVIEID, details.getMovieId());
        fav.put(MovDBContract.MovieEntry.COLUMN_TITLE, details.getTitle());
        fav.put(MovDBContract.MovieEntry.COLUMN_DESCRIPTION, details.getSynopsis());
        fav.put(MovDBContract.MovieEntry.COLUMN_POSTER, details.getPoster());
        fav.put(MovDBContract.MovieEntry.COLUMN_BACKDROP, details.getBackdrop());
        fav.put(MovDBContract.MovieEntry.COLUMN_RATING, details.getRating());
        fav.put(MovDBContract.MovieEntry.COLUMN_RELEASE, details.getReleaseDate());
        fav.put(MovDBContract.MovieEntry.COLUMN_FAVORITE, "favorite");

        Uri rUri = getActivity().getContentResolver().insert(MovDBContract.MovieEntry.CONTENT_URI, fav);
        if (rUri == null) {
            removeFavorite();
        } else {
            Toast.makeText(getActivity(), rUri.toString(), Toast.LENGTH_SHORT).show();
            fButton.setBackgroundColor(Color.YELLOW);
            fButton.setText("Favorite!");
        }
    }

    private Boolean favoriteCheck() {
        Boolean flag = false;
        Cursor cs = getActivity().getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, new String[]{MovDBContract.MovieEntry.COLUMN_MOVIEID}, null, null, null);
        if (cs == null) {
            return flag;
        } else {
            while (cs.moveToNext()) {
                if (cs.getString(0).equals(details.getMovieId())) {
                    fButton.setBackgroundColor(Color.YELLOW);
                    fButton.setText("Favorite!");
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        cs.close();
        return flag;
    }

    private void removeFavorite() {
        getActivity().getContentResolver().delete(MovDBContract.MovieEntry.CONTENT_URI, MovDBContract.MovieEntry.COLUMN_MOVIEID + "=?", new String[]{details.getMovieId()});
        fButton.setText("Mark as\nfavorite");
        fButton.setBackgroundColor(Color.CYAN);

    }

    public void watchTrailer(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerLink)));
    }
}
