package com.poo.financial.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Income extends Transaction {
  private IncomeCategory category;

  public Income() {
  }

  public Income(double amount, Date date, IncomeCategory category) {
    super(amount, date);
    this.setCategory(category);
    save();
  }

  public Income(double amount, Date date, String id, IncomeCategory category) {
    super(amount, date, id);
    this.setCategory(category);
    save();
  }

  public IncomeCategory getCategory() {
    return category;
  }

  public void setCategory(IncomeCategory category) {
    this.category = category;
  }

  private void save() {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    File csvFile = new File("data.csv");
    boolean append = csvFile.exists() && csvFile.length() > 0;

    try (FileWriter fw = new FileWriter(csvFile, append);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {

      if (!append) {
        out.println("ID;Categoria;Tipo;Data;Valor");
      }

      String csvRow = String.join(";",
          this.getUuid().toString(),
          "RECEITA",
          this.getCategory().toString(),
          dateFormatter.format(this.getDate()),
          String.format("%.2f", this.getAmount()));

      out.println(csvRow);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
