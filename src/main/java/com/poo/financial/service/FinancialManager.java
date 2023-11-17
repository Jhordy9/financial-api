package com.poo.financial.service;

import java.util.Date;

import com.poo.financial.model.Expense;
import com.poo.financial.model.ExpenseCategory;
import com.poo.financial.model.Income;
import com.poo.financial.model.IncomeCategory;
import com.poo.financial.model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FinancialManager {
  private static ArrayList<Transaction> transactions = new ArrayList<>();

  public static void addTransaction(double amount, Date date, IncomeCategory category) {
    new Income(amount, date, category);
  }

  public static void addTransaction(double amount, Date date, ExpenseCategory category) {
    new Expense(amount, date, category);
  }

  private static void addExistingTransaction(double amount, Date date, String id, IncomeCategory category) {
    transactions.add(new Income(amount, date, id, category));
  }

  private static void addExistingTransaction(double amount, Date date, String id, ExpenseCategory category) {
    transactions.add(new Expense(amount, date, id, category));
  }

  public static ArrayList<Transaction> getTransactions() throws ParseException {
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/poo/financial/model/data.csv"))) {
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] values = line.split(";");
        String id = values[0].toString();
        String type = values[1];
        String categoryString = values[2];
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(values[3].trim());
        double amount = Double.parseDouble(values[4]);

        if (type.equals("RECEITA")) {
          IncomeCategory category = IncomeCategory.valueOf(categoryString);
          if (transactions.isEmpty()) {
            addExistingTransaction(amount, date, id, category);
          }

          if (!transactionExists(id)) {
            addExistingTransaction(amount, date, id, category);
          }
        }

        if (type.equals("DESPESA")) {
          ExpenseCategory category = ExpenseCategory.valueOf(categoryString);

          if (transactions.isEmpty()) {
            addExistingTransaction(amount, date, id, category);
          }

          if (!transactionExists(id)) {
            addExistingTransaction(amount, date, id, category);
          }
        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return transactions;
  }

  private static boolean transactionExists(String id) {
    return transactions.stream().anyMatch(t -> t.getUuid().equals(id));
  }

}
