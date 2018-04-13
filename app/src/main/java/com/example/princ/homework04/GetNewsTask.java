package com.example.princ.homework04;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetNewsTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {

    INewsArticle iNewsArticle;
    Context ctx;
    ProgressDialog pDlg;

    public GetNewsTask(INewsArticle iNewsArticle,Context ctx) {
        this.iNewsArticle = iNewsArticle;
        this.ctx=ctx;
        pDlg = new ProgressDialog(ctx);
        pDlg.setMessage("Loading News...");
        pDlg.setCancelable(false);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDlg.show();
    }


    @Override
    protected ArrayList<NewsArticle> doInBackground(String... strings) {
        HttpURLConnection con=null;
        ArrayList<NewsArticle> result = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            for (int j = 0; j <10000 ; j++) {

            }
        }
        try {
            URL url = new URL(strings[0]);
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result=NewsParser.NewsSAXParser.parseNews(con.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsArticle> s) {
        iNewsArticle.handleNewsArticle(s);
        pDlg.dismiss();
    }

    public static interface INewsArticle {
        public void handleNewsArticle(ArrayList<NewsArticle> s);
    }
}
