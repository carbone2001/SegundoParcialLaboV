package com.example.segundoparcial;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    public int id;
    public String username;
    public String rol;
    public boolean admin;

    public Usuario(int id, String username, String rol, boolean admin) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.admin = admin;
    }

    private Usuario() {}

    public static Usuario parserJSON(JSONObject jsonObject){
        Usuario usuario = new Usuario();
        try {
            usuario.id = jsonObject.getInt("id");
            usuario.username = jsonObject.getString("username");
            usuario.rol = jsonObject.getString("rol");
            usuario.admin = jsonObject.getBoolean("admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public JSONObject toJSON(){
        JSONObject usuarioJSON = new JSONObject();
        try {
            usuarioJSON.put("id",this.id);
            usuarioJSON.put("username",this.username);
            usuarioJSON.put("rol",this.rol);
            usuarioJSON.put("admin",this.admin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usuarioJSON;
    }

    public static void obtenerListaUsuariosDeLaApi(Handler handler){
        HttpThread httpThread = new HttpThread("http://192.168.0.69:3001/usuarios","GET",handler,HttpThread.TEXT);
        httpThread.start();
    }

    public static List<Usuario> parseArrayJSON(JSONArray listaUsuariosArrayJSON){
        Log.d("INFORMACION DEL PARSER ARRAY",listaUsuariosArrayJSON.toString());
        List<Usuario> listaUsuarios = new ArrayList<>();
        for(int i=0;i<listaUsuariosArrayJSON.length();i++){
            JSONObject current = null;
            try {
                current = listaUsuariosArrayJSON.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listaUsuarios.add(Usuario.parserJSON(current));
        }
        return listaUsuarios;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                ", admin=" + admin +
                '}';
    }

    public static JSONArray toArrayJSON(List<Usuario> usuarios){
        JSONArray jsonArray = new JSONArray();
        for(Usuario usuario : usuarios){
            jsonArray.put(usuario.toJSON());
        }
        return jsonArray;
    }

    public static JSONArray obtenerUsuariosDelSharedPreference(MainActivity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String listaUsuariosStr = prefs.getString("listaUsuarios","no-creada");
        JSONArray listaUsuariosArrayJSON = new JSONArray();
        try {
            if("no-creada".equals(listaUsuariosStr)){
                Handler handler = new Handler(activity);
                Usuario.obtenerListaUsuariosDeLaApi(handler);
            }
            else{
                JSONObject jsonObject = new JSONObject(listaUsuariosStr);
                listaUsuariosArrayJSON = jsonObject.getJSONArray("lista");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  listaUsuariosArrayJSON;
    }


}
