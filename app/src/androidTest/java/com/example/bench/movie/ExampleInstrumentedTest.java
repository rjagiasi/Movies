package com.example.bench.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.example.bench.movie.MovieDB.DATABASE_NAME;
import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;
import static com.example.bench.movie.MovieDBContract.COL_DATEINS;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        appContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = new MovieDB(appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        db.execSQL("Insert into " + TABLE_NAME + " VALUES (1, 'abc', 'abc.jpg', 'dakj' , 'oijdoi',3.6 , '2016-12-25', '2016-09-10');");

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT (julianday('now')-julianday('" + COL_DATEINS + "')) from "+TABLE_NAME, null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        int n = 0;
        do
        {
            String s = c.getString(n);
            System.out.print(s);
//            assertTrue(s, s.equalsIgnoreCase("768767"));
        }
        while (c.moveToNext());
    }
}
