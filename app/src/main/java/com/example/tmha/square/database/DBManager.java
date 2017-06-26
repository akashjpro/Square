package com.example.tmha.square.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tmha.square.model.Project;
import com.example.tmha.square.model.Report;

import java.util.ArrayList;

/**
 * Created by tmha on 6/21/2017.
 */

public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "project.sqlite";
    private static final String TABLE_PRỌJECT    = "Project";
    private static final String ID               = "Id";
    private static final String NAME_PROJECT     = "NameProject";
    private static final String PHOTO            = "PhotoProject";
    private static final String PROGESS          = "Progess";
    private static final String START_TIME       = "StartTime";
    private static final String END_TIME         = "EndTime";
    private static final String CONTENT          = "Content";
    private static final String ADDRESS          = "Address";
    private static final String CREATE_BY        = "CreateBy";
    private static final String TIMRE_CREATE     = "TimeCreate";
    // DBName default save in folder: /data/data/package/database/project.sqlite
    private static int versionDB = 1;

    private static SQLiteDatabase database;
    private Context mContext;

    private static final String TABLE_REPORT    = "Report";
    private static final String NAME_REPORT     = "NameReport";
    private static final String ID_PROJECT      = "IdProject";
    private static final String ALBUM_PHOTO     = "Album";


    private static final String QUERY_TABLE_PROJECT
            = "CREATE  TABLE " + TABLE_PRỌJECT + " (" +
            ID +" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , " +
            NAME_PROJECT + " TEXT, " +
            PHOTO        + " TEXT, " +
            PROGESS      + " INTEGER, " +
            START_TIME   + " TEXT, " +
            END_TIME     + " TEXT, " +
            CONTENT      + " TEXT, " +
            ADDRESS      + " TEXT, " +
            CREATE_BY    + " TEXT, " +
            TIMRE_CREATE + " TEXT)";

    private static final String QUERY_TABLE_REPORT
            = "CREATE  TABLE " + TABLE_REPORT + " (" +
            ID +" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , " +
            ID_PROJECT   + " INTEGER, "   +
            NAME_REPORT  + " TEXT, "    +
            CONTENT      + " TEXT, "    +
            ALBUM_PHOTO  + " TEXT, "    +
            CREATE_BY    + " TEXT, "    +
            TIMRE_CREATE + " TEXT)";



    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, versionDB);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table project
        db.execSQL(QUERY_TABLE_PROJECT);
        //create table report
        db.execSQL(QUERY_TABLE_REPORT);
    }

    // upgrade version when add & remove table or other properties
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

    }

    /**
     * open database
     */
    public void openDB(){
        database = getWritableDatabase();// same opendatabase to write
    }

    /**
     * Insert project data
     * @param project
     * @return
     */
    public boolean insertProject(Project project){
        boolean result = false;
        try {
            openDB();
            ContentValues values = getValuesProject(project);
            long currentID = database.insert(TABLE_PRỌJECT, null, values);
            if(currentID >0 ){
                return true;
            }
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            close();
        }


        return  result;
    }


    public boolean insertReport(Report report){
        boolean result = false;
        try {
            openDB();
            ContentValues values = getValuesReport(report);
            long currentID = database.insert(TABLE_REPORT, null, values);
            if(currentID >0 ){
                return true;
            }
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            close();
        }


        return  result;
    }


    /**
     * get list project
     * @return
     */
    public ArrayList<Project> getLimitproject(int row, int fromIndex){
        ArrayList<Project> listproject = new ArrayList<>();
        try {
            openDB();
            Cursor cursor
                    = database.rawQuery("SELECT * FROM " + TABLE_PRỌJECT +
                    " ORDER BY ID DESC "  +
                    "LIMIT " +  row + " OFFSET "+ fromIndex, null);
            Project project;
            while(cursor.moveToNext()){
                project = new Project(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );
                listproject.add(project);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            close();
        }
        return listproject;
    }


    //SELECT * FROM Report ORDER BY ID DESC LIMIT 3 OFFSET 2;
    public ArrayList<Report> getLimitReport(int row, int fromIndex, int idProject){
        ArrayList<Report> listReport = new ArrayList<>();
        try {
            openDB();

            String sql = "SELECT * FROM " + TABLE_REPORT +
                    " WHERE " + ID_PROJECT + " = " + idProject +
                    " ORDER BY "+ ID +" DESC "  +
                    "LIMIT " +  row + " OFFSET "+ fromIndex;

            Cursor cursor
                    = database.rawQuery(sql, null);
            Report report;
            while(cursor.moveToNext()){
                report = new Report(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                listReport.add(report);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            close();
        }
        return listReport;
    }



    /**
     * get one project
     * @param id
     * @return
     */
    public Project getProject(String id){
        Project project = new Project();
        try {
            openDB();
            Cursor cursor
                    = database.rawQuery("SELECT * FROM "
                    + TABLE_PRỌJECT +
                    " WHERE " + ID + " = " + id , null);
            while(cursor.moveToNext()){
                project = new Project(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            close();
        }
        return project;
    }


    public Report getReport(String id){
        Report report = new Report();
        try {
            openDB();
            Cursor cursor
                    = database.rawQuery("SELECT * FROM "
                    + TABLE_REPORT +
                    " WHERE " + ID + " = " + id , null);
            while(cursor.moveToNext()){
                report = new Report(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            close();
        }
        return report;
    }

    /**
     * update project
     * @param project
     * @return
     */
    public  boolean updateProject(Project project){
        boolean result = false;
        try {
            openDB();
            ContentValues values = getValuesProject(project);
            int KQ = database.update(TABLE_PRỌJECT, values,
                    ID + " = " + project.getmID(), null);
            if(KQ > 0){
                return true;
            }
        }catch (Exception e){

        }finally {
            close();
        }

        return result;
    }


    public  boolean updateReport(Report report){
        boolean result = false;
        try {
            openDB();
            ContentValues values = getValuesReport(report);
            int KQ = database.update(TABLE_REPORT, values,
                    ID + " = " + report.getmID(), null);
            if(KQ > 0){
                return true;
            }
        }catch (Exception e){

        }finally {
            close();
        }

        return result;
    }


    /**
     * delete project
     * @param id
     * @return
     */
    public boolean deleteProject(String id){
        boolean result = false;
        try {
            openDB();
            int KQ = database.delete(TABLE_PRỌJECT,
                    ID + " = " + id, null);
            if(KQ > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return result;
    }

    /**
     * delete report with id or delete all report with idProject
     * @param id
     * @param idProject
     * @return
     */
    public boolean deleteReport(String id, String idProject){
        boolean result = false;
        try {
            openDB();
            int resultDelete = 0;
            if(id != null) {
                 resultDelete = database.delete(TABLE_REPORT,
                        ID + " = " + id, null);
            }else{
                resultDelete = database.delete(TABLE_REPORT,
                        ID_PROJECT + " = " + idProject, null);
            }
            if(resultDelete > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return result;
    }

    public Integer getMaxIdProject(){
        int id = 0;
        try {
            openDB();
            Cursor cursor = database.rawQuery("SELECT MAX(Id) FROM Project ", null);
            id = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return  id;
    }

    public Integer getMaxIdReport(){
        int id = 0;
        try {
            openDB();
            Cursor cursor = database.rawQuery("SELECT MAX( "+ ID +" ) FROM " + TABLE_REPORT, null);
            id = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return  id;
    }



    /**
     * get content values of project
     * @param project
     * @return
     */
    private ContentValues getValuesProject(Project project){
        ContentValues values = new ContentValues();
        values.put(NAME_PROJECT, project.getmProjectName());
        values.put(PHOTO, project.getmProjectPhoto());
        values.put(PROGESS, project.getmProgess());
        values.put(START_TIME, project.getmStartTime());
        values.put(END_TIME, project.getmEndTime());
        values.put(CONTENT, project.getmProjectContent());
        values.put(ADDRESS, project.getmAddress());
        values.put(CREATE_BY, project.getmCreateBy());
        values.put(TIMRE_CREATE, project.getmTimeCreate());

        return values;
    }

    /**
     * get content values report
     * @param report
     * @return
     */

    private ContentValues getValuesReport(Report report){
        ContentValues values = new ContentValues();
        values.put(ID_PROJECT, report.getmIdProject());
        values.put(NAME_REPORT, report.getmReportName());
        values.put(CONTENT, report.getmContent());
        values.put(ALBUM_PHOTO, report.getmAlbum());
        values.put(CREATE_BY, report.getmCreateBy());
        values.put(TIMRE_CREATE, report.getmTimeReport());

        return values;
    }

}
