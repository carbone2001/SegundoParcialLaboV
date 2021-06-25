package com.example.segundoparcial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.NetworkStatsManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, DialogInterface.OnClickListener, Handler.Callback, View.OnClickListener {
    List<Usuario> listaUsuarios = new ArrayList<>();
    Dialog dialog;
    View formularioUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONArray listaUsuariosArrayJSON = Usuario.obtenerUsuariosDelSharedPreference(this);
        this.listaUsuarios = Usuario.parseArrayJSON(listaUsuariosArrayJSON);
        TextView tvListaUsuarios = findViewById(R.id.tvListaUsuarios);
        tvListaUsuarios.setText(listaUsuariosArrayJSON.toString());


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String listaUsuariosStr = prefs.getString("listaUsuarios","no-creada");
//        JSONArray listaUsuariosArrayJSON = new JSONArray();
//        try {
//            if("no-creada".equals(listaUsuariosStr)){
//                Handler handler = new Handler(this);
//                Usuario.obtenerListaUsuariosDeLaApi(handler);
//            }
//            else{
//                JSONObject jsonObject = new JSONObject(listaUsuariosStr);
//                listaUsuariosArrayJSON = jsonObject.getJSONArray("lista");
//                TextView tvListaUsuarios = findViewById(R.id.tvListaUsuarios);
//                tvListaUsuarios.setText(listaUsuariosArrayJSON.toString());
//                this.listaUsuarios = Usuario.parseArrayJSON(listaUsuariosArrayJSON);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        if(message.arg1 == HttpThread.TEXT){
            String response = (String)message.obj;
            try {
                JSONArray jsonArray = new JSONArray(response);
                TextView tvListaUsuarios = findViewById(R.id.tvListaUsuarios);
                tvListaUsuarios.setText(jsonArray.toString());
                this.listaUsuarios = Usuario.parseArrayJSON(jsonArray);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editText = prefs.edit();
                JSONObject lista = new JSONObject();
                lista.put("lista",jsonArray);
                editText.putString("listaUsuarios",lista.toString());
                editText.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        MenuItem menuItem = menu.findItem(R.id.svBuscador);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(MainActivity.this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnAgergarUsuario){

            this.formularioUsuario = getLayoutInflater().inflate(R.layout.formulario_usuario,null);
            Spinner spRol = this.formularioUsuario.findViewById(R.id.spRol);
            String[] spRolItems = new String[]{"Supervisor","Construction Manager","Project Manager"};
            ArrayAdapter<String> spRolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,spRolItems);
            spRolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRol.setAdapter(spRolAdapter);

            ToggleButton tgAdmin = this.formularioUsuario.findViewById(R.id.tgAdmin);
            tgAdmin.setTextOff("OFF");
            tgAdmin.setTextOn("ON");
            tgAdmin.setChecked(false);


            Dialog dialog = new Dialog(
                    "Crear Usuario",
                    null,
                    "GUARDAR",
                    this,
                    "CERRAR",
                    null,
                    this.formularioUsuario);

            dialog.show(getSupportFragmentManager(),"");
            this.dialog = dialog;
        }
        return true;
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        EditText etUsername = this.dialog.getDialog().findViewById(R.id.etUsername);
        Spinner spRol = this.dialog.getDialog().findViewById(R.id.spRol);
        ToggleButton tgAdmin = this.dialog.getDialog().findViewById(R.id.tgAdmin);

        String username = etUsername.getText().toString();
        String rol = spRol.getSelectedItem().toString();
        String admin = tgAdmin.getText().toString();
        String on = tgAdmin.getTextOn().toString();
        if(!username.isEmpty()){
            int maxId = 0;
            for(Usuario usuario:this.listaUsuarios){
                if(usuario.id > maxId){
                    maxId = usuario.id;
                }
            }

            Usuario nuevoUsuario = new Usuario(
                    maxId+1,
                    username,
                    rol,
                    on.equals(admin) ? true : false);

            this.listaUsuarios.add(nuevoUsuario);

            JSONObject listaJSON = new JSONObject();

            try {
                JSONArray jsonArray = Usuario.toArrayJSON(this.listaUsuarios);
                listaJSON.put("lista",jsonArray);
                TextView tvListaUsuarios = findViewById(R.id.tvListaUsuarios);
                tvListaUsuarios.setText(jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editText = prefs.edit();
            JSONObject lista = new JSONObject();
            try {
                lista.put("lista",Usuario.toArrayJSON(this.listaUsuarios));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editText.putString("listaUsuarios",lista.toString());
            editText.commit();
        }
        else{
            Dialog dialog = new Dialog("Campo username vacio", "No puede haber campos vacios");
            dialog.show(getSupportFragmentManager(),"camposVacios");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String nombreUsuario) {
        Dialog dialog;
        for(Usuario usuario : this.listaUsuarios){
            if(usuario.username.equals(nombreUsuario)){
                dialog = new Dialog("Usuario encontrado","El rol del usuario es "+usuario.rol,"CERRAR",null,null,null);
                dialog.show(getSupportFragmentManager(),"encontrado");
                return true;
            }
        }
        dialog = new Dialog("Usuario no encontrado","El usuario "+nombreUsuario+" no esta dentro de la lista");
        dialog.show(getSupportFragmentManager(),"noEncontrado");

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}