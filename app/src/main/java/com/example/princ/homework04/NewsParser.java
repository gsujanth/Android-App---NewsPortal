package com.example.princ.homework04;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewsParser {
    public static class NewsSAXParser extends DefaultHandler {
        ArrayList<NewsArticle> newsArticles;
        NewsArticle newsArticle;
        StringBuilder innerXml;

        public static ArrayList<NewsArticle> parseNews(InputStream inputStream) throws IOException, SAXException {
            NewsSAXParser parser=new NewsSAXParser();
            Xml.parse(inputStream,Xml.Encoding.UTF_8,parser);
            return parser.newsArticles;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.newsArticles=new ArrayList<>();
            this.innerXml=new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(qName.equals("item")){
                newsArticle=new NewsArticle();
                innerXml.setLength(0);
            }else if(qName.equals("media:content")){
                newsArticle.setImageLink(attributes.getValue("url"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(newsArticle!=null) {
                if (qName.equals("title")) {
                    newsArticle.setTitle(innerXml.toString());
                } else if (qName.equals("description")) {
                    newsArticle.setDescription(innerXml.toString());
                } else if (qName.equals("link")) {
                    newsArticle.setLink(innerXml.toString());
                } else if (qName.equals("pubDate")) {
                    newsArticle.setPubDate(innerXml.toString());
                } else if (localName.equals("item")) {
                    newsArticles.add(newsArticle);
                }
                innerXml.setLength(0);
            }else{
                Log.d("demo", "endElement: "+"not initialized: "+innerXml.toString());
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            innerXml.append(ch,start,length);
        }
    }
}
