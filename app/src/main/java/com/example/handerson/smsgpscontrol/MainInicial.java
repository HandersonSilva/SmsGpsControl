package com.example.handerson.smsgpscontrol;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inicial);

        final EditText edtnome = (EditText)findViewById(R.id.editText_nome);

        final EditText edtNumero = (EditText)findViewById(R.id.editText_numero);

        Button btnOK = (Button)findViewById(R.id.button_aceitar);


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtnome.getText().toString();
                String numero = edtNumero.getText().toString();

                Intent it = new Intent();
                it.putExtra("nome",nome);
                it.putExtra("numero", numero);
                setResult(RESULT_OK, it);
                finish();

            }
        });
    }
}
