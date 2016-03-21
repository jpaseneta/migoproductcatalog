package com.example.v15.migoproductcatalog.Database;

/**
 * Created by V15 on 18/03/2016.
 */
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.v15.migoproductcatalog.Model.Books;
import com.example.v15.migoproductcatalog.Model.Movies;
import com.example.v15.migoproductcatalog.Model.Products;
import com.example.v15.migoproductcatalog.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Define the version and database file name
    private static final String DB_NAME = "migo.db";
    private static final int DB_VERSION = 1;

    // Use a static class to defined the data structure
    // This will come in very handy if you using Agile
    // As your development model
    private static class Products {
        private static final String TABLE_NAME = "Products";
        private static final String COL_ID = "id";
        private static final String COL_TITLE = "title";
        private static final String COL_GENREID = "genreid";
        private static final String COL_FILENAME= "filename";
        private static final String COL_EXTERNAL = "external";
    }

    private static class MediaType {
        private static final String TABLE_NAME = "MediaType";
        private static final String COL_ID = "id";
        private static final String COL_MEDIA="media";
    }

    private static class Genre {
        private static final String TABLE_NAME = "Genre";
        private static final String COL_ID = "id";
        private static final String COL_GENRENAME = "genrename";
        private static final String COL_MEDIAID = "mediaid";
    }

    private static class Movies {
        private static final String TABLE_NAME = "Movies";
        private static final String COL_ID = "id";
        private static final String COL_TITLE = "title";
        private static final String COL_YEAR = "year";
        private static final String COL_GENREID = "genreid";
        private static final String COL_FILENAME = "filename";
        private static final String COL_EXTERNAL = "external";
    }

    private static class Books {
        private static final String TABLE_NAME = "Books";
        private static final String COL_ID = "id";
        private static final String COL_TITLE = "title";
        private static final String COL_AUTHOR = "author";
        private static final String COL_GENREID = "genreid";
        private static final String COL_FILENAME = "filename";
        private static final String COL_EXTERNAL = "external";
    }

    private static class Music {
        private static final String TABLE_NAME = "Music";
        private static final String COL_ID = "id";
        private static final String COL_TITLE = "title";
        private static final String COL_ARTIST = "artist";
        private static final String COL_GENREID = "genreid";
        private static final String COL_FILENAME = "filename";
        private static final String COL_EXTERNAL = "external";
    }

    private SQLiteDatabase db;
    private Context context;
    // Constructor to simplify Business logic access to the repository
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        // Android will look for the database defined by DB_NAME
        // And if not found will invoke your onCreate method
        this.db = this.getWritableDatabase();
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Android has created the database identified by DB_NAME
        // The new database is passed to you vai the db arg
        // Now it is up to you to create the Schema.
        // This schema creates a very simple user table, in order
        // Store user login credentials
        db.beginTransaction();


        //CREATE MEDIATYPE TABLE
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT)",
                MediaType.TABLE_NAME, MediaType.COL_ID,
                MediaType.COL_MEDIA));

        //CREATE GENRE TABLE
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
                Genre.TABLE_NAME, Genre.COL_ID,
                Genre.COL_GENRENAME,Genre.COL_MEDIAID,Genre.COL_MEDIAID,MediaType.TABLE_NAME, MediaType.COL_ID));

        //CREATE PRODUCTS TABLE
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT,%s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
                Products.TABLE_NAME, Products.COL_ID,
                Products.COL_TITLE, Products.COL_GENREID, Products.COL_FILENAME,Products.COL_EXTERNAL, Products.COL_GENREID, Genre.TABLE_NAME, Genre.COL_ID));

        db.setTransactionSuccessful();
        db.endTransaction();

        db.beginTransaction();


        //CREATE MOVIES TABLE
        db.execSQL(String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s TEXT, " +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))",
                Movies.TABLE_NAME,
                Movies.COL_ID,
                Movies.COL_TITLE,
                Movies.COL_YEAR,
                Movies.COL_GENREID,
                Movies.COL_FILENAME,
                Movies.COL_EXTERNAL,
                Movies.COL_GENREID, Genre.TABLE_NAME, Genre.COL_ID));

        //CREATE BOOKS TABLE
        db.execSQL(String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s TEXT, " +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s)) ",
                Books.TABLE_NAME,
                Books.COL_ID,
                Books.COL_TITLE,
                Books.COL_AUTHOR,
                Books.COL_GENREID,
                Books.COL_FILENAME,
                Books.COL_EXTERNAL,
                Books.COL_GENREID,Genre.TABLE_NAME,Genre.COL_ID));

        //CREATE MUSIC TABLE
        db.execSQL(String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s TEXT, " +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))",
                Music.TABLE_NAME,
                Music.COL_ID,
                Music.COL_TITLE,
                Music.COL_ARTIST,
                Music.COL_GENREID,
                Music.COL_FILENAME,
                Music.COL_EXTERNAL,
                Music.COL_GENREID,Genre.TABLE_NAME,Genre.COL_ID));
        db.setTransactionSuccessful();
        db.endTransaction();

        db.beginTransaction();
        //initialize media type values
        db.execSQL(String.format("INSERT INTO %s (%s) VALUES ('%s')",MediaType.TABLE_NAME,MediaType.COL_MEDIA,"Music"));
        db.execSQL(String.format("INSERT INTO %s (%s) VALUES ('%s')", MediaType.TABLE_NAME, MediaType.COL_MEDIA, "Books"));
        db.execSQL(String.format("INSERT INTO %s (%s) VALUES ('%s')", MediaType.TABLE_NAME, MediaType.COL_MEDIA, "Movies"));

        Cursor resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",MediaType.COL_ID,MediaType.TABLE_NAME,MediaType.COL_MEDIA,"Music"),null);
        resultSet.moveToFirst();
        int mediaID=resultSet.getInt(0);
        Log.d("MediaId", mediaID+"");
        //initialize genre values
        //Music
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)", Genre.TABLE_NAME, Genre.COL_GENRENAME, Genre.COL_MEDIAID, "Rock", mediaID));
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)", Genre.TABLE_NAME, Genre.COL_GENRENAME, Genre.COL_MEDIAID, "Pop", mediaID));
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)",Genre.TABLE_NAME,Genre.COL_GENRENAME,Genre.COL_MEDIAID,"EDM",mediaID));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",MediaType.COL_ID,MediaType.TABLE_NAME,MediaType.COL_MEDIA,"Books"),null);
        resultSet.moveToFirst();
        mediaID=resultSet.getInt(0);
        //Books
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)", Genre.TABLE_NAME, Genre.COL_GENRENAME, Genre.COL_MEDIAID, "Fiction", mediaID));
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)", Genre.TABLE_NAME, Genre.COL_GENRENAME, Genre.COL_MEDIAID, "Non Fiction", mediaID));


        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1", MediaType.COL_ID, MediaType.TABLE_NAME, MediaType.COL_MEDIA, "Movies"), null);
        resultSet.moveToFirst();
        mediaID=resultSet.getInt(0);
        //Movies
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)",Genre.TABLE_NAME,Genre.COL_GENRENAME,Genre.COL_MEDIAID,"Comedy",mediaID));
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)", Genre.TABLE_NAME, Genre.COL_GENRENAME, Genre.COL_MEDIAID, "Horror", mediaID));
        db.execSQL(String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)",Genre.TABLE_NAME,Genre.COL_GENRENAME,Genre.COL_MEDIAID,"Drama",mediaID));

        //Initialize Products //Books

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Non Fiction"),null);
        resultSet.moveToFirst();
        int genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                         "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE,Books.COL_AUTHOR,Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "Diary Of A Young Girl","Anne Frank",genreID,"diaryofayounggirl",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Diary Of A Young Girl",genreID,"diaryofayounggirl",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "The Art Of War", "Sun Tzu", genreID, "theartofwar",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "The Art Of War", genreID, "theartofwar",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "Tuesdays With Morrie", "Mitch Albom", genreID, "tuesdayswithmorrie",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Tuesdays With Morrie", genreID, "tuesdayswithmorrie",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Fiction"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                         "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE,Books.COL_AUTHOR,Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "Fifty Shades Of Grey","E.L. James",genreID,"fiftyshadesofgrey",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Fifty Shades Of Grey",genreID,"fiftyshadesofgrey",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "Animal Farm", "George Orwell", genreID, "animalfarm",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Animal Farm", genreID, "animalfarm",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                         "VALUES ('%s','%s',%d,'%s',%d)",
                Books.TABLE_NAME,
                Books.COL_TITLE,Books.COL_AUTHOR,Books.COL_GENREID,Books.COL_FILENAME,Books.COL_EXTERNAL,
                "How To Kill A Mockingbird","Harper Lee",genreID,"howtokillamockingbird",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "How To Kill A Mockingbird",genreID,"howtokillamockingbird",0));

        //Initialize Products Music

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Rock"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                         "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Damned If I Do Ya","All Time Low",genreID,"damnedifidoya",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Damned If I Do Ya",genreID,"damnedifidoya",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE, Music.COL_ARTIST, Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "For Baltimore", "All Time Low", genreID, "forbaltimore",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "For Baltimore", genreID, "forbaltimore",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Somewhere In Neverland","All Time Low",genreID,"somewhereinneverland",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Somewhere In Neverland",genreID,"somewhereinneverland",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Face Down","The Red Jumpsuit Apparatus",genreID,"facedown",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Face Down",genreID,"facedown",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Your Guardian Angel","The Red Jumpsuit Apparatus",genreID,"yourguardianangel",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Your Guardian Angel",genreID,"yourguardianangel",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Pen And Paper","The Red Jumpsuit Apparatus",genreID,"penandpaper",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Pen And Paper",genreID,"penandpaper",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "I Write Sins Not Tragedies","Panic! At The Disco",genreID,"iwritesinsnottragedies",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "I Write Sins Not Tragedies",genreID,"iwritesinsnottragedies",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "New Perspective","Panic! At The Disco",genreID,"newperspective",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "New Perspective",genreID,"newperspective",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE, Music.COL_ARTIST, Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "This Is Gospel", "Panic! At The Disco", genreID, "thisisgospel",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "This Is Gospel", genreID, "thisisgospel",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Pop"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Back To December","Taylor Swift",genreID,"backtodecember",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Back To December",genreID,"backtodecember",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE, Music.COL_ARTIST, Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Enchanted", "Taylor Swift", genreID, "enchanted",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Enchanted", genreID, "enchanted",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Love Story","Taylor Swift",genreID,"lovestory",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Love Story",genreID,"lovestory",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Hello","Adele",genreID,"hello",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Hello",genreID,"hello",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Rolling In The Deep","Adele",genreID,"rollinginthedeep",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Rolling In The Deep",genreID,"rollinginthedeep",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Someone Like You","Adele",genreID,"someonelikeyou",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Someone Like You",genreID,"someonelikeyou",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Sorry","Justin Bieber",genreID,"sorry",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Sorry",genreID,"sorry",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "What Do You Mean","Justin Bieber",genreID,"whatdoyoumean",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "What Do You Mean",genreID,"whatdoyoumean",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE, Music.COL_ARTIST, Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Love Yourself", "Justin Bieber", genreID, "loveyourself",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Love Yourself", genreID, "loveyourself",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"EDM"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Levels","Avicii",genreID,"levels",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Levels",genreID,"levels",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE, Music.COL_ARTIST, Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Broken Arrows", "Avicii", genreID, "brokenarrows",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Broken Arrows", genreID, "brokenarrows",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "The Nights","Avicii",genreID,"thenights",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "The Nights",genreID,"thenights",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Find You","Zedd ft. Matthew Koma & Miriam Bryant",genreID,"findyou",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Find You",genreID,"findyou",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Clarity","Zedd ft. Foxes",genreID,"clarity",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Clarity",genreID,"clarity",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Stay The Night","Zedd ft. Hayley Williams",genreID,"staythenight",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Stay The Night",genreID,"staythenight",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Roses","The Chainsmokers ft. Rozes",genreID,"roses",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Roses",genreID,"roses",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Waterbed","The Chainsmokers ft. Waterbed",genreID,"waterbed",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Waterbed",genreID,"waterbed",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s','%s',%d,'%s',%d)",
                Music.TABLE_NAME,
                Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                "Kanye","The Chainsmokers ft. Kanye",genreID,"kanye",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Kanye",genreID,"kanye",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Comedy"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "22 Jump Street",2014,genreID,"twentytwojumpstreet",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "22 Jump Street",genreID,"twentytwojumpstreet",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE, Movies.COL_YEAR, Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "Borat", 2006, genreID, "borat.jpg",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Borat", genreID, "borat.jpg",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "Hangover Part II",2011,genreID,"hangover2",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Hangover Part II",genreID,"hangover2",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Horror"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "The Amityville Horror",2005,genreID,"amityville",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "The Amityville Horror",genreID,"amityville",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE, Movies.COL_YEAR, Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "The Conjuring", 2013, genreID, "conjuring",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "The Conjuring", genreID, "conjuring",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "Insidious",2010,genreID,"insidious",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Insidious",genreID,"insidious",0));

        resultSet = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' LIMIT 1",Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,"Drama"),null);
        resultSet.moveToFirst();
        genreID=resultSet.getInt(0);

        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "Before We Go",2014,genreID,"beforewego",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Before We Go",genreID,"beforewego",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "Click",2006,genreID,"click",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "Click",genreID,"click",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,%d,'%s',%d)",
                Movies.TABLE_NAME,
                Movies.COL_TITLE,Movies.COL_YEAR,Movies.COL_GENREID,Movies.COL_FILENAME,Movies.COL_EXTERNAL,
                "The Pursuit Of Happyness",2006,genreID,"pursuitofhappiness",0));
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                "The Pursuit Of Happyness",genreID,"pursuitofhappiness",0));
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.d("DATABASEHELPER", "DATABASE INITIALIZATION SUCCESS!");
    }

//    public String[] getUserCredentials() {
//
//        String[] creds;
//        Cursor cursor;
//
//        creds = new String[3];
//
//        cursor = this.db.query(UserTable.NAME, new String[] {
//                        UserTable.COL_USERNAME, UserTable.COL_PASSWORD},
//                null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//
//            creds[0] = cursor.getString(0);
//            creds[1] = cursor.getString(1);
//            creds[2] = cursor.getString(2);
//            cursor.close();
//
//        } else {
//
//            throw new Exception("No User Credentials Found");
//
//        }
//
//        return creds;
//
//    }
public ArrayList<com.example.v15.migoproductcatalog.Model.Products> getAllBooks() {
    ArrayList<com.example.v15.migoproductcatalog.Model.Products> bookList = new ArrayList<>();

    // Select All Query
    String selectQuery = "SELECT Books.title,Books.author,Books.genreid," +
                        "Genre.genrename,Books.filename,Books.external FROM Books INNER JOIN Genre on genre.id=books.genreid";
//            String.format("SELECT %s,%s,%s,%s,%s FROM %s",
//                    Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID, Books.COL_FILENAME, Books.TABLE_NAME);
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            com.example.v15.migoproductcatalog.Model.Books book = new com.example.v15.migoproductcatalog.Model.Books();
            book.setTitle(cursor.getString(0));
            book.setAuthor(cursor.getString(1));
            int genreId=cursor.getInt(2);
            book.setGenreID(genreId);
            book.setGenreName(cursor.getString(3));
            int externalFlag = cursor.getInt(5);
            book.setExternalFlag(externalFlag);
            String filename=cursor.getString(4);
            if(externalFlag==1){
                book.setFileName(filename);
            }else {
                book.setResId(getResId(filename, R.drawable.class));
            }
            // Adding contact to list
            bookList.add(book);
        } while (cursor.moveToNext());
        cursor.close();
    }

    // return list
    return bookList;
}

    public ArrayList<com.example.v15.migoproductcatalog.Model.Products> getAllMusic() {
        ArrayList<com.example.v15.migoproductcatalog.Model.Products> musicList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT Music.title,Music.Artist,Music.genreid," +
                "Genre.genrename,Music.filename,Music.external FROM Music INNER JOIN Genre on genre.id=music.genreid";
//            String.format("SELECT %s,%s,%s,%s,%s FROM %s",
//                    Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID, Books.COL_FILENAME, Books.TABLE_NAME);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                com.example.v15.migoproductcatalog.Model.Music music = new com.example.v15.migoproductcatalog.Model.Music();
                music.setTitle(cursor.getString(0));
                music.setArtist(cursor.getString(1));
                int genreId=cursor.getInt(2);
                music.setGenreID(genreId);
                music.setGenreName(cursor.getString(3));
                int externalFlag = cursor.getInt(5);
                music.setExternalFlag(externalFlag);
                String filename=cursor.getString(4);
                if(externalFlag==1){
                    music.setFileName(filename);
                }else {
                    music.setResId(getResId(filename, R.drawable.class));
                }
                // Adding contact to list
                musicList.add(music);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return list
        return musicList;
    }

    public ArrayList<com.example.v15.migoproductcatalog.Model.Products> getAllMovies() {
        ArrayList<com.example.v15.migoproductcatalog.Model.Products> movieList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT Movies.title,Movies.Year,Movies.genreid," +
                "Genre.genrename,Movies.filename,Movies.external FROM Movies INNER JOIN Genre on genre.id=movies.genreid";
//            String.format("SELECT %s,%s,%s,%s,%s FROM %s",
//                    Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID, Books.COL_FILENAME, Books.TABLE_NAME);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                com.example.v15.migoproductcatalog.Model.Movies movie = new com.example.v15.migoproductcatalog.Model.Movies();
                movie.setTitle(cursor.getString(0));
                movie.setYear(cursor.getInt(1));
                int genreId=cursor.getInt(2);
                movie.setGenreID(genreId);
                movie.setGenreName(cursor.getString(3));
                int externalFlag = cursor.getInt(5);
                movie.setExternalFlag(externalFlag);
                String filename=cursor.getString(4);
                if(externalFlag==1){
                    movie.setFileName(filename);
                }else {
                    movie.setResId(getResId(filename, R.drawable.class));
                }
                // Adding contact to list
                movieList.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return list
        return movieList;
    }

    public ArrayList<com.example.v15.migoproductcatalog.Model.Products> getAllProducts() {
        ArrayList<com.example.v15.migoproductcatalog.Model.Products> productList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT Products.title,Products.genreid," +
                "Genre.genrename,Products.filename,Products.external FROM Products INNER JOIN Genre on genre.id=Products.genreid";
//            String.format("SELECT %s,%s,%s,%s,%s FROM %s",
//                    Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID, Books.COL_FILENAME, Books.TABLE_NAME);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                com.example.v15.migoproductcatalog.Model.Products product = new com.example.v15.migoproductcatalog.Model.Products();
                product.setTitle(cursor.getString(0));
                int genreId=cursor.getInt(1);
                product.setGenreID(genreId);
                product.setGenreName(cursor.getString(2));
                int externalFlag = cursor.getInt(4);
                String filename=cursor.getString(3);
                if(externalFlag==1){
                    product.setFileName(filename);
                }else {
                    product.setResId(getResId(filename, R.drawable.class));
                }
                product.setExternalFlag(externalFlag);

                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return list
        return productList;
    }

    public com.example.v15.migoproductcatalog.Model.Products addNewProduct(com.example.v15.migoproductcatalog.Model.Products newProduct, int mediaType){
        db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s) " +
                        "VALUES ('%s',%d,'%s',%d)",
                Products.TABLE_NAME,
                Products.COL_TITLE,Products.COL_GENREID,Products.COL_FILENAME,Products.COL_EXTERNAL,
                newProduct.getTitle(),newProduct.getGenreID(),newProduct.getFileName(),newProduct.getExternalFlag()));
                Log.d("Insert External Flag", newProduct.getExternalFlag()+"");
        switch (mediaType){
            case 1: //Music
                db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                "VALUES ('%s','%s',%d,'%s',%d)",
                        Music.TABLE_NAME,
                        Music.COL_TITLE,Music.COL_ARTIST,Music.COL_GENREID,Music.COL_FILENAME,Music.COL_EXTERNAL,
                        newProduct.getTitle(),newProduct.getOtherInfo(),newProduct.getGenreID(),newProduct.getFileName(),newProduct.getExternalFlag()));
                com.example.v15.migoproductcatalog.Model.Music music =  new com.example.v15.migoproductcatalog.Model.Music();
                music.setTitle(newProduct.getTitle());
                music.setArtist(music.getOtherInfo());
                music.setGenreID(newProduct.getGenreID());
                music.setGenreName(newProduct.getGenreName());
                music.setFileName(newProduct.getFileName());
                music.setExternalFlag(newProduct.getExternalFlag());
                return music;
            case 2://Books
                db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                "VALUES ('%s','%s',%d,'%s',%d)",
                        Books.TABLE_NAME,
                        Books.COL_TITLE, Books.COL_AUTHOR, Books.COL_GENREID, Books.COL_FILENAME, Books.COL_EXTERNAL,
                        newProduct.getTitle(), newProduct.getOtherInfo(), newProduct.getGenreID(), newProduct.getFileName(), newProduct.getExternalFlag()));
                com.example.v15.migoproductcatalog.Model.Books book =  new com.example.v15.migoproductcatalog.Model.Books();
                book.setTitle(newProduct.getTitle());
                book.setAuthor(newProduct.getOtherInfo());
                book.setGenreID(newProduct.getGenreID());
                book.setGenreName(newProduct.getGenreName());
                book.setFileName(newProduct.getFileName());
                book.setExternalFlag(newProduct.getExternalFlag());
                return book;

            case 3://Movies
                db.execSQL(String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                                "VALUES ('%s',%d,%d,'%s',%d)",
                        Movies.TABLE_NAME,
                        Movies.COL_TITLE, Movies.COL_YEAR, Movies.COL_GENREID, Movies.COL_FILENAME, Movies.COL_EXTERNAL,
                        newProduct.getTitle(), Integer.parseInt(newProduct.getOtherInfo()), newProduct.getGenreID(), newProduct.getFileName(), newProduct.getExternalFlag()));
                com.example.v15.migoproductcatalog.Model.Movies movie =  new com.example.v15.migoproductcatalog.Model.Movies();
                movie.setTitle(newProduct.getTitle());
                movie.setYear(Integer.parseInt(newProduct.getOtherInfo()));
                movie.setGenreID(newProduct.getGenreID());
                movie.setGenreName(newProduct.getGenreName());
                movie.setFileName(newProduct.getFileName());
                movie.setExternalFlag(newProduct.getExternalFlag());

                return movie;
        }
        return null;
    }

    public List<String> getMediaTypes(){
        List<String> mediatypes = new ArrayList<>();

        Cursor cursor =  db.rawQuery(String.format("SELECT %s FROM %s",
                MediaType.COL_MEDIA,MediaType.TABLE_NAME),null);
        if (cursor.moveToFirst()) {
            do{
                mediatypes.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return mediatypes;

    }

    public int getMediaTypeId(String mediaName){
        Cursor cursor =  db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s'",
                MediaType.COL_ID,MediaType.TABLE_NAME,MediaType.COL_MEDIA,mediaName),null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getGenreIDFromGenreName(String genreName){
        Cursor cursor =  db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s'",
                Genre.COL_ID,Genre.TABLE_NAME,Genre.COL_GENRENAME,genreName),null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<String> getGenreNames(int mediaType){
        List<String> genreNames = new ArrayList<>();

        Cursor cursor =  db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=%d",
                Genre.COL_GENRENAME,Genre.TABLE_NAME,Genre.COL_MEDIAID,mediaType),null);
        if (cursor.moveToFirst()) {
            do{
                genreNames.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return genreNames;

    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // Later when you change the DB_VERSION
        // This code will be invoked to bring your database
        // Upto the correct specification
    }
}