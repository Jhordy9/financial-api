package com.poo.financial.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense extends Transaction {
  private ExpenseCategory category;
  public final String type = "DESPESA";

  public Expense() {
  }

  public Expense(double amount, Date date, ExpenseCategory category, String filePath) {
    super(amount, date);
    this.setCategory(category);
    save(filePath);
  }

  public Expense(double amount, Date date, String id, ExpenseCategory category) {
    super(amount, date, id);
    this.setCategory(category);
  }

  public String getType() {
    return type;
  }

  public ExpenseCategory getCategory() {
    return category;
  }

  public void setCategory(ExpenseCategory category) {
    this.category = category;
  }

  private void save(String filePath) {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    File csvFile = new File(filePath);
    boolean append = csvFile.exists() && csvFile.length() > 0;

    try (FileWriter fw = new FileWriter(csvFile, append);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {

      if (!append) {
        out.println("ID;Categoria;Tipo;Data;Valor");
      }

      String csvRow = String.join(";",
          this.getUuid().toString(),
          "DESPESA",
          this.getCategory().toString(),
          dateFormatter.format(this.getDate()),
          String.format("%.2f", this.getAmount()));

      out.println(csvRow);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
