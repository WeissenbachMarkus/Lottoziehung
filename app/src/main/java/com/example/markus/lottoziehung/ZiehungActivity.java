package com.example.markus.lottoziehung;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ZiehungActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER_ID = 0;
    private TextView numbers;
    private Button getNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziehung);

        this.numbers = (TextView) findViewById(R.id.numbers);
        this.getNumbers = (Button) findViewById(R.id.getNumbers);

        this.getNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, ZiehungActivity.this);
            }
        });

        try {
            getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        }catch (Exception ex)
        {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Toast.makeText(ZiehungActivity.this,"here",Toast.LENGTH_LONG).show();
                    return getContentResolver().query(NumberContract.NumberEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(ZiehungActivity.class.getSimpleName(), "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       try
       {
           this.numbers.setText(data.toString());
       }catch (Exception ex)
       {
           Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
