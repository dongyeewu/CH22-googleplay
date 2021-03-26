package tw.careabout.yourfamily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Elder_editDbHelper extends SQLiteOpenHelper {
    public String sCreateTableCommand;    // 資料庫名稱
    private static String DB_FILE = "";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    //這裡有變!!!!
    private static final String DB_TABLE = "elder";    // 資料庫物件，固定的欄位變數
    private static final String DB_TABLE1 = "member";

    private static final String crTBsql = "CREATE     TABLE  IF NOT EXISTS  " + DB_TABLE + "   ( "
            + "eld001    INTEGER   PRIMARY KEY," + "eld002 TEXT NOT NULL," + "eld003 TEXT,"+ "eld004 TEXT,"
            + "eld005 TEXT," + "eld006 TEXT," + "eld007 INTEGER," + "eld008 TEXT," + "eld009 TEXT," +
            "eld010 TEXT," + "eld011 TEXT," + "eld012 TEXT);";
    private static SQLiteDatabase database;



//    public FriendDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context){
        if (database == null || !database.isOpen())  {
            database = new Elder_editDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    public Elder_editDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
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
    public long insertRec(String e_name, String e_buidler, String e_sex,String e_blod,String e_birth,int e_group) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("eld002", e_name);
        rec.put("eld003", e_buidler);
        rec.put("eld004", e_sex);
        rec.put("eld005", e_blod);
        rec.put("eld006", e_birth);
        rec.put("eld007", e_group);
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    public int RecCount() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        int rgc=recSet.getCount();
//        選擇的TABLE資料有幾筆
        recSet.close();
        db.close();
        return rgc;
    }

    public ArrayList<String> FindRec(String g_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> recAry = new ArrayList<String>();
//        String[] fldSet;
        int c=Integer.valueOf(g_id);
        String sql = "SELECT * FROM " + DB_TABLE +
                " WHERE eld007 ='"+g_id+"'";
        String[] args = {"%" + g_id + "%"};
        Cursor recSet = db.rawQuery(sql,null);
        int columnCount = recSet.getColumnCount();
        int a=recSet.getCount();
        if (recSet.getCount() != 0)
        {
            while (recSet.moveToNext())
            {
                String fldSet="";
                for (int i = 0; i < columnCount; i++)
                {
                    fldSet += recSet.getString(i) + "###";
                }
                recAry.add(fldSet);
            }
        }
        recSet.close();
        db.close();
        return recAry;
    }
    public ArrayList<String> Findgroup(String m_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> recAry = new ArrayList<String>();
//        String[] fldSet;
        int c=Integer.valueOf(m_id);
        String sql = "SELECT * FROM " + DB_TABLE +
                " WHERE eld007 IN ( SELECT mem007 FROM "+ DB_TABLE1 +" WHERE mem001 = '"+ m_id +"')";
//        String[] args = {"%" + m_id + "%"};
        Cursor recSet = db.rawQuery(sql,null);
        int columnCount = recSet.getColumnCount();
        int a=recSet.getCount();
        if (recSet.getCount() != 0)
        {
            while (recSet.moveToNext())
            {
                String fldSet="";
                for (int i = 0; i < columnCount; i++)
                {
                    fldSet += recSet.getString(i) + "###";
                    recAry.add(fldSet);
                }
            }
        }
        recSet.close();
        db.close();
        return recAry;
    }
    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE +" ORDER BY eld001 DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "###";
            recAry.add(fldSet);
        }
        recSet.close();
        db.close();
        return recAry;
    }



    public ArrayList<String> selectElder(String e_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> elderRowData = new ArrayList<>();
        String sql = "SELECT*FROM "+DB_TABLE+" WHERE eld001 = ?";
        String[] args = { e_id }; //問號的查詢條件
        Cursor cursor = db.rawQuery(sql,args); //將args[ ]帶入sql的問號，會按照順序放
        int columnCount = cursor.getColumnCount();  //看有幾個欄位，column:欄位

        if( cursor.getCount()!=0 ){
            cursor.moveToFirst();
            for(int i=0; i<columnCount; i++){
                elderRowData.add(cursor.getString(i));
            }
        }
        cursor.close();
        db.close();
        return elderRowData;
    }

    public String[] FindMEMBER() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT*FROM "+DB_TABLE1;
        Cursor cursor = db.rawQuery(sql,null); //將args[ ]帶入sql的問號，會按照順序放
        int columnCount = cursor.getColumnCount();  //看有幾個欄位，column:欄位

        String mem[] =new String[columnCount];
        if( cursor.getCount()!=0 ){
            cursor.moveToFirst();
            for(int i=0; i<columnCount; i++){
                mem[i] = cursor.getString(i);
            }
        }
        cursor.close();
        db.close();
        return mem;
    }

    //--清空 elder SQLite資料
    public void clearelderRec(){
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
            String whereClause=" eld001 = '" + b_id + "'";
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE, whereClause, null); //

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
    public int updateRec(String e_id, String e_name, String e_buidler, String e_sex,String e_blod,String e_birth) {
        SQLiteDatabase db = getWritableDatabase();
        String sql= " SELECT * FROM " + DB_TABLE ;
        Cursor recSet=db.rawQuery(sql,null);
        if (recSet.getCount()!=0){
            ContentValues rec=new ContentValues();
            rec.put("eld002",e_name);
            rec.put("eld003",e_buidler);
            rec.put("eld004",e_sex);
            rec.put("eld005",e_blod);
            rec.put("eld006",e_birth);
            String whereClause=" eld001= '" + e_id + "'";
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
//        long rowID1 = db.insert(DB_TABLE1, null, rec);
//        db.close();
        return rowID;
    }
}
