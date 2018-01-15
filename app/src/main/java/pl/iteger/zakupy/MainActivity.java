package pl.iteger.zakupy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText produkt;
    EditText ilosc;
    Button dodaj;
    OslonaBazyDanych_Wrapper bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        bazaDanych = new OslonaBazyDanych_Wrapper(this);
        dodaj = findViewById(R.id.button);
        produkt = findViewById(R.id.produkt);
        ilosc = findViewById(R.id.ilosc);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String p = produkt.getText().toString();
                String i = ilosc.getText().toString();
                //    Capture sign value and unit
                if (i.equals("")) {
                    i = "1";
                }
                if (!p.equals("") && i.matches("([+\\-])?((?:\\d+/|(?:\\d+|^|\\s)\\.)?\\d+)\\s*([^\\s\\d+\\-.,:;^/]+(?:\\^\\d+(?:$|(?=[\\s:;/])))?(?:/[^\\s\\d+\\-.,:;^/]+(?:\\^\\d+(?:$|(?=[\\s:;/])))?)*)?")) {   //".*\\d+.*"
                    bazaDanych.otworz();
                    bazaDanych.dodajZakup(p, i);
                    bazaDanych.zamknij();
                    ilosc.setText("");
                    produkt.setText("");
                    finish();
                } else {
                    if(!i.matches("([+\\-])?((?:\\d+\\/|(?:\\d+|^|\\s)\\.)?\\d+)\\s*([^\\s\\d+\\-.,:;^\\/]+(?:\\^\\d+(?:$|(?=[\\s:;\\/])))?(?:\\/[^\\s\\d+\\-.,:;^\\/]+(?:\\^\\d+(?:$|(?=[\\s:;\\/])))?)*)?")) //".*\\d+.*"
                    {
                        Toast.makeText(getApplicationContext(), "Nie podałeś żadnej poprawnej jednostki lub cyfry!!", Toast.LENGTH_SHORT).show();
                    }
                    else if(p.equals("")) {
                        Toast.makeText(getApplicationContext(), "Nie można dodać produktu bez nazwy!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ListaZakupowMenu:
                Intent i = new Intent(this, ListaZakupow.class);
                startActivity(i);
                return true;
            case R.id.WyczyśćListeMenu:

                UtworzDialog().show();

                return true;
        }
        return false;

    }


    public AlertDialog UtworzDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy na pewno chcesz wyczyścić liste?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bazaDanych.otworz();
                        bazaDanych.UsunWszystkieZakupy();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                //Set your icon here
                .setTitle("Uwaga!")
                .setIcon(R.mipmap.ic_alert);
        return builder.create();
    }

}
