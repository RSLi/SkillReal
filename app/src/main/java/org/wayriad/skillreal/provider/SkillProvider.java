package org.wayriad.skillreal.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.security.PrivilegedExceptionAction;

public class SkillProvider extends ContentProvider
{

    private SkillOpenHelper mHelper = null;


    /*
    URI Matcher
     */
    private static final int SKILL_LIST = 1;
    private static final int SKILL_ID = 2;
    private static final UriMatcher URI_MATCHER;

    static{
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(SkillContract.AUTHORITY,"skills",SKILL_LIST);
        URI_MATCHER.addURI(SkillContract.AUTHORITY,"skills/#",SKILL_ID);
    }

    /*
    Content Provider standard life cycle methods
     */
    public SkillProvider() {}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        //TODO: use deleted flag for the skill table rather than really deleting
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int deletedCount = 0;
        switch(URI_MATCHER.match(uri)){
            case SKILL_LIST:
                deletedCount = db.delete(SkillContract.TableSkills.TABLE_NAME,selection,selectionArgs);
                break;
            case SKILL_ID:
                String where = SkillContract.TableSkills._ID + " = " + uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    where += " AND " + selection;
                }
                deletedCount = db.delete(SkillContract.TableSkills.TABLE_NAME,where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI not supported");
        }

        if(deletedCount > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return deletedCount;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (URI_MATCHER.match(uri)) {
            case SKILL_LIST:
                return SkillContract.TableSkills.CONTENT_TYPE;
            case SKILL_ID:
                return SkillContract.TableSkills.CONTENT_TYPE_SINGLE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        //only uri targeting tables can do insertion
        if(URI_MATCHER.match(uri) != SKILL_LIST){
            throw new IllegalArgumentException("Unsupported URI for insertion:" + uri);
        }
        //deploying insertion depending on table
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(URI_MATCHER.match(uri) == SKILL_LIST){
            long id = db.insert(SkillContract.TableSkills.TABLE_NAME,null,values);
            return getUriForId(id,uri);
        }else{
            throw new IllegalArgumentException("Unsupported URI for insertion:" + uri);
        }
    }

    private Uri getUriForId(long id, Uri uri)
    {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }
    @Override
    public boolean onCreate() {
        mHelper = new SkillOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch(URI_MATCHER.match(uri)) {
            case SKILL_LIST:
                builder.setTables(SkillContract.TableSkills.TABLE_NAME);
                if(TextUtils.isEmpty(sortOrder)){
                    sortOrder = SkillContract.TableSkills.SORT_ORDER_DEFAULT;
                }
                break;
            case SKILL_ID:
                builder.setTables(SkillContract.TableSkills.TABLE_NAME);
                builder.appendWhere(SkillContract.TableSkills._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI for query:" + uri);
        }

        Cursor cursor = builder.query(db,projection,selection,selectionArgs,null,null,sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updateCount = 0;

        switch(URI_MATCHER.match(uri)){
            case SKILL_LIST:
                updateCount = db.update(SkillContract.TableSkills.TABLE_NAME,values,selection,selectionArgs);
                break;
            case SKILL_ID:
                String where = SkillContract.TableSkills._ID + " = " + uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    where += " AND " + selection;
                }
                updateCount = db.update(SkillContract.TableSkills.TABLE_NAME,values,where,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if(updateCount > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return updateCount;
    }

    public Cursor getSkillList()
    {
        return query(SkillContract.TableSkills.CONTENT_URI, SkillContract.TableSkills.PROJECTION_ALL,null,null,null);
    }

    public Cursor getSkill(int id)
    {
        return query(Uri.withAppendedPath(SkillContract.TableSkills.CONTENT_URI,id+""), SkillContract.TableSkills.PROJECTION_ALL,null,null,null);
    }

    public Uri addSkill(String skillName, String skillCatagory, String skillLevel, String skillPotential){
        ContentValues values = new ContentValues();
        values.put(SkillContract.TableSkills.COLUMN_NAME_SKILL_NAME,skillName);
        values.put(SkillContract.TableSkills.COLUMN_NAME_SKILL_CATEGORY,skillCatagory);
        values.put(SkillContract.TableSkills.COLUMN_NAME_SKILL_LEVEL,skillLevel);
        values.put(SkillContract.TableSkills.COLUMN_NAME_SKILL_POTENTIAL,skillPotential);

        values.put(SkillContract.TableSkills.COLUMN_NAME_DELETED,0);
        return insert(SkillContract.TableSkills.CONTENT_URI,values);
    }
}
