package com.poo.financial.model;

import java.util.Date;
import java.util.UUID;

public class Transaction {
  private Date date;
  private double amount;
  private String id = UUID.randomUUID().toString();

  public Transaction() {
  }

  public Transaction(double amount, Date date) {
    this.date = date;
    this.amount = amount;
  }

  public Transaction(double amount, Date date, String id) {
    this.date = date;
    this.amount = amount;
    this.id = id;
  }

  public String getUuid() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  protected void setDate(Date date) {
    this.date = date;
  }

  public double getAmount() {
    return amount;
  }

  protected void setAmount(double amount) {
    this.amount = amount;
  }
}
