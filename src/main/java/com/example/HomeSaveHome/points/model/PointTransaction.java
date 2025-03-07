package com.example.HomeSaveHome.points.model;

public class PointTransaction {
    private int amount;
    private String description;
    // 날짜 등 추가 필드를 넣을 수 있음

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
