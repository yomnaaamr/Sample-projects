package com.example.news;

public class data {

    private String image;
    private String date,title,content,author,Url;

    public data(String vimage,String vdate,String vtitle ,String vcontent,String vauthor,String vUrl){

        image = vimage;
        date = vdate;
        title = vtitle;
        content = vcontent;
        author = vauthor;
        Url = vUrl;

    }

    public String getImage(){
        return image;
    }

    public String getName(){
        return date;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getAuthor(){
        return author;
    }

    public  String getUrl(){
        return Url;
    }
}
