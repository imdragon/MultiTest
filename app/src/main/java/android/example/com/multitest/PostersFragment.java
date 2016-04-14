package android.example.com.multitest;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Admin on 4/11/2016.
 */
public class PostersFragment extends Fragment {
    Boolean isDualPane;
    GridView gridView;

    protected ArrayList<String> moviePosterAddress = new ArrayList<>();
    public ArrayList<Movie> movieObjectArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sortChoice) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.sort_option).setItems(R.array.sortOptionArray, new DialogInterface.OnClickListener() {
                //// TODO: 3/8/2016 See about styling the AlertDialog without a new layout
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        getActivity().setTitle("Most Popular");
                        new RequestPopularMovies(PostersFragment.this).execute("popularity.desc", null, null);
                        // popularity.desc
                    }
                    if (which == 1) {
                        getActivity().setTitle("Highest Rated");
                        // below request shows by highest rating for US movies
                        new RequestPopularMovies(PostersFragment.this).execute("certification_country=US&sort_by=vote_average.desc&vote_count.gte=1000", null, null);
                        // rating.desc
                    }
                    if (which == 2) {
                        getActivity().setTitle("My Favorites");
                        favoriteLayout();
                    }
                }
            });
            AlertDialog pop = builder.create();
            pop.show();
        }
        if (item.getItemId() == R.id.deleteAllFavorites) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Delete all favorites?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                /**
                 * This method will be invoked when a button in the dialog is clicked.
                 *
                 * @param dialog The dialog that received the click.
                 * @param which  The button that was clicked (e.g.
                 *               {@link DialogInterface#BUTTON1}) or the position
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getContentResolver().delete(MovDBContract.MovieEntry.CONTENT_URI, null, null);
                }
            }).setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                /**
                 * This method will be invoked when a button in the dialog is clicked.
                 *
                 * @param dialog The dialog that received the click.
                 * @param which  The button that was clicked (e.g.
                 *               {@link DialogInterface#BUTTON1}) or the position
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog pop = builder.create();
            pop.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater inflate = getActivity().getMenuInflater();
        inflate.inflate(R.menu.popup_menu_layout, menu);
    }


    public void favoriteLayout() {
        movieObjectArray.clear();
        moviePosterAddress.clear();
        String[] mProjection = {
                MovDBContract.MovieEntry.COLUMN_TITLE,
                MovDBContract.MovieEntry.COLUMN_MOVIEID,
                MovDBContract.MovieEntry.COLUMN_DESCRIPTION,
                MovDBContract.MovieEntry.COLUMN_POSTER,
                MovDBContract.MovieEntry.COLUMN_BACKDROP,
                MovDBContract.MovieEntry.COLUMN_RATING,
                MovDBContract.MovieEntry.COLUMN_RELEASE,
                MovDBContract.MovieEntry.COLUMN_FAVORITE
        };
          /*    0 Title
                1 MovieID
                2 Synopsis
                3 Poster
                4 Backdrop
                5 Rating
                6 Release
                7 favorite <--- probably don't need it */
        Cursor cs = getActivity().getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, mProjection, null, null, null);
        if (cs == null) {
            Log.e("Output:", String.valueOf(cs.getCount()));
        } else {
            while (cs.moveToNext()) {
                moviePosterAddress.add(cs.getString(3));
                Movie tempMovie = new Movie();
                tempMovie.setTitle(cs.getString(0));
                tempMovie.setPoster(cs.getString(3));
                tempMovie.setSynopsis(cs.getString(2));
                tempMovie.setRating(cs.getString(5));
                tempMovie.setReleaseDate(cs.getString(6));
                tempMovie.setBackdrop(cs.getString(4));
                tempMovie.setMovieId(cs.getString(1));
                movieObjectArray.add(tempMovie);
            }
            setupGrid();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        if (savedInstanceState == null) {
            new RequestPopularMovies(this).execute("popularity.desc", null, null);
            Log.e("WAS NULL", "WAS NULL");
        } else {
            moviePosterAddress = savedInstanceState.getStringArrayList("posters");
            movieObjectArray = savedInstanceState.getParcelableArrayList("movies");
            setupGrid();
            Log.e("NOT NULL", "NOT NULL");
        }
        return view;
    }

    public void setupGrid() {
        gridView.setAdapter(new ImageAdapter(getActivity(), moviePosterAddress));

        //
        DetailsFragment detailFragment = DetailsFragment.newInstance(0, movieObjectArray.get(0));
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.details, detailFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        //
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Movie movieDetails = movieObjectArray.get(position);
//                Log.e("movieID", movieObjectArray.get(position).getMovieId());
//                Intent i = new Intent(getActivity(), DetailsActivity.class);
//                i.putExtra("movieInfo", movieDetails);
//                startActivity(i);
//            }
//        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        View detailFrame = getActivity().findViewById(R.id.details);

        isDualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                if (!isDualPane) {
                    Movie movieDetails = movieObjectArray.get(pos);
                    Log.e("movieID", movieObjectArray.get(pos).getMovieId());
                    Intent i = new Intent(getActivity(), DetailsActivity.class);
                    i.putExtra("movieInfo", movieDetails);
                    startActivity(i);
                } else {
                    DetailsFragment detailFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);

                    if (detailFragment == null || detailFragment.getShownIndex() != pos) {
                        Movie movieDetails = movieObjectArray.get(pos);
                        Log.e("movieID", movieObjectArray.get(pos).getMovieId());

                        detailFragment = DetailsFragment.newInstance(pos, movieDetails);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.replace(R.id.details, detailFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    }
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("SAVEDiN", "SAVEDiN");
        outState.putStringArrayList("posters", moviePosterAddress);
        outState.putParcelableArrayList("movies", movieObjectArray);
        super.onSaveInstanceState(outState);
    }
}
