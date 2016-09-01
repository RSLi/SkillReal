package org.wayriad.skillreal.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by robertlee on 7/16/16.
 */
public final class SkillContract
{
    /*
     Note on the Database Schema:
     Table:
     ---> skills
            - skill_name
            - skill_category
            - skill_level
            - skill_potential
            - deleted
            - _ID
     ---> categories
            - category_name
            - _ID
     */
    public static final String AUTHORITY =
            "org.wayriad.skillreal.provider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final class TableSkills implements BaseColumns
    {
        public static final String TABLE_NAME = "skills";
        public static final String COLUMN_NAME_SKILL_NAME = "skill_name";
        public static final String COLUMN_NAME_SKILL_CATEGORY = "skill_category";
        public static final String COLUMN_NAME_SKILL_LEVEL = "skill_level";
        public static final String COLUMN_NAME_SKILL_POTENTIAL = "skill_potential";
        public static final String COLUMN_NAME_DELETED = "deleted";

        public static final String[] PROJECTION_ALL =
                         {_ID,
                        COLUMN_NAME_SKILL_NAME,
                        COLUMN_NAME_SKILL_CATEGORY,
                        COLUMN_NAME_SKILL_LEVEL,
                        COLUMN_NAME_SKILL_POTENTIAL,
                        COLUMN_NAME_DELETED};

        public static final Uri CONTENT_URI = Uri.withAppendedPath(SkillContract.CONTENT_URI,"skills");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.org.wayriad.skillreal.provider.skills";
        public static final String CONTENT_TYPE_SINGLE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.org.wayriad.skillreal.provider.skills";

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }



}
