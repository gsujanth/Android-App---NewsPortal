package com.example.princ.homework04;

/*Assignment# - Homework04
  Names : Sujanth Babu Guntupalli
          Mounika Yendluri
*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsTask.INewsArticle{

    Button go;
    ImageButton pButton, nButton;
    TextView viewCategory, titleTV, dateTV, descTV, articleNoTV;
    ImageView iv;
    AlertDialog.Builder builder;
    String[] categories={"Top Stories","World","U.S.","Business","Politics","Technology","Health","Entertainment","Travel","Living","Most Recent"};
    ArrayList<NewsArticle> maNewsArticles=new ArrayList<>();
    int newsArticleId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        go = (Button) findViewById(R.id.goButton);
        pButton = (ImageButton) findViewById(R.id.pButton);
        nButton = (ImageButton) findViewById(R.id.nButton);
        viewCategory = (TextView) findViewById(R.id.categoryTV);
        titleTV = (TextView) findViewById(R.id.titleTV);
        dateTV = (TextView) findViewById(R.id.dateTV);
        descTV = (TextView) findViewById(R.id.descTV);
        articleNoTV = (TextView) findViewById(R.id.articleNoTV);
        iv=(ImageView) findViewById(R.id.imageView);

        iv.setVisibility(View.GONE);
        pButton.setEnabled(false);
        nButton.setEnabled(false);
        titleTV.setMovementMethod(LinkMovementMethod.getInstance());

        builder = new AlertDialog.Builder(this);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    Toast.makeText(MainActivity.this, "Is Connected", Toast.LENGTH_SHORT).show();
                    builder.setTitle("Choose Category")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    viewCategory.setText(categories[item]);
                                    maNewsArticles.clear();
                                    newsArticleId=0;
                                    if(isConnected()) {
                                        if(categories[item].equals("Top Stories")) {
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_topstories.rss");
                                        } else if(categories[item].equals("World")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_world.rss");
                                        }else if(categories[item].equals("U.S.")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_us.rss");
                                        }else if(categories[item].equals("Business")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/money_latest.rss");
                                        }else if(categories[item].equals("Politics")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_allpolitics.rss");
                                        }else if(categories[item].equals("Technology")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss");
                                        }else if(categories[item].equals("Health")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_health.rss");
                                        }else if(categories[item].equals("Entertainment")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_showbiz.rss");
                                        }else if(categories[item].equals("Travel")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_travel.rss");
                                        }else if(categories[item].equals("Living")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_living.rss");
                                        }else if(categories[item].equals("Most Recent")){
                                            new GetNewsTask(MainActivity.this, MainActivity.this).execute("http://rss.cnn.com/rss/cnn_latest.rss");
                                        }else{
                                            Toast.makeText(MainActivity.this, "Wrong URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).create().show();
                } else {
                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (maNewsArticles != null && !maNewsArticles.isEmpty()) {
                        if (newsArticleId > 0) {
                            newsArticleId -= 1;
                            if(maNewsArticles.get(newsArticleId).getTitle()!=null&&!maNewsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(Html.fromHtml("<a href="+maNewsArticles.get(newsArticleId).getLink()+">"+maNewsArticles.get(newsArticleId).getTitle()+"</a>"));
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(maNewsArticles.get(newsArticleId).getPubDate()!=null&&!maNewsArticles.get(newsArticleId).getPubDate().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getPubDate()!="null") {
                                dateTV.setText(maNewsArticles.get(newsArticleId).getPubDate());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(maNewsArticles.get(newsArticleId).getDescription()!=null&&!maNewsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(maNewsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),maNewsArticles.size()));
                            if(isConnected()) {
                                Picasso.with(MainActivity.this).load(maNewsArticles.get(newsArticleId).getImageLink()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else if (newsArticleId == 0) {
                            newsArticleId = maNewsArticles.size() - 1;
                            if(maNewsArticles.get(newsArticleId).getTitle()!=null&&!maNewsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(Html.fromHtml("<a href="+maNewsArticles.get(newsArticleId).getLink()+">"+maNewsArticles.get(newsArticleId).getTitle()+"</a>"));
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(maNewsArticles.get(newsArticleId).getPubDate()!=null&&!maNewsArticles.get(newsArticleId).getPubDate().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getPubDate()!="null") {
                                dateTV.setText(maNewsArticles.get(newsArticleId).getPubDate());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(maNewsArticles.get(newsArticleId).getDescription()!=null&&!maNewsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(maNewsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),maNewsArticles.size()));
                            if(isConnected()) {
                                Picasso.with(MainActivity.this).load(maNewsArticles.get(newsArticleId).getImageLink()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No News to dispaly", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){

                }
            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (maNewsArticles != null && !maNewsArticles.isEmpty()) {
                        if (newsArticleId < maNewsArticles.size() - 1) {
                            newsArticleId += 1;
                            if(maNewsArticles.get(newsArticleId).getTitle()!=null&&!maNewsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(Html.fromHtml("<a href="+maNewsArticles.get(newsArticleId).getLink()+">"+maNewsArticles.get(newsArticleId).getTitle()+"</a>"));
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(maNewsArticles.get(newsArticleId).getPubDate()!=null&&!maNewsArticles.get(newsArticleId).getPubDate().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getPubDate()!="null") {
                                dateTV.setText(maNewsArticles.get(newsArticleId).getPubDate());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(maNewsArticles.get(newsArticleId).getDescription()!=null&&!maNewsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(maNewsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),maNewsArticles.size()));
                            if(isConnected()) {
                                Picasso.with(MainActivity.this).load(maNewsArticles.get(newsArticleId).getImageLink()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else if (newsArticleId == maNewsArticles.size() - 1) {
                            newsArticleId = 0;
                            if(maNewsArticles.get(newsArticleId).getTitle()!=null&&!maNewsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(Html.fromHtml("<a href="+maNewsArticles.get(newsArticleId).getLink()+">"+maNewsArticles.get(newsArticleId).getTitle()+"</a>"));
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(maNewsArticles.get(newsArticleId).getPubDate()!=null&&!maNewsArticles.get(newsArticleId).getPubDate().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getPubDate()!="null") {
                                dateTV.setText(maNewsArticles.get(newsArticleId).getPubDate());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(maNewsArticles.get(newsArticleId).getDescription()!=null&&!maNewsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(maNewsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),maNewsArticles.size()));
                            if(isConnected()) {
                                Picasso.with(MainActivity.this).load(maNewsArticles.get(newsArticleId).getImageLink()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No News to Display", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){

                }
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(maNewsArticles.get(newsArticleId).getLink()));
                    startActivity(browserIntent);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void handleNewsArticle(ArrayList<NewsArticle> s) {
        if(s!=null&&!s.isEmpty()) {
            maNewsArticles = s;
            iv.setVisibility(View.VISIBLE);
            if(maNewsArticles.get(newsArticleId).getTitle()!=null&&!maNewsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getTitle()!="null") {
                titleTV.setText(Html.fromHtml("<a href="+maNewsArticles.get(newsArticleId).getLink()+">"+maNewsArticles.get(newsArticleId).getTitle()+"</a>"));
            }else{
                titleTV.setText("No Title Found");
            }
            if(maNewsArticles.get(newsArticleId).getPubDate()!=null&&!maNewsArticles.get(newsArticleId).getPubDate().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getPubDate()!="null") {
                dateTV.setText(maNewsArticles.get(newsArticleId).getPubDate());
            }else{
                dateTV.setText("No Published Date found");
            }
            if(maNewsArticles.get(newsArticleId).getDescription()!=null&&!maNewsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&maNewsArticles.get(newsArticleId).getDescription()!="null") {
                descTV.setText(maNewsArticles.get(newsArticleId).getDescription());
            }else{
                descTV.setText("No Description Found");
            }
            articleNoTV.setText(String.format("%d out of %d", (newsArticleId + 1), maNewsArticles.size()));
            if (isConnected()) {
                Picasso.with(MainActivity.this).load(maNewsArticles.get(newsArticleId).getImageLink()).into(iv);
            } else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
            if(maNewsArticles.size()>1) {
                pButton.setEnabled(true);
                nButton.setEnabled(true);
            }else{
                pButton.setEnabled(false);
                nButton.setEnabled(false);
            }
        }else{
            pButton.setEnabled(false);
            nButton.setEnabled(false);
            titleTV.setText(null);
            descTV.setText(null);
            dateTV.setText(null);
            articleNoTV.setText(String.format("%d out of %d", 0,0));
            iv.setImageBitmap(null);
            Toast.makeText(this, "No News Found", Toast.LENGTH_SHORT).show();
        }
    }
}

/*
textView.setOnClickListener(new OnClickListener() {

    @Override
    public void onClick(View view)
    {
         Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(PASTE_YOUR_URL_HERE));
         startActivity(browser);
    }

});


android:autoLink="web"
 */
