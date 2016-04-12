package android.example.com.multitest;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public String title;
    public String poster;
    public String synopsis;
    public String releaseDate;
    public String rating;
    public String backdrop;
    public String movieId;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public Movie() {

    }

    protected Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        synopsis = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
        backdrop = in.readString();
        movieId = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public Movie(String title, String poster, String synopsis, String releaseDate, String rating, String backdrop, String movieId) {
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.backdrop = backdrop;
        this.movieId = movieId;
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(synopsis);
        dest.writeString(releaseDate);
        dest.writeString(rating);
        dest.writeString(backdrop);
        dest.writeString(movieId);
    }
}
