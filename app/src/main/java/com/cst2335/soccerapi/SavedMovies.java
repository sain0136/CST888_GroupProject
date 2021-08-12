package com.cst2335.soccerapi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
/**
 * Loads up recycler view
 */
public class SavedMovies extends AppCompatActivity {
    /** display list of movies database*/
    RecyclerView movieList;
    /** adapter name */
    MyMoviesAdapter adt =new MyMoviesAdapter();
    /** database srore for movie objects */
    ArrayList<MoviesStored> movies = new ArrayList<>();
    /** database access */
    SQLiteDatabase db;
    /** Movie stores object */
    MoviesStored movieSave = new MoviesStored();
    /**
     * creates the the actvity
     * @param savedInstanceState
     */
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_movies);
        movieList =findViewById(R.id.movieList);

        Button backBttn = findViewById(R.id.backButtom);
        MySqlData opener = new MySqlData( this );
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from "+ MySqlData.TABLE_NAME + ";",null);

        int _idCol = results.getColumnIndex("_id");
        int movieTiCol = results.getColumnIndex(MySqlData.col_movie_Title);
        int yrCol = results.getColumnIndex(MySqlData.col_year);
        int ratCol = results.getColumnIndex(MySqlData.col_rating);
        int runtCol = results.getColumnIndex(MySqlData.col_runtime);
        int mainActsCol = results.getColumnIndex(MySqlData.col_mainActors);
        int moviePostUrlCol = results.getColumnIndex(MySqlData.col_moviePosterUrl);
        int plt = results.getColumnIndex(MySqlData.col_plot);

        while( results.moveToNext()) {
            String movieTi=results.getString(movieTiCol);
            String yr =results.getString(yrCol);
            String rat =results.getString(ratCol);
            String runt =results.getString(runtCol);
            String plot =results.getString(plt);
            String mainActs=results.getString(mainActsCol) ;
            String moviePoUrl =results.getString(moviePostUrlCol);
            long i =results.getInt(_idCol);

            movieSave =new MoviesStored(movieTi,yr,rat,runt,plot,mainActs,moviePoUrl,i);
            movies.add(movieSave);
        }
        movieList.setAdapter(adt);
        movieList.setLayoutManager(new LinearLayoutManager(this));
        backBttn.setOnClickListener((clk)->{
            Intent nextPage = new Intent( SavedMovies.this, MovieActivity.class);
            startActivity( nextPage );
        });
    }

    /**
     * Adapter implementation
     */
    private class MyMoviesAdapter extends RecyclerView.Adapter<MyRowViews>{
        @Override
        public int getItemViewType(int position) {
            MoviesStored thisRow = movies.get(position);
            return (int)thisRow.id;
        }
        /**
         * Implemented method
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            int layoutID=R.layout.movie_view;
            View loadedRow = inflater.inflate(layoutID, parent, false);
            return new MyRowViews(loadedRow);
        }
        /**
         * Implemented method
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder( SavedMovies.MyRowViews holder, int position) {
            holder.movieText.setText(movies.get(position).getMovieTitle());
            holder.yearText.setText(movies.get(position).getYear().toString());
            int op = (int) movies.get(position).getId();
            holder.IdView.setText(Integer.toString(op));
            holder.setPosition(position);
        }

        /**
         * Implemented method
         * @return
         */
        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

    /**
     * Implemented of recyclerView for each view
     */
    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView movieText;
        TextView yearText;
        TextView IdView;

        int position =-1;
        ImageView imagePoserView ;

        /**
         * Implemented method
         * @param itemView
         */
        public MyRowViews( View itemView) {
            super(itemView);
            IdView = itemView.findViewById(R.id.IdMarker);
            movieText = itemView.findViewById(R.id.movieTitleView);
            yearText = itemView.findViewById(R.id.yearView);
            imagePoserView = findViewById(R.id.moviePoster);
            itemView.setOnClickListener(click ->{
                AlertDialog.Builder builder = new AlertDialog.Builder( SavedMovies.this );
                builder.setMessage("View Movie details or delete "+ movieText.getText() +" from database: ");
                builder.setTitle("Delete:");
                MoviesStored removedMovies= movies.get(position);
                builder.setNeutralButton("View Movie Details",(dialog,cl)->{
                    Intent nextPage = new Intent( SavedMovies.this, EmptyFrameWork.class);
                    nextPage.putExtra( "ID", movies.get(position).getId());
                    startActivity( nextPage );
                });
                builder.setPositiveButton("Yes",((dialog, cl) -> {
                    movies.remove(position);
                    adt.notifyItemRemoved(position);
                    db.delete(MySqlData.TABLE_NAME,"_id=?",new String[]{Long.toString(removedMovies.getId())});
                    Snackbar.make(movieText, "You deleted " + removedMovies.movieTitle,Snackbar.LENGTH_LONG)
                            .show();
                }));
                builder.setNegativeButton("no",((dialog, cl) -> {}));
                builder.create().show();
            });
        }
        public void setPosition(int p){
            position= p;
        }
    }

    /**
     * Custom class for creating a movie object
     */
    private class MoviesStored {
        String movieTitle;
        String year ;
        String rating ;
        String runtime ;
        String plot ;
        String mainActors ;
        String moviePosterUrl;
        long id;
        /**
         *  empty constructor
         */
        public MoviesStored() {
        }
        /**
         * setter
         * @param id
         */
        public void setId(long id) {
            this.id = id;
        }
        /**
         * getter
         * @return
         */
        public long getId() {
            return id;
        }
        /**
         * constructor
         * @param movieTitle
         * @param year
         * @param rating
         * @param runtime
         * @param plot
         * @param mainActors
         * @param moviePosterUrl
         */
        public MoviesStored(String movieTitle, String year, String rating, String runtime, String plot, String mainActors, String moviePosterUrl) {
            this.movieTitle = movieTitle;
            this.year = year;
            this.rating = rating;
            this.runtime = runtime;
            this.plot = plot;
            this.mainActors = mainActors;
            this.moviePosterUrl = moviePosterUrl;
        }
        /**
         * constructor
         * @param movieTitle
         * @param year
         * @param rating
         * @param runtime
         * @param plot
         * @param mainActors
         * @param moviePosterUrl
         * @param id
         */
        public MoviesStored(String movieTitle, String year, String rating, String runtime, String plot, String mainActors, String moviePosterUrl, long id) {
            this.movieTitle = movieTitle;
            this.year = year;
            this.rating = rating;
            this.runtime = runtime;
            this.plot = plot;
            this.mainActors = mainActors;
            this.moviePosterUrl = moviePosterUrl;
            this.id = id;
        }
        /**
         * getter
         * @return
         */
        public String getMovieTitle() {
            return movieTitle;
        }
        /**
         * getter
         * @return
         */
        public String getYear() {
            return year;
        }
        /**
         * getter
         * @return
         */
        public String getRating() {
            return rating;
        }
        /**
         * getter
         * @return
         */
        public String getRuntime() {
            return runtime;
        }
        /**
         * getter
         * @return
         */
        public String getPlot() {
            return plot;
        }
        /**
         * getter
         * @return
         */
        public String getMainActors() {
            return mainActors;
        }
        /**
         * getter
         * @return
         */
        public String getMoviePosterUrl() {
            return moviePosterUrl;
        }
    }
}
