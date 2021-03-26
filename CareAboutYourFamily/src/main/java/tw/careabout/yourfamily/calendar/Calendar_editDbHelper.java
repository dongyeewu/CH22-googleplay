package tw.careabout.yourfamily.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Calendar_editDbHelper extends SQLiteOpenHelper {
    public String sCreateTableCommand;    // 資料庫名稱
    private static String DB_FILE = "";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    //這裡有變!!!!
    private static final String DB_TABLE = "cal100";    // 資料庫物件，固定的欄位變數

    private static final String crTBsql = "CREATE     TABLE  IF NOT EXISTS  " + DB_TABLE + "   ( "
            + "cal001    INTEGER   PRIMARY KEY," + "cal002 TEXT NOT NULL," + "cal003 TEXT,"+ "cal004 TEXT,"
            + "cal005 TEXT," + "cal006 TEXT," + "cal007 TEXT," + "cal008 TEXT," + "cal009 TEXT," +
            "cal010 TEXT," + "cal011 TEXT," + "cal012 TEXT," + "cal013 TEXT," +
            "cal014 TEXT," + "cal015 TEXT);";
    private static SQLiteDatabase database;



//    public FriendDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context){
        if (database == null || !database.isOpen())  {
            database = new Calendar_editDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    public Calendar_editDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
//        super(context, name, factory, version);
        super(context, name, null, 1);
        DB_FILE = name;
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP  TABLE  IF  EXISTS  " + DB_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    //因為有給   return 所以會自動生    String裡面的數值要記得改 重複會打架
    public long insertRec(String c_name, String c_content, String c_date,String c_time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("cal002", c_name);
        rec.put("cal003", c_content);
//        rec.put("address", b_address);
        rec.put("cal004", c_date);
        rec.put("cal005", c_time);

        long rowID = db.insert(DB_TABLE, null, rec);//會閃退
        db.close();
        return rowID;
    }

    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        int a=recSet.getCount();
//        選擇的TABLE資料有幾筆
        recSet.close();
        db.close();

        return recSet.getCount();


    }
    public String[] FindRec(String b_id) {
        SQLiteDatabase db = getReadableDatabase();



        String[] fldSet;
        int c=Integer.valueOf(b_id);
        String sql = "SELECT * FROM " + DB_TABLE +
                " WHERE cal001 ='"+b_id+"'";

        String[] args = {"%" + b_id + "%"};

        Cursor recSet = db.rawQuery(sql,null);


        int columnCount = recSet.getColumnCount();
//        Log.d(TAG,"ans:"+recSet.getCount());
        fldSet=new String[columnCount];
        int a=recSet.getCount();
        if (recSet.getCount() != 0)
        {
            //==================
//            for (int i = 0; i < columnCount; i++)
//            {
//                args[b][i]="";
//                a[b][i] += recSet.getString(i) + " ";
//            }
//
//            b++;
//            fldSet +="\n";
            //===================
//            recSet.moveToFirst();
//            fldSet[0] ="";
//
//                    recSet.getString(4) + " "
//                    + "\n";

            while (recSet.moveToNext())
            {
                for (int i = 0; i < columnCount; i++)
                {
                    fldSet[i] ="";
                    fldSet[i]+= recSet.getString(i) + " ";
                }

            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }
    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();

        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "###";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();

//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }
    public String[][] FindLIST() {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM " + DB_TABLE ;


        Cursor recSet = db.rawQuery(sql, null);

        int columnCount = recSet.getColumnCount();//資料欄位
        int count = recSet.getCount();// 資料列
        String a[][] =new String[recSet.getCount()][columnCount];

//        Log.d(TAG,"ans:"+recSet.getCount());
        if (recSet.getCount() != 0)
        {
//
            int b=0;

            while (recSet.moveToNext())
            {

                for (int i = 0; i < columnCount; i++)
                {
                    a[b][i]="";
                    a[b][i] += recSet.getString(i) + " ";
                }

                b++;
                fldSet +="\n";
            }
        }
        recSet.close();
        db.close();
        String[][] d=a;
        int c=0;
        return a;

    }

    public String[][] FindDATE(String cal004) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM " + DB_TABLE+
                " WHERE cal004 LIKE ? ORDER BY cal005 ASC";
        String[]args = {"%" + cal004 + "%"};



        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();//資料欄位
        int count = recSet.getCount();// 資料列
        String a[][] =new String[recSet.getCount()][columnCount];

//        Log.d(TAG,"ans:"+recSet.getCount());
        if (recSet.getCount() != 0)
        {
//            recSet.moveToFirst();
//            fldSet =
//                    recSet.getString(0)+","+
//                            recSet.getString(1)+","+
//                            recSet.getString(2)+","+
//                            recSet.getString(3)+","+
//                    recSet.getString(4) + " "
//                            + "\n";
            int b=0;

            while (recSet.moveToNext())
            {

                for (int i = 0; i < columnCount; i++)
                {
                    a[b][i]="";
                    a[b][i] += recSet.getString(i);
                }

                b++;
                fldSet +="\n";
            }
        }
        recSet.close();
        db.close();
        String[][] d=a;
        int c=0;
        return a;

    }
    public int clearRec(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        int h=recSet.getColumnCount();
        int l=0;
        if (recSet.getCount()!=0){
            int rowsAffected = db.delete(DB_TABLE, "1", null);
            recSet.close();
            db.close();
            return rowsAffected;
        }else
        {
            recSet.close();
            db.close();
            return -1;
        }

    }
    public int deleteRec(String b_id) {
        //=============================
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause=" cal001 = '" + b_id + "'";
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE, whereClause, null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
        //============================
//        return 0;
    }
    public void createTB(String b_d) {
        // 批次新增
        int maxrec = 9;
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < maxrec; i++) {
            ContentValues newRow = new ContentValues();
            newRow.put("cal002", "帶路人" + u_chinayear(i)+"去看牙醫");
            newRow.put("cal003", "拔了" + u_chinano((int) (Math.random() * 4 + 1)) + "顆牙齒");
            newRow.put("cal004", b_d);
            newRow.put("cal005", "0"+i+":00");
            db.insert(DB_TABLE, null, newRow);
        }
        db.close();
    }

    private String u_chinano(int input_i) {
        String c_number = "";
        String china_no[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        c_number = china_no[input_i % 10];

        return c_number;
    }

    private String u_chinayear(int input_i) {
        String c_number = "";
        String china_no[] = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        c_number = china_no[input_i % 10];

        return c_number;
    }
    public int updateRec(String b_id, String b_title, String b_content, String b_date, String b_time) {
        SQLiteDatabase db = getWritableDatabase();
        String sql= " SELECT * FROM " + DB_TABLE ;
        Cursor recSet=db.rawQuery(sql,null);
        if (recSet.getCount()!=0){
            ContentValues rec=new ContentValues();
            rec.put("cal002",b_title);
            rec.put("cal003",b_content);
            rec.put("cal004",b_date);
            rec.put("cal005",b_time);
            String whereClause=" cal001= '" + b_id + "'";
            int rowsAffected = db.update(DB_TABLE,rec,whereClause,null);
            recSet.close();
            db.close();
            return rowsAffected;

        }else{
            recSet.close();
            db.close();
            return -1;
        }


    }


    public void creatfile() {
        SQLiteDatabase db = getWritableDatabase();
        int a=0;
        db.execSQL(crTBsql);
        db.close();
    }

    public long insertRec_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    public String[] FindMEMBER() {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM member";
        Cursor recSet = db.rawQuery(sql, null);
        int columnCount = recSet.getColumnCount();//資料欄位
        int count = recSet.getCount();// 資料列
        String mem007="";
//        String a[][] =new String[recSet.getCount()][columnCount];
//        Log.d(TAG,"ans:"+recSet.getCount());

        String mem[] =new String[columnCount];
        if (recSet.getCount() != 0)
        {
            while (recSet.moveToNext()){
                for (int i = 0; i < columnCount; i++)
                {
                    mem[i]="";
                    mem[i] = recSet.getString(i);
                }
            }
//        }else {
//            for (int i = 0; i < columnCount; i++)
//            {
//                mem[i]="99";
//
//            }
        }
        recSet.close();
        db.close();
        return mem;
    }
}
