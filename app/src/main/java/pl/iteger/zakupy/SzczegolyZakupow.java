package pl.iteger.zakupy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SzczegolyZakupow extends AppCompatActivity {

    ImageButton usun;
    ImageButton edytuj;
    EditText produkt;
    EditText ilosc;
    OslonaBazyDanych_Wrapper bazaDanych;
    private Zakupy zakupy;
    public static final String EXTRA_ZAKUPY_ID = "extra_zakupy_id";
    public static final String EXTRA_ZAKUPY = "extra_zakupy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szczegoly_zakupow);
        bazaDanych = new OslonaBazyDanych_Wrapper(this);
        usun = findViewById(R.id.usun_btn);
        edytuj = findViewById(R.id.edytuj_btn);
        produkt = findViewById(R.id.nazwa_produktu);
        ilosc = findViewById(R.id.nazwa_ilosci);
        if(!getIntent().hasExtra(EXTRA_ZAKUPY) || !getIntent().hasExtra(EXTRA_ZAKUPY_ID)) // w yprzypadku jakiegoś błędu z id, czy jakoś, na wszelki wypadek zabezpieczymy się wyjątkiem
        {
            throw new RuntimeException("Nie przekazales orderu!");
        }
        zakupy = getIntent().getParcelableExtra(EXTRA_ZAKUPY);
        produkt.setText(zakupy.getProdukt());
        ilosc.setText(zakupy.getIlosc());

        usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtworzDialog().show();

            }
        });

        edytuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtworzDialogZmiany().show();
            }
        });
    }

    public AlertDialog UtworzDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy na pewno chcesz usunąć produkt z listy?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        setResult(RESULT_OK);

                        finish();
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

    public AlertDialog UtworzDialogZmiany() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy na pewno chcesz zapisać zmiany?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Zakupy zakupy_edytowane = new Zakupy();
                        zakupy_edytowane.setId(zakupy.getId());
                        zakupy_edytowane.setProdukt(produkt.getText().toString());
                        zakupy_edytowane.setIlosc(ilosc.getText().toString());

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_ZAKUPY, zakupy_edytowane);
                        setResult(200 , intent);
                        finish();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(201);
                        finish();
                    }
                })
                //Set your icon here
                .setTitle("Pytanie")
                .setIcon(R.mipmap.ic_question);
        return builder.create();
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

                UtworzDialogMenu().show();

                return true;
        }
        return false;

    }


    public AlertDialog UtworzDialogMenu() {
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
