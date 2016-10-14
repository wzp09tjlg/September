package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/10/10.
 */
public class ChannelItem implements Serializable {
    private int id;
    private String name;
    private int orderId;
    private int selected;

    public ChannelItem(){}

    public ChannelItem(int id,String name,int orderId,int selected){
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "{id:" + id + ";name:" + name + ";orderId:"
                + orderId + ";selected:" + selected + "}";
    }
}
