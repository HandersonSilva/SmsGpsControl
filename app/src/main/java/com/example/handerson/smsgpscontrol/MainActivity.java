package com.example.handerson.smsgpscontrol;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static String numberStr;
    static  String nomeStr;
    static String msgStr;
    EditText number;
    EditText msg;
    Button btnEnviarSms;
    IntentFilter intentFilter;
    private static final int RESULT_NUMERO=1;
    public  static final String CONFIG = "ConfigFile";

    private BroadcastReceiver intentReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView interacao = (TextView)findViewById(R.id.textview_Interacao);
            interacao.setText(intent.getExtras().getString("sms"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //intentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        number = (EditText) findViewById(R.id.editText_numero);
       // msg = (EditText) findViewById(R.id.editText_digitaSms);
       // btnEnviarSms = (Button) findViewById(R.id.button_enviar);

        //Recuperando dados de configuração
        SharedPreferences settings = getSharedPreferences(CONFIG, 0);
        nomeStr = settings.getString("config_nome",null);
        numberStr = settings.getString("config_numero",null);

        //Abrir pagina inicial
        if(numberStr == null){
            Intent intentInicial = new Intent( MainActivity.this,MainInicial.class) ;
            startActivityForResult(intentInicial,RESULT_NUMERO);

        }
        mostrarDados(nomeStr,numberStr);
      /*  btnEnviarSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // msgStr = msg.getText().toString();

                //Enviar mensagem
               // sendSms(numberStr, msgStr);
            }
        });*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                numberStr = data.getStringExtra("numero");
                nomeStr = data.getStringExtra("nome");
                mostrarDados(nomeStr,numberStr);
                //Salvando dados de configuração
                SharedPreferences settings = getSharedPreferences(CONFIG, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("config_nome",nomeStr);
                editor.putString("config_numero", numberStr);
                // Commit the edits!
                editor.commit();
            }
    }
    public  void mostrarDados(String nome,String numero){
        if(numero!= null & numero != ""){
            TextView textNumero =(TextView)findViewById(R.id.textView_num);
            TextView textNome = (TextView)findViewById(R.id.textView_nome);
            textNome.setText(nome);
            textNumero.setText(numero);
        }else {
            TextView textNome = (TextView)findViewById(R.id.textView_nome);
            textNome.setText("Erro! ");
            TextView textNumero =(TextView)findViewById(R.id.textView_num);
            textNumero.setText("Erro!");
        }
    }

    //Função enviar mensagem
    protected void sendSms(String numero , String msg){
        String SENT ="Mensagem Enviada";
        String DELIVERED ="Mensagem Entregue";

        PendingIntent sendPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        PendingIntent deliveredPI= PendingIntent.getBroadcast(this,0, new Intent(DELIVERED),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"Mensagem Enviada!!!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),"O Envio Falhou!!!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,"Sem Serviço!!!",Toast.LENGTH_SHORT).show();
                }
            }
        },new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),"Mensagem Recebida!!!",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(),"O Recebimento  Falhou!!!",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getBaseContext()," Dispositivo está Offline!!!",Toast.LENGTH_SHORT).show();
                        break;
                    }
            }
        },new IntentFilter(DELIVERED));


        SmsManager sms = SmsManager.getDefault();//Obtendo o gerenciador de mensagem de texto
        sms.sendTextMessage(numero,null,msg,sendPI,deliveredPI);


    }
    protected void onResume(){
        registerReceiver(intentReceived,intentFilter);
        super.onResume();
    }
    protected void onPause(){
        unregisterReceiver(intentReceived);
        super.onPause();
    }



}
