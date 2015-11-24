/*
 * Copyright (C) 2011 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.koboc.collect.android.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.koboc.collect.android.provider.FormsProvider;
import org.koboc.collect.android.provider.InstanceProvider;

public class DatabaseUtility {
    private static FormsProvider.DatabaseHelper databaseHelperForm = new FormsProvider.DatabaseHelper("forms.db");
    private static InstanceProvider.DatabaseHelper databaseHelperInstance  = new InstanceProvider.DatabaseHelper("instances.db");
    private static Cursor formsCursor,instanceCursor;
    private static SQLiteDatabase database;


    public static int getTotalFormsCount(){
        database = databaseHelperForm.getWritableDatabase();
        formsCursor = database.rawQuery("SELECT * FROM forms" , null);

        return formsCursor.getCount();
    }

    public static int getTotalInstanceCount(String id){
        database = databaseHelperInstance.getWritableDatabase();
        instanceCursor = database.rawQuery("Select * from instances where caseId = "+id,null);

        return instanceCursor.getCount();
    }

    public static int getPre_formCount(){
        database = databaseHelperForm.getWritableDatabase();
        formsCursor = database.rawQuery("SELECT * FROM forms" , null);
        int i = 0;
        while(formsCursor.moveToNext()){
            if(formsCursor.getString(1).contains("Pre_") || formsCursor.getString(1).startsWith("pre_")){
                i++;
            }
        }
        return i;
    }

    public static int getPost_formCount(){
        database = databaseHelperForm.getWritableDatabase();
        formsCursor = database.rawQuery("SELECT * FROM forms" , null);
        int i = 0;
        while(formsCursor.moveToNext()){
            if(formsCursor.getString(1).contains("Post_") || formsCursor.getString(1).startsWith("post_")){
                i++;
            }
        }
        return i;
    }

    public static int getPost_InstanceCount(String id){
        database = databaseHelperInstance.getWritableDatabase();
        instanceCursor = database.rawQuery("SELECT * FROM instances where caseId = "+id , null);
        int i = 0;
        while(instanceCursor.moveToNext()){
            if(instanceCursor.getString(1).contains("Post_") || instanceCursor.getString(1).startsWith("post_")){
                i++;
            }
        }
        return i;
    }

    public static int getPre_InstanceCount(String id){
        database = databaseHelperInstance.getWritableDatabase();
        instanceCursor = database.rawQuery("SELECT * FROM instances where caseId = "+ id, null);
        int i = 0;
        while(instanceCursor.moveToNext()){
            if(instanceCursor.getString(1).contains("Pre_") || instanceCursor.getString(1).startsWith("pre_")){
                i++;
            }
        }
        return i;
    }

    public static Cursor getPre_Instances(String id){
        database = databaseHelperInstance.getWritableDatabase();

        instanceCursor = database.rawQuery("SELECT * FROM instances where displayName like ('%Pre_%') and caseId = "+ id, null);

        return instanceCursor;
    }

    public static Cursor getPost_Instances(String id){
        database = databaseHelperInstance.getWritableDatabase();

        //instanceCursor = database.rawQuery("SELECT * FROM instances where displayName like ('%Post_%') and caseId = "+ id +" and status in (\"complete\",\"submissionFailed\") ", null);

        instanceCursor = database.rawQuery("SELECT * FROM instances where displayName like ('%Post_%') and caseId = "+ id, null);

        return instanceCursor;
    }

    public static Cursor getPost_InstancesForUpload(String id){
        database = databaseHelperInstance.getWritableDatabase();

        instanceCursor = database.rawQuery("SELECT * FROM instances where displayName like ('%Post_%') and caseId = "+ id +" and status in (\"complete\",\"submissionFailed\") ", null);

        //instanceCursor = database.rawQuery("SELECT * FROM instances where displayName like ('%Post_%') and caseId = "+ id, null);

        return instanceCursor;
    }
}
