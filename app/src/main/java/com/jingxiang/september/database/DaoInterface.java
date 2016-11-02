package com.jingxiang.september.database;

import com.jingxiang.september.network.parse.ChannelItem;
import com.jingxiang.september.network.parse.UpdateBean;

import java.util.List;

/**
 * Created by wu on 2016/10/9.
 * dao中执行的动作都统一在这里定义,便于统一管理。这里是根据表名来划分逻辑处理
 */
public interface DaoInterface {
    /** channel_news */
    //查询
    ChannelItem selectNewsById(int id);
    //插入
    boolean insertNewsItem(ChannelItem item);
    boolean insertNewsItem(List<ChannelItem> items);
    //删除
    boolean deleteNewsItemById(int id);
    //更新
    boolean updateNewsItem(int id, int selected);
    boolean updateNewsItem(ChannelItem item);
    //排序
    List<ChannelItem> selectSortNewsItemList();

    /** channel_video*/
    //查询
    ChannelItem selectVideoById(int id);
    //插入
    boolean insertVideoItem(ChannelItem item);
    boolean insertVideoItem(List<ChannelItem> items);
    //删除
    boolean deleteVideoItemById(int id);
    //更新
    boolean updateVideoItem(int id, int selected);
    boolean updateVideoItem(ChannelItem item);
    //排序
    List<ChannelItem> selectSortVideoItemList();

    /** version_update */
    //查询
    UpdateBean selectUpdateBean(String versionCode);
    UpdateBean selectUpdateBean();
    //插入
    boolean insertUpdateBean(UpdateBean bean);
    //删除
    boolean deleteUpdateBean(String versionCode);
    boolean deleteAllUpdateBean();
    //更新
    boolean updateUpdateBean(UpdateBean bean);
}
