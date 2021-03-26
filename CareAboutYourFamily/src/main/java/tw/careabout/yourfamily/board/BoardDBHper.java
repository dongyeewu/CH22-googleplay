package tw.careabout.yourfamily.board;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardDBHper extends SQLiteOpenHelper {

    private static final String DB_TABLE = "board";    // 資料庫物件，固定的欄位變數
    private static final String createBoardTable = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + " ( "
            + "boa001 INTEGER ," + "boa002 TEXT NOT NULL," + "boa003 TEXT NOT NULL,"
            + "boa004 TEXT NOT NULL,"  + "boa005 TEXT ," + "boa006 TEXT ," + "boa007 INTEGER ,"
            + "boa008 TEXT ,"  + "boa009 TEXT ," + "boa010 TEXT ," + "boa011 TEXT ,"
            + "boa012 TEXT ,"  + "boa013 TEXT ) ";


    public BoardDBHper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createBoardTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(createBoardTable);
    }

    public int checkBoardTable(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT * FROM "+DB_TABLE;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount()!=0){
            cursor.close();
            db.close();
            return 1;
        }
        cursor.close();
        db.close();
        return 0;
    }

    //--建立Board SQLite資料
    public void insertBoardRec(int boa001, String boa002, String boa003, String boa004, String boa005, String boa006, int boa007,
                                String boa008, String boa009, String boa010, String boa011, String boa012, String boa013) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("boa001", boa001); //id
        rec.put("boa002", boa002); //創建者
        rec.put("boa003", boa003); //創建時間
        rec.put("boa004", boa004); //標題
        rec.put("boa005", boa005); //內容
        rec.put("boa006", boa006); //編輯時間
        rec.put("boa007", boa007); //群組ID
        rec.put("boa008", boa008); //預備欄位
        rec.put("boa009", boa009); //預備欄位
        rec.put("boa010", boa010); //預備欄位
        rec.put("boa011", boa011); //預備欄位
        rec.put("boa012", boa012); //預備欄位
        rec.put("boa013", boa013); //預備欄位
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
    }


    //--抓取所有Board SQLite 資料
    public ArrayList< Map<String,Object> > findAllRec(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT * FROM "+DB_TABLE+" ORDER BY boa001 DESC ";
        Cursor cursor = db.rawQuery(sql,null);

        ArrayList< Map<String,Object> > boardRec = new ArrayList<>();

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for(int i=0; i<cursor.getCount(); i++){
                Map<String,Object> item = new HashMap<>();
                item.put("boa001", cursor.getInt(0)); //id
                item.put("boa002", cursor.getString(1)); //創建者
                item.put("boa003", cursor.getString(2)); //創建時間
                item.put("boa004", cursor.getString(3)); //標題
                item.put("boa005", cursor.getString(4)); //內容
                item.put("boa006", cursor.getString(5)); //編輯時間
                item.put("boa007", cursor.getInt(6)); //群組ID
                item.put("boa008", cursor.getString(7)); //預備欄位
                item.put("boa009", cursor.getString(8)); //預備欄位
                item.put("boa010", cursor.getString(9)); //預備欄位
                item.put("boa011", cursor.getString(10)); //預備欄位
                item.put("boa012", cursor.getString(11)); //預備欄位
                item.put("boa013", cursor.getString(12)); //預備欄位
                boardRec.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return boardRec;
    }

    //--抓取自己的Board SQLite 資料
    public ArrayList< Map<String,Object> > findmyRec(String user){
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT * FROM "+DB_TABLE+" WHERE boa002=? ORDER BY boa001 DESC ";
        String[] args = { user }; //問號的查詢條件
        Cursor cursor = db.rawQuery(sql,args);

        ArrayList< Map<String,Object> > boardRec = new ArrayList<>();

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for(int i=0; i<cursor.getCount(); i++){
                Map<String,Object> item = new HashMap<>();
                item.put("boa001", cursor.getInt(0)); //id
                item.put("boa002", cursor.getString(1)); //創建者
                item.put("boa003", cursor.getString(2)); //創建時間
                item.put("boa004", cursor.getString(3)); //標題
                item.put("boa005", cursor.getString(4)); //內容
                item.put("boa006", cursor.getString(5)); //編輯時間
                item.put("boa007", cursor.getInt(6)); //群組ID
                item.put("boa008", cursor.getString(7)); //預備欄位
                item.put("boa009", cursor.getString(8)); //預備欄位
                item.put("boa010", cursor.getString(9)); //預備欄位
                item.put("boa011", cursor.getString(10)); //預備欄位
                item.put("boa012", cursor.getString(11)); //預備欄位
                item.put("boa013", cursor.getString(12)); //預備欄位
                boardRec.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return boardRec;
    }

    //--抓取所有Board SQLite simple資料
    public ArrayList<SimpleBoardData> findAllRec_simple(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT boa002, boa003, boa004, boa006 FROM "+DB_TABLE+" ORDER BY boa001 DESC ";
        Cursor cursor = db.rawQuery(sql,null);

        ArrayList<SimpleBoardData> boardRec = new ArrayList<>();
        String showTime = "";

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for(int i=0; i<cursor.getCount(); i++){
                if(cursor.getString(3).equals("null")){
                    showTime = cursor.getString(1);
                }
                if(!cursor.getString(3).equals("null")){
                    showTime = cursor.getString(3);
                }
                SimpleBoardData item = new SimpleBoardData(
                        cursor.getString(2), //標題
                        cursor.getString(0), //創建者
                        showTime  //創建時間
                );
                boardRec.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return boardRec;
    }

    //--抓取自己的 Board SQLite simple資料
    public ArrayList<SimpleBoardData> findmyRec_simple(String user){
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT boa002, boa003, boa004, boa006 FROM "+DB_TABLE+" WHERE boa002= ? ORDER BY boa001 DESC ";
        String[] args = { user }; //問號的查詢條件
        Cursor cursor = db.rawQuery(sql,args);

        ArrayList<SimpleBoardData> boardRec = new ArrayList<>();
        String showTime = "";

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            for(int i=0; i<cursor.getCount(); i++){
                if(cursor.getString(3).equals("null")){
                    showTime = cursor.getString(1);
                }
                if(!cursor.getString(3).equals("null")){
                    showTime = cursor.getString(3);
                }
                SimpleBoardData item = new SimpleBoardData(
                        cursor.getString(2), //標題
                        cursor.getString(0), //創建者
                        showTime  //創建時間
                );
                boardRec.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return boardRec;
    }

    //--更新Board SQLite 資料
    public void updateRec(int id, String title, String context, String editTime){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues updateData = new ContentValues();
        updateData.put("boa004",title);
        updateData.put("boa005",context);
        updateData.put("boa006",editTime);
        String whereClause = "boa001=?";
        String whereArg[] = { Integer.toString(id) };

        int rowsAffected = db.update(DB_TABLE,updateData,whereClause,whereArg);

        db.close();
    }

    //--刪除 Board SQLite資料
    public void deleteBoardRec(int id){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE boa001 = ? ";
        String arg[] = { String.valueOf(id) };
        Cursor cursor = db.rawQuery(sql, arg);
        if (cursor.getCount() != 0) {
            String whereClause = " boa001 = ? ";
            int rowsAffected = db.delete(DB_TABLE, whereClause, arg);
            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }
    }

    //--清空 Board SQLite資料
    public void clearBoardRec(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            int rowsAffected = db.delete(DB_TABLE, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }
    }
}
