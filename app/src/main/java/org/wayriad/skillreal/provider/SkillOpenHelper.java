package org.wayriad.skillreal.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robertlee on 7/25/16.
 */
public class SkillOpenHelper extends SQLiteOpenHelper
{
    public static final String NAME = "skillreal";
    public static final int VERSION = 1;




    public SkillOpenHelper(Context context)
    {
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE_DB = "CREATE TABLE " + SkillContract.TableSkills.TABLE_NAME + " (" +
                SkillContract.TableSkills._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                SkillContract.TableSkills.COLUMN_NAME_SKILL_NAME + " TEXT NOT NULL, \n" +
                SkillContract.TableSkills.COLUMN_NAME_SKILL_CATEGORY + " TEXT NOT NULL, \n" +
                SkillContract.TableSkills.COLUMN_NAME_SKILL_LEVEL + " INTEGER NOT NULL, \n" +
                SkillContract.TableSkills.COLUMN_NAME_SKILL_POTENTIAL + " INTEGER NOT NULL, \n" +
                SkillContract.TableSkills.COLUMN_NAME_DELETED + " INTEGER NOT NULL \n" +
                ");";
        db.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
