package com.example.littlethoughts.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.litepal.LitePal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThoughtsProvider extends ContentProvider {

    public static final int TODO_LIST = 0;

    public static final int TODO_ITEM = 1;

    public static final int THOUGHTS_LIST = 2;

    public static final int THOUGHTS_ITEM = 3;

    public static final String AUTHORITY = "com.example.littlethoughts.provider";

    private static UriMatcher uriMatcher;

    private SQLiteDatabase database;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "todolist", TODO_LIST);
        uriMatcher.addURI(AUTHORITY, "todoitem", TODO_ITEM);
        uriMatcher.addURI(AUTHORITY, "thoughtslist", THOUGHTS_LIST);
        uriMatcher.addURI(AUTHORITY, "thoughtsitem", THOUGHTS_ITEM);
    }

    @Override
    public boolean onCreate() {
        LitePal.initialize(getContext());
        database = LitePal.getDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case TODO_LIST:
                cursor = database.query("todolist", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TODO_ITEM:
                cursor = database.query("todoitem", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case THOUGHTS_LIST:
                cursor = database.query("thoughtslist", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case THOUGHTS_ITEM:
                cursor = database.query("thoughtsitem", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri uriReturn = null;
        long insertId;
        switch (uriMatcher.match(uri)) {
            case TODO_ITEM:
                insertId = database.insert("todoitem", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/todoitem/" + insertId);
                break;
            case THOUGHTS_ITEM:
                insertId = database.insert("thoughtsitem", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/thoughtsitem/" + insertId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteLines = 0;
        switch (uriMatcher.match(uri)) {
            case TODO_ITEM:
                deleteLines = database.delete("todoitem", selection, selectionArgs);
                break;
            case THOUGHTS_ITEM:
                deleteLines = database.delete("thoughtsitem", selection, selectionArgs);
                break;
            default:
                break;
        }
        return deleteLines;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updatedLines = 0;
        switch (uriMatcher.match(uri)) {
            case TODO_ITEM:
                updatedLines = database.update("todoitem", values, selection, selectionArgs);
                break;
            case THOUGHTS_ITEM:
                updatedLines = database.update("thoughtsitem", values, selection, selectionArgs);
                break;
        }
        return updatedLines;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String MIME = null;
        switch (uriMatcher.match(uri)) {
            case TODO_LIST:
                MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".todolist";
                break;
            case TODO_ITEM:
                MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".todoitem";
                break;
            case THOUGHTS_LIST:
                MIME = "vnd.android.cursor.dir.vnd." + AUTHORITY + ".thoughtslist";
                break;
            case THOUGHTS_ITEM:
                MIME = "vnd.android.cursor.dir.vnd." + AUTHORITY + ".thoughtsitem";
            default:
                break;
        }
        return MIME;
    }
}
