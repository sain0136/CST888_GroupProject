package algonquin.cst2335.groupproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 *  Made for displaying individual movies from the database
 */
public class EmptyFrameWork extends AppCompatActivity {
    /** bitmap poster */
    Bitmap bitmap;
    /** databse obj acess saved movies*/
    SQLiteDatabase db;
    /** index for proper record lookup*/
    int index;
    /** used to set poster image*/
    ImageView sqlMap;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
//        MovieListFragment movieFragment = new MovieListFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, movieFragment).commit();
        MySqlData opener = new MySqlData( this );
        db = opener.getWritableDatabase();
        Intent fromPrevious = getIntent();
        index = (int) fromPrevious.getLongExtra("ID",0);
        TextView title = findViewById(R.id.movieTitleViewInv);
        TextView yr = findViewById(R.id.yearViewInv);
        TextView rate = findViewById(R.id.ratingViewInv);
        TextView runClock = findViewById(R.id.runtimeViewInv);
        TextView plotLine = findViewById(R.id.plotViewInv);
        TextView actors = findViewById(R.id.mainActorsViewInv);
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
            String yrW =results.getString(yrCol);
            String rat =results.getString(ratCol);
            String runt =results.getString(runtCol);
            String plot =results.getString(plt);
            String mainActs=results.getString(mainActsCol) ;
            String moviePoUrl =results.getString(moviePostUrlCol);
            long i =results.getInt(_idCol);
            if( i == index){
                title.setText(movieTi);
                yr.setText("Released: "+yrW);
                rate.setText("Average Rating: "+rat);
                runClock.setText("Runtime: "+runt);
                actors.setText("Main Actors: "+mainActs);
                plotLine.setText(plot);

                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute( () -> {
                    try {
                        bitmap = BitmapFactory.decodeStream((InputStream)new URL(moviePoUrl).getContent());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                runOnUiThread(() -> {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(),
                                    movieTi, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP,0,0);
                            toast.show();


                            sqlMap = findViewById(R.id.moviePicView);
                            sqlMap.setImageBitmap(bitmap);
                        }
                    }, 500);

                });
            }
        }
        Button back = findViewById(R.id.backButtonView);
        back.setOnClickListener((clk)->{
            Intent nextPage = new Intent( EmptyFrameWork.this, SavedMovies.class);
            startActivity( nextPage );
        });
    }
}
