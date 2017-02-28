package com.example.markus.lottoziehung;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static com.example.markus.lottoziehung.NumberContract.NumberEntry.*;

public class ZiehungActivity2 extends AppCompatActivity {

    private TextView numbers;
    private Button getNumbers;
    private Cursor mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziehung2);

        this.numbers = (TextView) findViewById(R.id.numbers);
        this.getNumbers = (Button) findViewById(R.id.getNumbers);


        this.getNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    executeASYNC();

                    String result = "";
                    ArrayList<Integer> list = new ArrayList<>();

                    if (mData.getCount() > 2)

                        if (mData.moveToFirst()) {

                            for (int i = 0; i < mData.getCount(); i++) {

                                int id = mData.getColumnIndex(COLUMN_VALUE);
                                list.add(id);

                                if (i != 0)
                                    result = result + "," + mData.getString(id);
                                else
                                    result += mData.getString(id);

                                mData.moveToNext();
                            }


                            numbers.setText(result);
                            Toast.makeText(ZiehungActivity2.this, list.size()+"", Toast.LENGTH_LONG).show();
                            for (Integer id : list) {

                                String[] ids={id.toString()};
                                int num=getContentResolver().delete(
                                        CONTENT_URI.buildUpon().appendPath(id+"").build(),
                                        null, null);
                                Toast.makeText(ZiehungActivity2.this, num+"", Toast.LENGTH_LONG).show();
                            }

                        } else
                            Toast.makeText(ZiehungActivity2.this, "Keine Zahlen mehr vorhanden!", Toast.LENGTH_LONG).show();


                } catch (Exception ex) {
                    Toast.makeText(ZiehungActivity2.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void executeASYNC() {
        new FetchNumbers().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mData.close();
    }

    public class FetchNumbers extends AsyncTask<Void, Void, Cursor> {

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

            mData = cursor;


        }
    }
}
