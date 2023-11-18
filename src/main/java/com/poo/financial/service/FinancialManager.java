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
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class FinancialManager {
  private static ArrayList<Transaction> transactions = new ArrayList<>();

  public static void addTransaction(double amount, Date date, IncomeCategory category) {
    new Income(amount, date, category);
  }

  public static void addTransaction(double amount, Date date, ExpenseCategory category) {
    new Expense(amount, date, category);
  }

  private static void addExistingTransaction(double amount, Date date, String id, IncomeCategory category) {
    Income income = new Income(amount, date, id, category);

    if (transactions.isEmpty()) {
      income.setBalance(amount);
    } else {
      Transaction lastTransaction = transactions.get(transactions.size() - 1);

      income.setBalance(lastTransaction.getBalance() + amount);
    }

    transactions.add(income);
  }

  private static void addExistingTransaction(double amount, Date date, String id, ExpenseCategory category) {
    Expense expense = new Expense(amount, date, id, category);

    if (transactions.isEmpty()) {
      expense.setBalance(amount);
    } else {
      Transaction lastTransaction = transactions.get(transactions.size() - 1);

      expense.setBalance(lastTransaction.getBalance() - amount);
    }

    transactions.add(expense);
  }

  public static ArrayList<Transaction> getTransactions() throws Exception {
    loadTransactions();
    return transactions;
  }

  public static ArrayList<Transaction> getTransactions(String type) throws Exception {
    loadTransactions();
    if (type.equalsIgnoreCase("RECEITA")) {
      return transactions.stream()
          .filter(t -> t instanceof Income)
          .collect(Collectors.toCollection(ArrayList::new));
    }

    if (type.equalsIgnoreCase("DESPESA")) {
      return transactions.stream()
          .filter(t -> t instanceof Expense)
          .collect(Collectors.toCollection(ArrayList::new));
    }

    throw new InvalidParameterException("Tipo de transação inválido");
  }

  private static void loadTransactions() throws Exception {
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

    Collections.sort(transactions, (t1, t2) -> t2.getDate().compareTo(t1.getDate()));
  }

  private static boolean transactionExists(String id) {
    return transactions.stream().anyMatch(t -> t.getUuid().equals(id));
  }

}
