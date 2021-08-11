package com.cst2335.soccerapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class FavoriteNews extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    ArrayList<NewsItem> elements=new ArrayList<>();
    MyAdapter adt=new MyAdapter();
    public static final String SOCCER_TITLE = "TITLE";
    public static final String SOCCER_IMG = "IMG";
    public static final String SOCCER_DATE = "DATE";
    public static final String SOCCER_DESC = "DESC";
    public static final String SOCCER_LINK = "LINK";
    private boolean isTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_news);
        loadDataFromDatabase();
        RecyclerView myList = findViewById(R.id.soccerFavNewsList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        myList.setLayoutManager(layoutManager);
        myList.setAdapter(adt);

        ImageView goToHome = findViewById(R.id.backHome);
        Intent goBackHome = new Intent(FavoriteNews.this, SoccerMain.class);
        goToHome.setOnClickListener(click ->{
            startActivity(goBackHome);

        });
        isTablet = findViewById(R.id.soccerFragmentLocation) != null;
    }

    private void loadDataFromDatabase() {

        SoccerOpenHelper dbOpener = new SoccerOpenHelper(this);
        sqLiteDatabase = dbOpener.getWritableDatabase();


        String [] columns = {SoccerOpenHelper.COL_ID, SoccerOpenHelper.COL_SOCCERIMG, SoccerOpenHelper.COL_SOCCERTITLE, SoccerOpenHelper.COL_SOCCERDATE, SoccerOpenHelper.COL_SOCCERDESC, SoccerOpenHelper.COL_SOCCERLINK};
        Cursor results = sqLiteDatabase.query(false, SoccerOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int soccerImgColumnIndex = results.getColumnIndex(SoccerOpenHelper.COL_SOCCERIMG);
        int soccerTitleColumnIndex = results.getColumnIndex(SoccerOpenHelper.COL_SOCCERTITLE);
        int soccerDateColumnIndex = results.getColumnIndex(SoccerOpenHelper.COL_SOCCERDATE);
        int soccerDescColumnIndex = results.getColumnIndex(SoccerOpenHelper.COL_SOCCERDESC);
        int soccerLinkColumnIndex = results.getColumnIndex(SoccerOpenHelper.COL_SOCCERLINK);


        while(results.moveToNext())
        {
            String img = results.getString(soccerImgColumnIndex);
            String title = results.getString(soccerTitleColumnIndex);
            String date = results.getString(soccerDateColumnIndex);
            String desc = results.getString(soccerDescColumnIndex);
            String link = results.getString(soccerLinkColumnIndex);


            NewsItem soccerFav = new NewsItem();
            soccerFav.setNewsImg(img);
            soccerFav.setNewsTitle(title);
            soccerFav.setNewsDate(date);
            soccerFav.setNewsDescription(desc);
            soccerFav.setNewsLink(link);
            elements.add(soccerFav);
        }

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
                    Intent nextActivity = new Intent(FavoriteNews.this, EmptyActivity.class);
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