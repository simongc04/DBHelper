package org.iesharia.myapplication


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory? = null) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                AGE_COL + " TEXT" + ")")
        db.execSQL(query)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun addName(name: String, age: String) {
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(AGE_COL, age)


        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }




    fun EliminarID(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ? ", arrayOf(id.toString()))
        db.close()
    }
    fun updateRecord(id: Int, name: String, age: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(AGE_COL, age)


        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }






    companion object {
        val DATABASE_NAME = "nombres"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "name_table"
        val ID_COL = "id"
        val NAME_COl = "nombre"
        val AGE_COL = "edad"
    }
}

