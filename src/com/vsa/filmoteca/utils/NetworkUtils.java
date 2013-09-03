package com.vsa.filmoteca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {
	public static InputStream OpenHttpConnection(String urlString, int timeOutMillis) 
    	    throws IOException
    	    {
    	        InputStream in = null;
    	        int response = -1;

    	        URL url = new URL(urlString);
    	        URLConnection conn = url.openConnection();
    	        if (!(conn instanceof HttpURLConnection) || conn==null){
    	        	Log.d("Prueba: ","Por aqui van los tiros");
    	            throw new IOException("Not an HTTP connection");
    	            
    	        }
    	        try{
    	        	
    	            HttpURLConnection httpConn = (HttpURLConnection) conn;
    	            httpConn.setConnectTimeout(timeOutMillis);
    	            httpConn.setReadTimeout(timeOutMillis);
    	            httpConn.setAllowUserInteraction(false);
    	            httpConn.setInstanceFollowRedirects(true);
    	            httpConn.setRequestMethod("GET");
    	            httpConn.connect(); 
    	            response = httpConn.getResponseCode();                 
    	            if (response == HttpURLConnection.HTTP_OK) {
    	                in = httpConn.getInputStream();
    	            }                     
    	        }
    	        catch (Exception ex)
    	        {
    	        	ex.printStackTrace();
    	            throw new IOException("Error connecting");
    	        }
    	        
    	        return in;     
    	    }
    public static String DownloadText(String URL,int timeOutMillis)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL,timeOutMillis);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
          String str = "";
          char[] inputBuffer = new char[BUFFER_SIZE];          
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                    
                //---convert the chars to a String---
                String readString = 
                    String.copyValueOf(inputBuffer, 0, charRead);                    
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }    
        return str;        
    }
    public static ArrayList<HashMap<String,String>> getItems (int timeOutMillis){
		ArrayList<HashMap<String,String>> Peliculas=new ArrayList<HashMap<String,String>>();
    	String pagina = null;
    	String titulo,subtitulo,fecha,url;
    	pagina=DownloadText(Constants.URL_SOURCE,timeOutMillis);
    	
		if(pagina=="" || pagina==null){
			return null;
		}
		
		pagina=StringUtils.unescapeHTML(pagina);
    	HashMap<String, String> Item;
    	while(pagina.indexOf("url\">")>0){
    		Item=new HashMap<String, String>();
    		//Parseando URL de Sinopsis
    		pagina=pagina.substring(pagina.indexOf("Evento"));
    		pagina=pagina.substring(pagina.indexOf("href=\"")+6);
    		url=pagina.substring(0,pagina.indexOf("\""));
    		//Parseando el Titulo
    		pagina=pagina.substring(pagina.indexOf("url\">")+5, pagina.length());
    		titulo=pagina.substring(0, pagina.indexOf("</a>"));
    		if (titulo.indexOf("(")>0){
    			titulo=pagina.substring(0, titulo.indexOf("("));
    			subtitulo=pagina.substring(pagina.indexOf("("));
    		}else{
    			subtitulo="";
    		}
    		
    		//Parseando la fecha
    		pagina=pagina.substring(pagina.indexOf("description\">")+13,pagina.length());
    		fecha="-"+pagina.substring(0,pagina.indexOf("</span>"));
    		//Quitamos el espacio al final del título
    		while(fecha.substring(fecha.length()-1).equals(" ") ||
    				fecha.substring(fecha.length()-1).equals('\b') ||
    				fecha.substring(fecha.length()-1).equals('\t')||
    				fecha.substring(fecha.length()-1).equals('\r')){
    			fecha=fecha.substring(0,fecha.length()-1);
    			}
    		
    		
    		titulo=titulo.trim();
    		//A�ado la informacion al HashMap
    		Item.put(Constants.PARAM_ID_TITULO, "\t"+titulo);
    		Item.put(Constants.PARAM_ID_SUBTITULO, subtitulo);
    		Item.put(Constants.PARAM_ID_FECHA, fecha);
    		Item.put(Constants.PARAM_ID_URL, url);
    		//A�ado el HashMap al ArrayList
    		Peliculas.add(Item);
    		
    	}
    	return Peliculas;
    }
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}