package com.example.segundoparcial;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConexion {
    public static String request(String urlString, String method) {
        String response = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();
            int resposeCode = urlConnection.getResponseCode();
            if(resposeCode == 200){
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = procesarRespuestaStream(is);
                is.close();
                return baos.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    private static ByteArrayOutputStream procesarRespuestaStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int cantidad = 0;
            while (((cantidad = is.read(buffer)) != -1)) {
                baos.write(buffer, 0, cantidad);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }
}
