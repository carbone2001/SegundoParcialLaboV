package com.example.segundoparcial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedInputStream;

public class Dialog extends DialogFragment {
    private String titulo;
    private String mensaje;
    private String btnPositivo;
    private String btnNegative;
    private DialogInterface.OnClickListener onClickPositive;
    private DialogInterface.OnClickListener onClickNegative;
    private View view;

    public Dialog(
            String titulo,
            String mensaje,
            String btnPositivo,
            DialogInterface.OnClickListener onClickPositivo,
            String btnNegative,
            DialogInterface.OnClickListener onClickNegative
    ) {

        this.titulo = titulo;
        this.mensaje = mensaje;
        this.btnPositivo = btnPositivo;
        this.btnNegative = btnNegative;
        this.onClickPositive = onClickPositivo;
        this.onClickNegative = onClickNegative;
    }

    public Dialog(
            String titulo,
            String mensaje,
            String btnPositivo,
            DialogInterface.OnClickListener onClickPositivo,
            String btnNegativo,
            DialogInterface.OnClickListener onClickNegativo,
            View view
    ) {
        this(titulo, mensaje, btnPositivo, onClickPositivo, btnNegativo, onClickNegativo);
        this.view = view;
    }

    public Dialog(String titulo, String mensaje) {
        this(titulo, mensaje, "CERRAR", null, null, null);
    }


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.titulo);
        builder.setMessage(this.mensaje);
        builder.setPositiveButton(this.btnPositivo, this.onClickPositive);
        builder.setNeutralButton(this.btnNegative, this.onClickNegative);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


}
