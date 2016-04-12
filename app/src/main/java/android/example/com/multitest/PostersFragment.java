package android.example.com.multitest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
