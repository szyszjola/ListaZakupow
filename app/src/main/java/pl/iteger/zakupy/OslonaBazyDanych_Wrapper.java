package pl.iteger.zakupy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jolanta.szyszkiewicz on 2017.11.22.
 */

public class OslonaBazyDanych_Wrapper {

    private SQLiteDatabase db;
    private DatabaseHelper mOpenHelper;
    private static final String tablica_zakupow = "Zakupy";
    private static final String dataBaseName = "zakupy.db";
    private static final String produkt_kolumna = "Produkt";
    private static final String ilosc_kolumna = "Ilosc";
    private static final int VERSION = 2;

    static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("Create table " + tablica_zakupow + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + produkt_kolumna + " VARCHAR(30)," + ilosc_kolumna + " VARCHAR(30));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  " + tablica_zakupow);
            onCreate(sqLiteDatabase);
        }

        public void onDrop(SQLiteDatabase sqLiteDatabase) //moje
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  " + tablica_zakupow);
            onCreate(sqLiteDatabase);
        }
    }

    public OslonaBazyDanych_Wrapper(Context context) {
        mOpenHelper = new DatabaseHelper(context, dataBaseName, null, VERSION);
    }

    public OslonaBazyDanych_Wrapper otworz() throws SQLException {
        db = mOpenHelper.getWritableDatabase();
        return this;
    }

    public void zamknij() {
        mOpenHelper.close();
        db.close();
    }

    public ArrayList<Zakupy> pobierzWszystkieZakupy() {
        ArrayList<Zakupy> listaZakupow = new ArrayList<Zakupy>();
        Cursor c = db.rawQuery("Select * from " + tablica_zakupow, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Zakupy k = new Zakupy(c.getInt(0), c.getString(1), c.getString(2));
            c.moveToNext();
            listaZakupow.add(k);
        }

        c.close();
        return listaZakupow;
    }

    public void dodajZakup(String p, String i) {
        ContentValues cv = new ContentValues();
        cv.put(produkt_kolumna, p);
        cv.put(ilosc_kolumna, i);

        db.insertOrThrow(tablica_zakupow, null, cv);
    }

    public void edytujZakup(String p, String i, int id)
    {
        ContentValues cv = new ContentValues();
        cv.put(produkt_kolumna,p);
        cv.put(ilosc_kolumna,i);
        db.update(tablica_zakupow, cv, "ID = " + id, null);
    }

    public void usuńZakup(int id) {
        db.delete(tablica_zakupow, "ID = " + id, null);
    }

    public void UsunWszystkieZakupy()
    {
       mOpenHelper.onDrop(db);
    }

}//klasy anonimowe mają dostęp do prywatnych składników klas które je otaczają (w jednym pliku?)