package com.example.markus.lottoziehung;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static com.example.markus.lottoziehung.NumberContract.NumberEntry.*;

public class ZiehungActivity2 extends AppCompatActivity {
    private static final String LIFECYCLE_CALLBACK_TEXT_KEY = "callback";
    private TextView numbers;
    private Button getNumbers;
    private Cursor lottozahlen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziehung2);

        this.numbers = (TextView) findViewById(R.id.numbers);
        this.getNumbers = (Button) findViewById(R.id.getNumbers);

        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACK_TEXT_KEY))
            {
                this.numbers.setText(savedInstanceState.getString(LIFECYCLE_CALLBACK_TEXT_KEY));
            }
        }

        this.getNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FetchNumbers().execute();

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(LIFECYCLE_CALLBACK_TEXT_KEY, this.numbers.getText().toString());
        super.onSaveInstanceState(outState);


    }

    private void zahlenZiehen(int anzahl) {

        try {

            String result = "";
            ArrayList<Integer> list = new ArrayList<>();


            if (lottozahlen.getCount() >= anzahl) {

                if (lottozahlen.moveToFirst()) {

                    for (int i = 0; i < anzahl; i++) {

                        int id = lottozahlen.getInt(lottozahlen.getColumnIndex(_ID));
                        list.add(id);

                        if (i != 0)
                            result += "," + lottozahlen.getString(lottozahlen.getColumnIndex(COLUMN_VALUE));
                        else
                            result = lottozahlen.getString(lottozahlen.getColumnIndex(COLUMN_VALUE));

                        lottozahlen.moveToNext();
                    }

                    numbers.setText(result);

                    for (Integer id : list) {

                        getContentResolver().delete(
                                CONTENT_URI.buildUpon().appendPath(id + "").build(),
                                null, null);
                    }

                }

            } else
                Toast.makeText(ZiehungActivity2.this, "Nicht genug Zahlen vorhanden! " + lottozahlen.getCount(), Toast.LENGTH_LONG).show();


        } catch (Exception ex) {
            Toast.makeText(ZiehungActivity2.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lottozahlen.close();
    }

    public class FetchNumbers extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            // Make the query to get the data

            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            return resolver.query(CONTENT_URI,
                    null, null, null, null);

        }

        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            lottozahlen = cursor;
            zahlenZiehen(3);

        }
    }
}
