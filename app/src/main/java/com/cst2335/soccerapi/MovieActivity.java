package com.cst2335.soccerapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * @author Jean saint Rose
 * @version 1.0
 */
public class MovieActivity extends AppCompatActivity {
    /** This url for connection*/
    private String stringURL = "https://www.omdbapi.com/?apikey=6c9862c2&r=xml&t= ";
    /** The movies title*/
    private String movieTitle = null;
    /** The movies year release*/
    private String year = null;
    /** The movies rating*/
    private String rating = null;
    /** The movies rubtimw*/
    private String runtime = null;
    /** The movies plot*/
    private String plot = null;
    /** The movies main actors*/
    private String mainActors = null;
    /** The movies poster bitmap link*/
    private String moviePosterUrl = null;
    /** bitmap for poster*/
    private Bitmap bitmap;
    /** sql database*/
    private SQLiteDatabase db;
    /**
     * oncreate method load activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activitymain);

        MySqlData opener = new MySqlData( this );
        db = opener.getWritableDatabase();

        Button searchForMovie = findViewById(R.id.searchButton);
        EditText searchTerm = findViewById(R.id.movieSearchTerm);
        Button saveToDatabse = findViewById(R.id.databaseStore);
        Button savedMoviesView = findViewById(R.id.savedMovies);
        ImageButton help = findViewById(R.id.helpButton);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchWord =prefs.getString("SearchTerm", "lol");
        searchTerm.setText(searchWord);

        TextView title = findViewById(R.id.movieTitle);
        TextView yr = findViewById(R.id.movieYear);
        TextView rate = findViewById(R.id.movieRating);
        TextView runClock = findViewById(R.id.movieRuntime);
        TextView plotLine = findViewById(R.id.moviePlot);
        TextView actors = findViewById(R.id.movieActors);
        savedMoviesView.setOnClickListener((clk)->{
            Intent nextPage = new Intent( MovieActivity.this, SavedMovies.class);
            startActivity( nextPage );
        });

        searchForMovie.setOnClickListener((clk)->{
            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("SearchTerm", searchTerm.getText().toString());
            editor.apply();
            String search =searchTerm.getText().toString();
            AlertDialog dialog = new AlertDialog.Builder(MovieActivity.this)
                    .setTitle("Getting Movie Details")
                    .setMessage("Looking through our massive databases")
                    .setView(new ProgressBar(MovieActivity.this))
                    .show();
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {
                try {
                    stringURL= stringURL+URLEncoder.encode(search, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    InputStream in = null;
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8");
                    while( xpp.next() != XmlPullParser.END_DOCUMENT )
                    {
                        switch (xpp.getEventType())
                        {
                            case XmlPullParser.START_TAG:
                                if (xpp.getName().equals("movie"))
                                {
                                    movieTitle =xpp.getAttributeValue(null,"title");
                                    year=xpp.getAttributeValue(null,"year");
                                    rating=xpp.getAttributeValue(null,"imdbRating");
                                    runtime=xpp.getAttributeValue(null,"runtime");
                                    plot=xpp.getAttributeValue(null,"plot");
                                    mainActors=xpp.getAttributeValue(null,"actors");
                                    moviePosterUrl=xpp.getAttributeValue(null,"poster");
                                    try {
                                        bitmap = BitmapFactory.decodeStream((InputStream)new URL(moviePosterUrl).getContent());

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(() -> {
                                        dialog.hide();
                                        title.setText(movieTitle);
                                        yr.setText(year);
                                        rate.setText(rating);
                                        runClock.setText(runtime);
                                        plotLine.setText(plot);
                                        actors.setText(mainActors);
                                        ImageView i = findViewById(R.id.moviePoster);
                                        i.setImageBitmap(bitmap);
                                    });

                                }
                                break;
                        }
                    }
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            });
        });

        saveToDatabse.setOnClickListener((clk)->{
            ContentValues newRow = new ContentValues();
            newRow.put(MySqlData.col_movie_Title,movieTitle);
            newRow.put(MySqlData.col_rating,rating);
            newRow.put(MySqlData.col_runtime,runtime);
            newRow.put(MySqlData.col_year,year);
            newRow.put(MySqlData.col_plot,plot);
            newRow.put(MySqlData.col_mainActors,mainActors);
            newRow.put(MySqlData.col_moviePosterUrl,moviePosterUrl);
            long newId= db.insert(MySqlData.TABLE_NAME,MySqlData.col_movie_Title,newRow);
        });

        help.setOnClickListener((click)->{
            AlertDialog.Builder builder = new AlertDialog.Builder( MovieActivity.this );
            builder.setMessage("Use this program to search for movies by title. Press save to save it to your own list. Press saved video to view your list! You can delete videos or view extra details of favorite movies");
            builder.setNegativeButton("exit",((dialog, cl) -> {}));
            builder.create().show();
        });
    }
}
