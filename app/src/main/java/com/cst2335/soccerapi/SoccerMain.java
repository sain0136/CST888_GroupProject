package com.cst2335.soccerapi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SoccerMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<NewsItem> elements = new ArrayList<>();
    MyAdapter adt=new MyAdapter();
    public static final String SOCCER_TITLE = "TITLE";
    public static final String SOCCER_IMG = "IMG";
    public static final String SOCCER_DATE = "DATE";
    public static final String SOCCER_DESC = "DESC";
    public static final String SOCCER_LINK = "LINK";
    FrameLayout frameLayout;
    private Button instructions;
    private Switch aSwitch;
    SharedPreferences sharedPreferences;
    EditText rating;
    RatingBar rating_star;
    private boolean isTablet;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_main);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((item)-> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });


        frameLayout = findViewById(R.id.soccerFragmentLocation);
        isTablet = frameLayout != null;

        RecyclerView myList=findViewById(R.id.soccerNewsList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        myList.setLayoutManager(layoutManager);
        myList.setAdapter(adt);
        progressBar=findViewById(R.id.soccerProgressBar);
        alertDialog();


        Button soccerFavBtn = (Button)findViewById(R.id.soccerFavNews);
        Intent goToFavouriteList = new Intent(SoccerMain.this,FavoriteNews.class);
        soccerFavBtn.setOnClickListener(click -> {
            startActivity(goToFavouriteList);
        });

        instructions = (Button)findViewById(R.id.soccerIntro);
        instructions.setOnClickListener(click -> {
            Toast.makeText(this, getResources().getString(R.string.toastmessage), Toast.LENGTH_LONG).show();
        });

        aSwitch = (Switch)findViewById(R.id.soccerSwitch);
        aSwitch.setOnCheckedChangeListener((CompoundButton compoundButton, boolean checked)-> {
            if(checked == true){
                Snackbar
                        .make(compoundButton, getResources().getString(R.string.snackbar1), Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.undo), click->compoundButton.setChecked(!checked))
                        .show();
            }else{
                Snackbar
                        .make(compoundButton, getResources().getString(R.string.snackbar2), Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.undo), click->compoundButton.setChecked(!checked))
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        String message = null;
        switch (item.getItemId()){
            case R.id.goFav:
                message = getResources().getString(R.string.favPage);
                startActivity(new Intent(SoccerMain.this, FavoriteNews.class));
                break;
            case R.id.goSoccer:
                message = getResources().getString(R.string.soccerPage);
                startActivity(new Intent(SoccerMain.this, SoccerMain.class));
                break;
            case R.id.help:
                message = getResources().getString(R.string.toast_help);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.help))
                        .setMessage(getResources().getString(R.string.helpmessage))
                        .create().show();
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch (item.getItemId()) {
            case R.id.goFav:
                message = getResources().getString(R.string.favPage);
                startActivity(new Intent(SoccerMain.this, FavoriteNews.class));
                break;
            case R.id.goSoccer:
                message= getResources().getString(R.string.soccerPage);
                startActivity(new Intent(SoccerMain.this, SoccerMain.class));
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, getResources().getString(R.string.NavigationDrawer)+message, Toast.LENGTH_LONG).show();
        return false;
    }


    private void saveRanking() {
        SharedPreferences sp = getSharedPreferences("inputNum", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor=sp.edit();
        sharedPrefEditor.putString("ranking", rating_star.getRating()+"");
        sharedPrefEditor.putString("rankingScore", rating.getText().toString());
        sharedPrefEditor.commit();
    }


    private void alertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View newView = getLayoutInflater().inflate(R.layout.star_rating, null);
        rating = newView.findViewById(R.id.soccerComment);
        rating_star = newView.findViewById(R.id.soccerRatingStar);

        alertDialogBuilder.setView(newView);
        sharedPreferences = getSharedPreferences("inputNum", Context.MODE_PRIVATE);
        String savedRanking = sharedPreferences.getString("rankingScore", "");
        String savedR = sharedPreferences.getString("ranking", "0");
        rating.setText(savedRanking);
        rating_star.setRating(Float.parseFloat(savedR));
        alertDialogBuilder.setMessage(getResources().getString(R.string.ratingMessage))
                .setPositiveButton(getResources().getString(R.string.yesmessage), (click, arg) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    Executor newThread = Executors.newSingleThreadExecutor();
                    newThread.execute(() -> {
                        try {
                            URL url = new URL("https://www.goal.com/en/feeds/news?fmt=rss");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream response = urlConnection.getInputStream();
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(false);
                            XmlPullParser xpp = factory.newPullParser();
                            xpp.setInput(response, "UTF-8");
                            int eventType = xpp.getEventType();
                            NewsItem news = null;
                            String flag = "";
                            runOnUiThread(()->{
                                progressBar.setProgress(50);
                            });

                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    if (xpp.getName().equals("item")) {
                                        news = new NewsItem();
                                    } else if (xpp.getName().equals("title")) {
                                        flag = "title";
                                    } else if (xpp.getName().equals("pubDate")) {
                                        flag = "date";
                                    } else if (xpp.getName().equals("link")) {
                                        flag = "link";
                                    } else if (xpp.getName().equals("description")) {
                                        flag = "description";
                                    } else if (xpp.getName().equals("media:thumbnail")) {
                                        news.setNewsImg(xpp.getAttributeValue(null, "url"));
                                    }
                                } else if (eventType == XmlPullParser.TEXT) {
                                    if (flag.equals("title") && news != null) {
                                        news.setNewsTitle(xpp.getText());
                                        flag = "";
                                    } else if (flag.equals("date") && news != null) {
                                        news.setNewsDate(xpp.getText());
                                        flag = "";
                                    } else if (flag.equals("link") && news != null) {
                                        news.setNewsLink(xpp.getText());
                                        flag = "";
                                    } else if (flag.equals("description") && news != null) {
                                        news.setNewsDescription(xpp.getText());
                                        flag = "";
                                    }
                                } else if (eventType == XmlPullParser.END_TAG) {
                                    if (xpp.getName().equals("item")) {
                                        elements.add(news);
                                    }
                                }
                                eventType = xpp.next();
                            }

                            runOnUiThread(()->{

                                adt.notifyDataSetChanged();
                            });
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                    saveRanking();
                })
                .create().show();
    }


    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView titleText;
        public MyRowViews(View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.soccerTitle);

            itemView.setOnClickListener(c->{
                int position=getAdapterPosition();
                Bundle dataToPass = new Bundle();
                dataToPass.putString(SOCCER_IMG,elements.get(position).getNewsImg());
                dataToPass.putString(SOCCER_TITLE,elements.get(position).getNewsTitle());
                dataToPass.putString(SOCCER_DATE,elements.get(position).getNewsDate());
                dataToPass.putString(SOCCER_DESC,elements.get(position).getNewsDescription());
                dataToPass.putString(SOCCER_LINK,elements.get(position).getNewsLink());
                if(isTablet)
                {
                    SFragment soccerFragment = new SFragment();
                    soccerFragment.setArguments( dataToPass );
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.soccerFragmentLocation, soccerFragment)
                            .commit();
                }
                else
                {
                    Intent nextActivity = new Intent(SoccerMain.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass);
                    startActivity(nextActivity);
                }
            });
        }

    }

    private class MyAdapter extends RecyclerView.Adapter<MyRowViews>{


        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.news_item,parent,false);
            MyRowViews row=new MyRowViews(view);
            return row;
        }



        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.titleText.setText(elements.get(position).getNewsTitle());
        }


        @Override
        public int getItemCount() {
            return elements.size();
        }
    }
}