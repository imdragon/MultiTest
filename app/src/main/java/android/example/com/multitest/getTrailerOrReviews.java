package android.example.com.multitest;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getTrailerOrReviews extends AsyncTask<Integer, Void, Void> {
    Fragment callingActivity;

    StringBuilder total = new StringBuilder();
    String firstTrailer;

    public getTrailerOrReviews(Fragment activity) {
        callingActivity = activity;
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(Integer... params) {
        String mID = ((DetailsFragment) callingActivity).details.getMovieId();
        String parameterTorR = "";

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                parameterTorR = "videos";
            } else if (i == 1) {
                parameterTorR = "reviews";
            }
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/" + mID + "/" + parameterTorR + "?api_key=" + callingActivity.getString(R.string.apiKey));
                // making url request and sending it to be read
                Log.e("my url is", String.valueOf(url));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // preparing a reader to go through the response
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // below allows for controlled reading of potentially large text
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                if (i == 0) {
                    getTrailer();
                    total = new StringBuilder();
                } else {
                    getReviews();
                }
                Log.e("Trail", total.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void getTrailer() throws JSONException {
        JSONObject popArray = new JSONObject(total.toString());

        JSONArray trailers = popArray.getJSONArray("results");
        for (int i = 0; i < trailers.length(); i++) {
            JSONObject trailer = trailers.getJSONObject(i);
            firstTrailer = trailer.getString("key");
            ((DetailsFragment) callingActivity).trailerLink = firstTrailer;
        }
    }

    private void getReviews() throws JSONException {
        JSONObject revArray = new JSONObject(total.toString());
        JSONArray reviews = revArray.getJSONArray("results");
        for (int i = 0; i < reviews.length(); i++) {
            StringBuilder sb = new StringBuilder();
            JSONObject review = reviews.getJSONObject(i);
            sb.append(review.getString("content"));
            sb.append("\nReview by:\t");
            sb.append(review.getString("author"));
            ((DetailsFragment) callingActivity).reviews.add(sb.toString());
        }
        ((DetailsFragment) callingActivity).rAdapter.notifyDataSetChanged();
//        ((DetailsFragment)callingActivity).popReviews();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // TODO: 4/28/2016  Add a check for fragment being attached to activity.  Perhaps store movie in movie object and then update from there.
        // Added check to only update link if it exists and also when the fragment is added
        if (callingActivity.isAdded() && callingActivity.getActivity().findViewById(R.id.trailerLink) != null) {
            TextView mTrailer = (TextView) callingActivity.getActivity().findViewById(R.id.trailerLink);
            mTrailer.setVisibility(View.VISIBLE);
        }

    }
}