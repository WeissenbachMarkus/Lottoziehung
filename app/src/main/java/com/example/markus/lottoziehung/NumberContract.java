/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.markus.lottoziehung;

import android.net.Uri;
import android.provider.BaseColumns;


public class NumberContract {

    public static final String AUTHORITY = "com.example.markus.zahlengenerator";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NUMBERS = "numbers";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class NumberEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_NUMBERS)
                .build();

        // Task table and column names
        public static final String TABLE_NAME = "numbers";
        public static final String COLUMN_VALUE = "value";

    }
}
