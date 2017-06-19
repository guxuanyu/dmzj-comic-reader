package com.example.ganger.dmzjapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ganger on 2017/6/5.
 */
public class XmlParser {
    public static List parserNews(InputStream is){
        XmlPullParser xmlPullParser= Xml.newPullParser();
        List list=new ArrayList();
        try {
            xmlPullParser.setInput(is,"UTF-8");
            //xmlPullParser.setInput();
            int eventType=xmlPullParser.getEventType();// 获取事件类型
            AuthorAndDes aad=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        String starttagName=xmlPullParser.getName();
                        if("description".equals(starttagName)){
                            eventType=xmlPullParser.next();

                            String des=xmlPullParser.getText();
                            if(des.contains("<")) {
                                aad.setDes(des);
                                //Log.i("des------ ",des);
                            }
                        }
                        else  if("item".equals(starttagName)) {
                            aad=new AuthorAndDes();
                        }
                        else  if("author".equals(starttagName)) {
                            eventType=xmlPullParser.next();
                            String author=xmlPullParser.getText();
                            aad.setAuthor(author);
                            //Log.i("author","== "+author);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endtagName=xmlPullParser.getName();
                        if("item".equals(endtagName)) {
                            list.add(aad);
                            aad=null;
                        }
                        break;

                }
                eventType=xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List parserItems(List list){
        List<News> newss=new ArrayList<>();
        if(list!=null){
            for(Object o:list){
                String item="<xml>"+((AuthorAndDes)o).getDes()+"</xml>";
                item=item.replaceAll("&","&amp;");
                item=item.replaceAll("</br>","");
                //String item=(String)o;
                //String item="<xml version='1.1'><a href='http://manhua.dmzj.com/wyxmfsn/66014.shtml?from=rssReader&time=1496639295' title='我，英雄，魔法少女' target='_blank'>第33话</a></br><a href='http://manhua.dmzj.com/wyxmfsn/?from=rssReader&chapterid=66014' target='_blank'><img src='http://images.dmzj.com/webpic/7/wyxmfsn.jpg' width='150' height='200'/></a></xml>";
               // Log.i("str",item);
                InputStream inputStream=new ByteArrayInputStream(item.getBytes());
                XmlPullParser xmlPullParser= Xml.newPullParser();
                try {
                    xmlPullParser.setInput(inputStream,"UTF-8");
                    //xmlPullParser.setInput();
                    int eventType=xmlPullParser.getEventType();// 获取事件类型
                    News news=null;
                    while(eventType!=XmlPullParser.END_DOCUMENT){
                        switch(eventType){
                            case XmlPullParser.START_TAG:
                                String starttagName=xmlPullParser.getName();
                                //Log.i("startTag---------- ",starttagName);
                                if("xml".equals(starttagName)) {
                                    news=new News();
                                }
                                if("a".equals(starttagName)){
                                    //Log.i("aaaaa","aaaaa");
                                    if(xmlPullParser.getAttributeCount()==3) {

                                        String href1 = xmlPullParser.getAttributeValue(0);
                                        news.setRecentUrl(href1);
                                        //Log.i("href1------ ", href1);
                                        String title = xmlPullParser.getAttributeValue(1);
                                       // Log.i("title------ ", title);
                                        news.setTitle(title);
                                        String des = xmlPullParser.nextText();
                                        //list.add(des);
                                      //  Log.i("des------ ", des);
                                        news.setRecentTitle(des);

                                    }
                                    if(xmlPullParser.getAttributeCount()==2) {
                                        String href2 = xmlPullParser.getAttributeValue(0);
                                        //Log.i("href2------ ", href2);
                                        news.setAllUrl(href2);
                                    }
                                }
                                else if("img".equals(starttagName)){
                                    String imgUrl=xmlPullParser.getAttributeValue(0);
                                    news.setImageUrl(imgUrl);
                                }
                                //Log.i("startend---------- ","startend");
                                break;
                            case XmlPullParser.END_TAG:
                                String endtagName=xmlPullParser.getName();
                               // Log.i("endtTag---------- ","endtagName");
                                //Log.i("endtTag---------- ",endtagName);
                                if("xml".equals(endtagName)) {
                                    news.setAuthor(((AuthorAndDes)o).getAuthor());
                                    newss.add(news);
                                    //Log.i("news   ",news.toString());
                                    news=null;
                                }
                                break;

                        }
                        eventType=xmlPullParser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return newss;
    }
//
//    public static void testParser(){
//
//    }
}
