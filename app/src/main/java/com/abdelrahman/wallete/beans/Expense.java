package com.abdelrahman.wallete.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AbdelRahman on 8/26/2016.
 */
public class Expense extends RealmObject {
    private double amount;
    private String purpose;
    @PrimaryKey
    private long addedDate;
    private long when;

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public Expense(double amount, String purpose, long addedDate,long when) {
        this.amount = amount;
        this.purpose = purpose;
        this.addedDate = addedDate;
        this.when=when;
    }

    public Expense() {
    }
}
