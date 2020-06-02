package com.student.dashboard.entity;

public class Notice {

    public String filter, head, body, type, from;
    public long stamp;

    public Notice() {
    }

    public Notice(String filter, String head, String body, String type, String from, long stamp) {
        this.filter = filter;
        this.head = head;
        this.body = body;
        this.type = type;
        this.from = from;
        this.stamp = stamp;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

}

