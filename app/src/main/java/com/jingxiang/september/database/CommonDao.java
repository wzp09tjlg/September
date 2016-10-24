package com.jingxiang.september.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jingxiang.september.network.parse.ChannelItem;
import com.jingxiang.september.network.parse.UpdateBean;
import com.jingxiang.september.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/10/9.
 * 具体实现的DaoInterface的类 依旧是根据各个表来管理(数据的操作需要放在子线程中执行,因此在每次执行数据操作时需要开启子线程操作)
 * query()方法原型各个字段的意思
 * query(tableName,selectParams,selection,selectionArgs,groupBy,having,orderBy)
 *         表名      查询的字段(Str数组) 条件    条件参数     分组     分类    排序   （having的用法类似where 可用聚合方法）
 * insert(tableName,nullColumnHack,value)
 *         表名      当插入数据为空,系统置空的列名  插入数据的键值对ContentValues
 * delete(tableName,whereClause,whereArgs)
 *         表名      删除条件     删除条件参数(Str数组)
 * update(tableName,values,whereClause,whereArgs)
 *         表名      更新数据的键值对 更新条件  更新条件的值
 * 使用数据库时需要注意几点
 * 1.数据库操作必须在子线程中执行,在主线程中执行会卡顿甚至会出现anr
 * 2.使用游标时必须在使用完成之后关闭游标,不然会很耗费资源并且会卡顿。数据库权衡频繁打开和关闭与持续开启所耗费的资源，还是建议持续的开启，在应用最后退出的时候关闭
 * 3.在使用查询时记得关闭游标,在使用更新、插入、删除的时候，为保证数据的完整性 一定得使用事务,不然万一出现数据异常就会导致数据不一致的问题。
 */
public class CommonDao implements DaoInterface {
    /** Data */
    private CommonDB commonDB;

    /**************************************/
    public CommonDao(Context context){
        LogUtil.i("CommonDao  construct");
        commonDB = new CommonDB(context);
    }

    public void closeDb(){//关闭数据库
        LogUtil.i("CommonDB closeDB");
        if(commonDB != null){
            commonDB.close();
            LogUtil.i("CommonDB closed");
        }
    }

    /** 公共的方法 */
    //查看表是否在数据库中存在
    protected boolean isTableExist(String tableName){
        String sql = "SELECT count(*) FROM sqlite_master " +
                "WHERE type='table' AND name='" + tableName + "'";
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cur = null;
        try{
            cur = db.rawQuery(sql, null);
            int count = -1;
            while (cur.moveToNext()) {
                count = cur.getInt(0);
            }
            if (count <= 0) {
                return false;
            }
        }catch (Exception e){
            return false;
        }finally {
            if(!cur.isClosed())
                cur.close();
        }
        return true;
    }


    /** channel_news */
    //查询
    @Override
    public ChannelItem selectNewsById(int id) {
        if(id < 0) return null;
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cursor = null;
        ChannelItem item = new ChannelItem();
        try{
            cursor = db.query(CommonDB.TABLE_NEW,new String[]{"id","name","orderId","selected"}
                    ,"id=?",new String[]{String.valueOf(id)},null,null,null);
            if(cursor != null)
                cursor.moveToFirst();
            item.setId(cursor.getInt(cursor.getColumnIndex("id")));
            item.setName(cursor.getString(cursor.getColumnIndex("name")));
            item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
            item.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
        }catch (Exception e){
        }finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return item;
    }

    //插入
    @Override
    public boolean insertNewsItem(ChannelItem item) {
        if(item == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",item.getId());
        cv.put("name",item.getName());
        cv.put("orderId",item.getOrderId());
        cv.put("selected",item.getSelected());
        long tempResult = -1;
        try {
            db.beginTransaction();
            tempResult = db.insert(CommonDB.TABLE_NEW,null,cv);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
        }
        return tempResult != -1;
    }

    @Override
    public boolean insertNewsItem(List<ChannelItem> items) {
        if(items == null || items.size() <= 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int tempLen = items.size();
        long tempResult = 0;
        try{
            db.beginTransaction();
            for(int i=0;i<tempLen;i++){
              cv.put("id",items.get(i).getId());
              cv.put("name",items.get(i).getName());
              cv.put("orderId",items.get(i).getOrderId());
              cv.put("selected",items.get(i).getSelected());
              tempResult = db.insert(CommonDB.TABLE_NEW,null,cv);
              if(tempResult<1)//如果某一行插入失败,整体的插入数据过程是失败的
                  throw new Exception();
            }
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    //删除
    @Override
    public boolean deleteNewsItemById(int id) {
        if(id < 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.delete(CommonDB.TABLE_NEW,"id=?",new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult != 0;
    }

    //更新
    @Override
    public boolean updateNewsItem(int id, int selected) {
        if(id < 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("selected",selected);
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.update(CommonDB.TABLE_NEW,cv,"id=?",new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    @Override
    public boolean updateNewsItem(ChannelItem item) {
        if(item == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",item.getName());
        cv.put("orderId",item.getOrderId());
        cv.put("selected",item.getSelected());
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.update(CommonDB.TABLE_NEW,cv,"id=?",new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    //排序
    @Override
    public List<ChannelItem> selectSortNewsItemList() {
        if(!isTableExist(CommonDB.TABLE_NEW)) return null;
        List<ChannelItem> list = new ArrayList<>();
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cursor = db.query(CommonDB.TABLE_NEW,new String[]{"id","name","orderId","selected"},
                "",null,"","","");
        try{
            ChannelItem item = null;
            if(cursor != null)
                cursor.moveToFirst();
            while (cursor.moveToNext()){
                item = new ChannelItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
                item.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
                list.add(item);
            }
        }catch (Exception e){}
        finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;
    }

    /** channel_video */
    //查询
    @Override
    public ChannelItem selectVideoById(int id) {
        if(id < 0) return null;
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cursor = null;
        ChannelItem item = new ChannelItem();
        try{
            cursor = db.query(CommonDB.TABLE_VIDEO,new String[]{"id","name","orderId","selected"}
                    ,"id=?",new String[]{String.valueOf(id)},null,null,null);
            if(cursor != null)
                cursor.moveToFirst();
            item.setId(cursor.getInt(cursor.getColumnIndex("id")));
            item.setName(cursor.getString(cursor.getColumnIndex("name")));
            item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
            item.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
        }catch (Exception e){
        }finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return item;
    }

    //插入
    @Override
    public boolean insertVideoItem(ChannelItem item) {
        if(item == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",item.getId());
        cv.put("name",item.getName());
        cv.put("orderId",item.getOrderId());
        cv.put("selected",item.getSelected());
        long tempResult = -1;
        try {
            db.beginTransaction();
            tempResult = db.insert(CommonDB.TABLE_VIDEO,null,cv);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
        }
        return tempResult != -1;
    }

    @Override
    public boolean insertVideoItem(List<ChannelItem> items) {
        if(items == null || items.size() <= 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int tempLen = items.size();
        long tempResult = 0;
        try{
            db.beginTransaction();
            for(int i=0;i<tempLen;i++){
                cv.put("id",items.get(i).getId());
                cv.put("name",items.get(i).getName());
                cv.put("orderId",items.get(i).getOrderId());
                cv.put("selected",items.get(i).getSelected());
                tempResult = db.insert(CommonDB.TABLE_VIDEO,null,cv);
                if(tempResult<1)//如果某一行插入失败,整体的插入数据过程是失败的
                    throw new Exception();
            }
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    //删除
    @Override
    public boolean deleteVideoItemById(int id) {
        if(id < 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.delete(CommonDB.TABLE_VIDEO,"id=?",new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult != 0;
    }

    //更新
    @Override
    public boolean updateVideoItem(int id, int selected) {
        if(id < 0) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("selected",selected);
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.update(CommonDB.TABLE_VIDEO,cv,"id=?",new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    @Override
    public boolean updateVideoItem(ChannelItem item) {
        if(item == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",item.getName());
        cv.put("orderId",item.getOrderId());
        cv.put("selected",item.getSelected());
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.update(CommonDB.TABLE_VIDEO,cv,"id=?",new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    //排序
    @Override
    public List<ChannelItem> selectSortVideoItemList() {
        if(!isTableExist(CommonDB.TABLE_VIDEO)) return null;
        List<ChannelItem> list = new ArrayList<>();
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cursor = db.query(CommonDB.TABLE_VIDEO,new String[]{"id","name","orderId","selected"}
                ,"",null,"","","");
        try{
            ChannelItem item = null;
            if(cursor != null)
                cursor.moveToFirst();
            while(cursor.moveToNext()){
                item = new ChannelItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
                item.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
                list.add(item);
            }
        }catch (Exception e){}
        finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;
    }

    /** version_update */
    //查询
    @Override
    public UpdateBean selectUpdateBean(String versionCode) {
        if(!isTableExist(CommonDB.TABLE_VERSION_UPDATE)) return null;
        UpdateBean bean = new UpdateBean();
        SQLiteDatabase db = commonDB.getReadableDatabase();
        Cursor cursor = null;
        try{
             cursor = db.query(CommonDB.TABLE_VERSION_UPDATE,new String[]{"url,start,end,finished,status,updates"}
                     ,"versioncode=?",new String[]{versionCode},null,null,null);
            if(cursor != null){
                cursor.moveToFirst();
                bean.download_link = cursor.getString(cursor.getColumnIndex("url"));
                bean.start = cursor.getLong(cursor.getColumnIndex("start"));
                bean.end   = cursor.getLong(cursor.getColumnIndex("end"));
                bean.finished = cursor.getLong(cursor.getColumnIndex("finished"));
                bean.version_code = versionCode;
                bean.status = cursor.getInt(cursor.getColumnIndex("status"));
                bean.intro = cursor.getString(cursor.getColumnIndex("updates"));
            }
            return bean;
        }catch (Exception e){

        }finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

    //插入
    @Override
    public boolean insertUpdateBean(UpdateBean bean) {
        if(bean == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long tempResult = 0;
        try{
            db.beginTransaction();
            cv.put("url",bean.download_link);
            cv.put("start",bean.start);
            cv.put("end",bean.end);
            cv.put("finished",bean.finished);
            cv.put("versioncode",bean.version_code);
            cv.put("status",bean.status);
            cv.put("updates",bean.intro);
            tempResult = db.insert(CommonDB.TABLE_VERSION_UPDATE,null,cv);
            db.setTransactionSuccessful();
        }catch (Exception e){
            LogUtil.e("e.msg:" + e.getMessage());
        }
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }

    //删除单条记录
    @Override
    public boolean deleteUpdateBean(String versionCode) {
        if(TextUtils.isEmpty(versionCode)) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.delete(CommonDB.TABLE_VERSION_UPDATE
                    ,"versionCode=?",new String[]{versionCode});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult != 0;
    }

    //删除所有
    @Override
    public boolean deleteAllUpdateBean() {
        SQLiteDatabase db = commonDB.getWritableDatabase();
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.delete(CommonDB.TABLE_VERSION_UPDATE
                    ,null,null);
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult != 0;
    }

    //更新
    @Override
    public boolean updateUpdateBean(UpdateBean bean) {
        if(bean == null) return false;
        SQLiteDatabase db = commonDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("start",bean.start);
        cv.put("end",bean.end);
        cv.put("finished",bean.finished);
        long tempResult = 0;
        try {
            db.beginTransaction();
            tempResult = db.update(CommonDB.TABLE_VERSION_UPDATE,cv,"versioncode=?",new String[]{bean.version_code});
            db.setTransactionSuccessful();
        }catch (Exception e){}
        finally {
            db.endTransaction();
        }
        return tempResult > 0;
    }
}
