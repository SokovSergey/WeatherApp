package com.example.weather

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "signup.db", null, 1)
{
    //val databaseName : String = "Signup.db"

    private val KEY_ID = "_id"
    private val KEY_FIO = "FIO"
    private val KEY_GENDER = "gender"
    private val KEY_AGE = "age"
    private val KEY_SLEVEL = "symptomsLevel"
    private val KEY_OVERALLSLEVEL = "overallSensitivityLevel"

    private val perTableColumnKeys : List<String> = listOf(KEY_ID, KEY_FIO, KEY_GENDER, KEY_AGE, KEY_SLEVEL, KEY_OVERALLSLEVEL)

    override fun onCreate(MyDatabase: SQLiteDatabase) {
        MyDatabase.execSQL("create Table allusers(_id INTEGER primary key, email TEXT, username TEXT, password TEXT)")

        MyDatabase.execSQL("create Table personalParameters( " +
                KEY_ID + " INTEGER primary key, " +
                KEY_FIO + " TEXT, " +
                KEY_GENDER +" TEXT, " +
                KEY_AGE + " INTEGER, " +
                KEY_SLEVEL + " INTEGER, " +
                KEY_OVERALLSLEVEL + " INTEGER)")
    }

    override fun onUpgrade(MyDatabase: SQLiteDatabase, p1: Int, p2: Int) {
        MyDatabase.execSQL("drop Table if exists allUsers")

        MyDatabase.execSQL("drop Table if exists personalParameters")
    }

    fun lastId() : Int
    {
        val MyDataBase : SQLiteDatabase = this.readableDatabase

        val cursor : Cursor = MyDataBase.rawQuery("Select _id from allusers", null)

        if(cursor.count == 0)
            return -1

        val idColIndex = cursor.getColumnIndex(KEY_ID)

        if(idColIndex == -1)
            return -1


        var maxId : Int = -1

        if(cursor.moveToFirst())
        {
            do {
                val id = cursor.getInt(idColIndex)
                if( id > maxId)
                    maxId = id
            }
            while (cursor.moveToNext())
        }
        return  maxId
    }

    fun getIdByUser(username: String) : Int
    {
        val MyDataBase : SQLiteDatabase = this.readableDatabase

        val cursor : Cursor = MyDataBase.rawQuery("Select _id from allusers where username = ?", Array<String>(1){username})

        if(cursor.count == 0)
            return -1

        val idColIndex = cursor.getColumnIndex(KEY_ID)

        if(idColIndex == -1)
            return -1

        var resId : Int = -1

        if(cursor.moveToFirst())
        {
            do {
                resId = cursor.getInt(idColIndex)
            }
            while (cursor.moveToNext())
        }

        return resId

    }

    fun insertData(email : String, username : String, password: String ) : Boolean
    {
        val MyDataBase : SQLiteDatabase = this.writableDatabase
        var contentValues : ContentValues = ContentValues()
        contentValues.put("email", email)

        var id : Int = 0
        if(lastId() != -1)
            id = lastId() + 1

        contentValues.put("_id", id.toString())
        contentValues.put("username", username)
        contentValues.put("password", password)


        try
        {
            if(MyDataBase.insertOrThrow("allusers", null, contentValues) == -1L)
                return false
        }
        catch(e : SQLiteException)
        {
            println(e)
        }

        //var result : Long = MyDataBase.insert("allUsers", null, contentValues)

        var personalValues : ContentValues = ContentValues()
        personalValues.put(KEY_ID, id.toString())
        personalValues.put(KEY_FIO, "")
        personalValues.put(KEY_GENDER, "")
        personalValues.put(KEY_AGE, "0")
        personalValues.put(KEY_SLEVEL, "0")
        personalValues.put(KEY_OVERALLSLEVEL, "0")

        try
        {
            if(MyDataBase.insertOrThrow("personalParameters", null, personalValues) == -1L)
                return false
        }
        catch(e : SQLiteException)
        {
            println(e)
        }
        //result += MyDataBase.insert("personalParameters",null, personalValues)

        return true
    }

    fun insertPersonalData(username : String, fio : String, gender : String, age : Int, slevel : Int, overallSLevel : Int ) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.writableDatabase;
        val cursor : Cursor =  MyDataBase.rawQuery("Select _id from allusers where username = ?", Array<String>(1){username})

        if(cursor.count == 0)
            return false

        val idColIndex = cursor.getColumnIndex(KEY_ID)
        if(idColIndex == 0)
            return false

        val lst = arrayOf<String>(fio, gender, age.toString(), slevel.toString(), overallSLevel.toString(), idColIndex.toString())
        val res  = MyDataBase.rawQuery("UPDATE personalParameters SET FIO = ?, gender = ?, age = ?, symptomsLevel = ?,overallSensitivityLevel = ? WHERE _id = ?",lst)



        return  res.count > 0
    }

    fun checkEmail( email: String) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.readableDatabase;
        val cursor : Cursor =  MyDataBase.rawQuery("Select * from allusers where email = ?", Array<String>(1){email})



        return cursor.count > 0
    }

    fun checkUserName( username : String) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.readableDatabase;
        val sql = "Select * from allusers where username = ?"
        val cursor : Cursor
        try {
            cursor =  MyDataBase.rawQuery(sql, Array<String>(1){username})
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            return false
        }

        println( cursor.getColumnIndex("username").toString())

        return cursor.count > 0
    }

    fun savePersonalParameters(currentUserName : String, s_name : String, first_name : String, family_name : String, age: Int, gender : String, num_stars : Int, summaryVal : Int) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.writableDatabase;

        //var personalValues : ContentValues = ContentValues()
        val id =  getIdByUser(currentUserName)
        //personalValues.put(KEY_ID, id.toString())

        val FIO = s_name + " " + first_name + " " + family_name
        /*personalValues.put(KEY_FIO, FIO)
        personalValues.put(KEY_GENDER, gender)
        personalValues.put(KEY_AGE, age)
        personalValues.put(KEY_SLEVEL, num_stars)
        personalValues.put(KEY_OVERALLSLEVEL, summaryVal)*/

        try
        {
            val lst = arrayOf<String>(FIO, gender, age.toString(), num_stars.toString(), summaryVal.toString(), id.toString())
            val cursor : Cursor = MyDataBase.rawQuery("UPDATE personalParameters SET FIO = ?, gender = ?, age = ?, symptomsLevel = ?,overallSensitivityLevel = ? WHERE _id = ?",lst)
            if(cursor.count == -1)
            {
                return false
            }
            //if(MyDataBase.insertOrThrow("personalParameters", null, personalValues) == -1L)
        }
        catch(e : SQLiteException)
        {
            println(e)
            return false
        }
        return true
    }



    @SuppressLint("Range")
    fun userHasPersonalParameters(username : String) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.readableDatabase;

        val userIndexSql : String = "SELECT _id FROM allusers WHERE username = ?"
        val cursor : Cursor
        try {
            cursor =  MyDataBase.rawQuery(userIndexSql, Array<String>(1){username})
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            MyDataBase.execSQL(userIndexSql)
            return false
        }

        //Удалось ли найти индекс пользователя
        val idColIndex = cursor.getColumnIndex("_id")
        var resultUserId : Int = -1

        if(cursor.moveToFirst())
        {
            resultUserId = cursor.getInt(idColIndex)
        }
        else
            return false
        if(resultUserId == -1)
            return false

        val personalDataSql : String = "Select * from personalParameters where _id = ?"

        var personalParams : Cursor
        try {
            personalParams =  MyDataBase.rawQuery(personalDataSql, Array<String>(1){resultUserId.toString()})
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            MyDataBase.execSQL(userIndexSql)
            return false
        }


        if(personalParams.moveToFirst())
        {
            do{
                for(key : String in perTableColumnKeys)
                {
                    val data = personalParams.getString(personalParams.getColumnIndex(key))
                    println(data)
                    if (data.isEmpty())
                        return false

                }
            }
            while (cursor.moveToNext())
        }
        return true
    }

    @SuppressLint("Range")
    fun getUserPersonalParameters(username: String) : MutableMap<String, String>
    {
        var result : MutableMap<String,String> = mutableMapOf()


        val MyDataBase: SQLiteDatabase = this.readableDatabase;

        val userIndexSql : String = "SELECT _id FROM allusers WHERE username = ?"
        val cursor : Cursor
        try {
            cursor =  MyDataBase.rawQuery(userIndexSql, Array<String>(1){username})
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            MyDataBase.execSQL(userIndexSql)
            return result
        }

        //Удалось ли найти индекс пользователя
        val idColIndex = cursor.getColumnIndex("_id")
        var resultUserId : Int = -1

        if(cursor.moveToFirst())
        {
            resultUserId = cursor.getInt(idColIndex)
        }

        val personalDataSql : String = "Select * from personalParameters where _id = ?"

        var personalParams : Cursor
        try {
            personalParams =  MyDataBase.rawQuery(personalDataSql, Array<String>(1){resultUserId.toString()})
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            MyDataBase.execSQL(userIndexSql)
            return result
        }


        if(personalParams.moveToFirst())
        {
            do{
                for(key : String in perTableColumnKeys)
                {
                    val data = personalParams.getString(personalParams.getColumnIndex(key))
                    if (data.isNotEmpty())
                    {
                        result.put(key, data)
                    }


                }
            }
            while (cursor.moveToNext())
        }


        return result

    }



    fun checkUserPassword( username: String, password: String) : Boolean
    {
        val MyDataBase: SQLiteDatabase = this.writableDatabase;
        var lst = listOf<String>(username, password)

        val cursor : Cursor =  MyDataBase.rawQuery("Select * from allusers where username = ? and password = ?",lst.toTypedArray())

        return cursor.count > 0
    }

    fun checkEmailPassword( email: String, password: String) : Boolean{
        val MyDataBase: SQLiteDatabase = this.writableDatabase;
        var lst = listOf<String>(email, password)

        val cursor : Cursor =  MyDataBase.rawQuery("Select * from allusers where email = ? and password = ?",lst.toTypedArray())


        return cursor.count > 0
    }
}