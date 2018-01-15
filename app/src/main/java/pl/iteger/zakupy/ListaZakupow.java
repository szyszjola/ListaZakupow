package pl.iteger.zakupy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by jolanta.szyszkiewicz on 2017.11.22.
 */

public class ListaZakupow extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {


    OslonaBazyDanych_Wrapper bazaDanych;
    ArrayAdapter<Zakupy> mAdapter;
    private ListView mList;
    public final static int REQUEST_CODE_SZCZEGOLY_ZAKUPOW = 0;
    public final static int MAIN_ACTIVITY = 5;
    public static final String EXTRA_ZAKUPY_ID = "extra_zakupy_id";
    public static final String EXTRA_ZAKUPY = "extra_zakupy";
    ImageButton dodaj;
    private int pozycja = 0;
    private ArrayList<Zakupy> listaZakupow = new ArrayList<Zakupy>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        dodaj = findViewById(R.id.dodaj_produkt_btn);
        bazaDanych = new OslonaBazyDanych_Wrapper(this);
        bazaDanych.otworz();
        listaZakupow = bazaDanych.pobierzWszystkieZakupy();
        bazaDanych.zamknij();
        // kontaktAdapter = new ArrayAdapter<Zakupy>(this, android.R.layout.simple_list_item_1, listaZakupow);
        mAdapter = new myAdapter(this, R.layout.item_wiadomosci, listaZakupow);
        mList = findViewById(R.id.lista);
        mList.setAdapter(mAdapter);
        mList.setOnItemLongClickListener(this);
        mList.setOnItemClickListener(this);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, MAIN_ACTIVITY);

            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        bazaDanych.otworz();
        bazaDanych.usuńZakup(listaZakupow.get(i).id);
        bazaDanych.zamknij();
        mAdapter.remove(listaZakupow.get(i));
        mAdapter.notifyDataSetChanged();

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Zakupy zakupy = mAdapter.getItem(i);
        Intent intent = new Intent(this, SzczegolyZakupow.class); //podnieniamy NewsDetailsActivity.class na akcje którą napisaliśmy w manifeście
        intent.putExtra(EXTRA_ZAKUPY_ID, zakupy.getId());
        pozycja = i;
        intent.putExtra(EXTRA_ZAKUPY, zakupy);
        startActivityForResult(intent, REQUEST_CODE_SZCZEGOLY_ZAKUPOW);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SZCZEGOLY_ZAKUPOW:
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(this, "Usunieto!", Toast.LENGTH_SHORT).show();
                        bazaDanych.otworz();
                        bazaDanych.usuńZakup(listaZakupow.get(pozycja).id);
                        bazaDanych.zamknij();
                        mAdapter.remove(listaZakupow.get(pozycja));
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 200:
                        Toast.makeText(this, "Edytowano!", Toast.LENGTH_SHORT).show();
                        final Zakupy zakupy = data.getParcelableExtra(SzczegolyZakupow.EXTRA_ZAKUPY); //ctr + alt + v
                        bazaDanych.otworz();
                        bazaDanych.edytujZakup(zakupy.getProdukt(), zakupy.getIlosc(), zakupy.getId());
                        bazaDanych.zamknij();
                        recreateActivityCompat(this);
                        break;
                    case 201:
                        break;
                }
            case MAIN_ACTIVITY:
                recreateActivityCompat(this);
                break;
        }

    }

    public static final void recreateActivityCompat(final Activity a) {   //odświeżanie apki
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listamenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.WyczyśćListeMenuSzczegol:

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