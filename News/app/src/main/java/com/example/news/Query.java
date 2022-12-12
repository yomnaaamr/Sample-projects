package com.example.news;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Query {

    private static final String log_TAG = News_MainActivity.class.getName();

    private Query(){

    }


    private static URL createUrl (String stringurl){
        URL url = null;
        try{
            url = new URL(stringurl);
        } catch (MalformedURLException e) {
            Log.e(log_TAG,"Problem Building the Url",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String JsonResponse = "";

        if(url==null){
            return JsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            }else {
                Log.e(log_TAG,"Error Response Code :"+urlConnection.getResponseCode());
            }

            }catch(IOException e){
            Log.e(log_TAG,"Problem Retrieving json results",e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return JsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }


    private static List<data> extractFeatureFromJson(String newJson){

        if(TextUtils.isEmpty(newJson)){
            return null;
        }

        List<data> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(newJson);
            JSONArray array = root.getJSONArray("results");
            String image ="";
            for(int i=0;i<array.length();i++){
                JSONObject item = array.getJSONObject(i);
                JSONArray load = item.getJSONArray("multimedia");
                for(int k=0;k<load.length();k++){

                    JSONObject photo = load.getJSONObject(k);
                     image = photo.getString("url");

                }
                String date = item.getString("published_date");
                String author = item.getString("byline");
                String title = item.getString("title");
                String content = item.getString("abstract");
                String Url = item.getString("url");

                data input = new data(image,date,title,content,author,Url);
                news.add(input);

            }
        } catch (JSONException e) {
           Log.e(log_TAG,"Problem Parsing the Json results",e);
        }
        return news;
    }

    public static List<data> fetchNewsData(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (Exception e) {
            Log.e(log_TAG,"Problem Making Http Request",e);
        }

        List<data> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
}
