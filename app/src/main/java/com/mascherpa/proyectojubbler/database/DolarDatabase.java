package com.mascherpa.proyectojubbler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mascherpa.proyectojubbler.model.Dolar;

import java.util.ArrayList;
import java.util.List;

public class DolarDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dolarDatabase.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "dolar_table";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BUY = "buy";
    private static final String COLUMN_SELL = "sell";
    private static final String COLUMN_UPDATE_DATE = "updateDate";

    public DolarDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BUY + " TEXT, " +
                COLUMN_SELL + " TEXT, " +
                COLUMN_UPDATE_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Guardo nueva fecha en caso de que no exista
    public void saveNewDate(Dolar dolar) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_UPDATE_DATE},
                COLUMN_UPDATE_DATE + " = ?", new String[]{dolar.getUpdateDate()},
                null, null, null);

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, dolar.getName());
            values.put(COLUMN_BUY, dolar.getBuy());
            values.put(COLUMN_SELL, dolar.getSell());
            values.put(COLUMN_UPDATE_DATE, dolar.getUpdateDate());
            db.insert(TABLE_NAME, null, values);
        }
        cursor.close();
        db.close();
    }

    //Retorno lista de dolares guardados
    public List<Dolar> getAllDolars() {
        List<Dolar> dolars = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Dolar dolar = new Dolar(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                dolars.add(dolar);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dolars;
    }


}
