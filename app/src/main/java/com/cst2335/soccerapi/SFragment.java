package com.cst2335.soccerapi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * this class is to set up the details fragment, extends Fragment
 */
public class SFragment extends Fragment {

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    ArrayList<NewsItem> newsItemArrayList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    Button addButton;
    Button favoriteButton;
    SoccerOpenHelper mySoccerOpenHelper;
    boolean aBoolean;
    ImageView imageView;
    String newsImg;
    String newsTitle;
    String newsData;
    String newsDescription;
    String newsLink;
    long newsId;

    /**
     * this method is to creat the view
     * @param inflater layoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return result
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment, container, false);
        imageView =(ImageView)result.findViewById(R.id.soccerNewsImg);

        newsImg = dataFromActivity.getString(SoccerMain.SOCCER_IMG).replace("http:","https:");
        Executor newThread= Executors.newSingleThreadExecutor();
        newThread.execute(()->{
            Bitmap image=null;
            try {
                URL imageUrl=new URL(newsImg);
                HttpURLConnection connection= (HttpURLConnection) imageUrl.openConnection();
                connection.connect();
                int responseCode=connection.getResponseCode();
                if(responseCode==200||responseCode==301){
                    image=BitmapFactory.decodeStream(connection.getInputStream());
                    Bitmap finalImage = image;
                    parentActivity.runOnUiThread(()->{
                        ImageView imageView=result.findViewById(R.id.soccerNewsImg);
                        imageView.setImageBitmap(finalImage);
                    });

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        TextView socTitle = (TextView)result.findViewById(R.id.soccerNewsTitle);
        newsTitle = dataFromActivity.getString(SoccerMain.SOCCER_TITLE);
        socTitle.setText(newsTitle);

        TextView socDate = (TextView)result.findViewById(R.id.soccerDate);
        newsData =dataFromActivity.getString(SoccerMain.SOCCER_DATE);
        socDate.setText(newsData);

        TextView socDesc = (TextView)result.findViewById(R.id.soccerDescription);
        newsDescription = dataFromActivity.getString(SoccerMain.SOCCER_DESC);
        socDesc.setText(newsDescription);

        TextView socLink = (TextView)result.findViewById(R.id.soccerLink);
        newsLink = dataFromActivity.getString(SoccerMain.SOCCER_LINK);
        socLink.setText(newsLink);

        favoriteButton = result.findViewById(R.id.goToSoccerFavBtn);
        Intent goToFavList = new Intent(container.getContext(),FavoriteNews.class);
        favoriteButton.setOnClickListener(click ->{
            startActivity(goToFavList);
        });

        Button socBrowserBtn = (Button)result.findViewById(R.id.browser);
        socBrowserBtn.setOnClickListener(click -> {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(dataFromActivity.getString(SoccerMain.SOCCER_LINK)));
            startActivity(intent);
        });

        addButton = (Button)result.findViewById(R.id.soccerSaveBtn);
        mySoccerOpenHelper = new SoccerOpenHelper(container.getContext());
        sqLiteDatabase = mySoccerOpenHelper.getWritableDatabase();
        changeBtnText();

        FrameLayout frameLayout = result.findViewById(R.id.soccerFragmentLocation);
        addButton.setOnClickListener(v -> {
            if(aBoolean){
                NewsItem soccerDel = new NewsItem();
                soccerDel.setNewsImg(newsImg);
                soccerDel.setNewsTitle(newsTitle);
                soccerDel.setNewsDate(newsData);
                soccerDel.setNewsDescription(newsDescription);
                soccerDel.setNewsLink(newsLink);
                newsItemArrayList.remove(soccerDel);
                deleteSoccer(soccerDel);
                changeBtnText();
            }else{
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(SoccerOpenHelper.COL_SOCCERIMG, newsImg);
                newRowValues.put(SoccerOpenHelper.COL_SOCCERTITLE, newsTitle);
                newRowValues.put(SoccerOpenHelper.COL_SOCCERDATE, newsData);
                newRowValues.put(SoccerOpenHelper.COL_SOCCERDESC, newsDescription);
                newRowValues.put(SoccerOpenHelper.COL_SOCCERLINK, newsLink);
                newsId = sqLiteDatabase.insert(SoccerOpenHelper.TABLE_NAME, null, newRowValues);
                changeBtnText();
            }
        });
        return result;
    }

    /**
     * create method to query and match the result wih the database,and change button text
     */
    private void changeBtnText(){
        Cursor results = sqLiteDatabase.query(false, SoccerOpenHelper.TABLE_NAME, new String[]{SoccerOpenHelper.COL_SOCCERTITLE, SoccerOpenHelper.COL_SOCCERDATE},
                SoccerOpenHelper.COL_SOCCERTITLE +" = ? and " + SoccerOpenHelper.COL_SOCCERDATE+"= ?" ,
                new String[]{String.valueOf(newsTitle),String.valueOf(newsData)}, null, null, null, null);
        if(results.getCount()>0){
            aBoolean =true;
            addButton.setText(getResources().getString(R.string.soccerRemoveFavBtn));
        }else{
            aBoolean =false;
            addButton.setText(getResources().getString(R.string.soccerSaveFavBtn));
        }
    }

    /**
     * create method to delete the specific row matched the query result in the table
     * @param soccerNews object of the Song class
     */
    protected void deleteSoccer(NewsItem soccerNews)
    {
        sqLiteDatabase.delete(SoccerOpenHelper.TABLE_NAME, SoccerOpenHelper.COL_SOCCERTITLE +" = ? and " + SoccerOpenHelper.COL_SOCCERDATE+" = ?",  new String[]{String.valueOf(newsTitle),String.valueOf(newsData)});
    }


    /**
     * this method is to set up the  onAttach function
     * @param context context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /**
         * context will either be FragmentExample for a tablet, or EmptyActivity for phone
         */
        parentActivity = (AppCompatActivity)context;
    }

}