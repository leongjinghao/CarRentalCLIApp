package com.carrentalproj.entity;

public class Notification {

    private int id;
    private String message;

    private Member member;

    public Notification() {
    }

    public Notification(int id, Member member, String message) {
        this.id = id;
        this.member = member;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", member=" + member +
                '}';
    }
}
