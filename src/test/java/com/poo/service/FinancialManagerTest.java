package com.poo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.poo.financial.model.Expense;
import com.poo.financial.model.ExpenseCategory;
import com.poo.financial.model.Income;
import com.poo.financial.model.IncomeCategory;
import com.poo.financial.model.Transaction;
import com.poo.financial.service.FinancialManager;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

public class FinancialManagerTest {

  @TempDir
  Path tempDir;

  Date todayDate = new Date();

  @BeforeEach
  public void setUp() throws Exception {
    clearTransactions();
  }

  @AfterEach
  public void tearDown() throws Exception {
    clearTransactions();
  }

  @Test
  public void testGetCurrentBalanceWithNoTransactions() throws Exception {
    String filePath = tempDir.resolve("data.csv").toString();
    FinancialManager.addTransaction(100.0, todayDate, IncomeCategory.SALARIO, filePath);

    assertEquals(100.0, FinancialManager.getCurrentBalance(),
        "Current balance should be zero when there are no transactions for today");
  }

  @Test
  public void testGetCurrentBalanceWithTransactions() throws Exception {
    String filePath = tempDir.resolve("data.csv").toString();
    Date dateMinuteAfter = new Date(todayDate.getTime() + 60000);
    FinancialManager.addTransaction(100.0, todayDate, IncomeCategory.SALARIO, filePath);
    FinancialManager.addTransaction(30.0, dateMinuteAfter, ExpenseCategory.ALIMENTACAO, filePath);

    double currentBalance = FinancialManager.getCurrentBalance();

    assertEquals(70.0, currentBalance, "Current balance should reflect the sum of today's transactions");
  }

  public static void clearTransactions() throws Exception {
    FinancialManager.getTransactions().clear();
  }

  @Test
  public void testAddIncomeTransaction() throws Exception {
    double amount = 100.0;
    Date date = new Date();
    IncomeCategory category = IncomeCategory.SALARIO;
    String filePath = tempDir.resolve("data.csv").toString();

    FinancialManager.addTransaction(amount, date, category, filePath);

    assertFalse(FinancialManager.getTransactions().isEmpty(), "Transactions list should not be empty");
    Transaction addedTransaction = FinancialManager.getTransactions().get(0);
    assertTrue(addedTransaction instanceof Income, "Added transaction should be an instance of Income");
    assertEquals(amount, addedTransaction.getBalance(), "Balance should be equal to the transaction amount");
  }

  @Test
  public void testAddExpenseTransaction() throws Exception {
    double amount = 50.0;
    Date date = new Date();
    ExpenseCategory category = ExpenseCategory.ALIMENTACAO;
    String filePath = tempDir.resolve("data.csv").toString();

    FinancialManager.addTransaction(amount, date, category, filePath);

    assertFalse(FinancialManager.getTransactions().isEmpty(), "Transactions list should not be empty");
    Transaction addedTransaction = FinancialManager.getTransactions().get(0);
    assertTrue(addedTransaction instanceof Expense, "Added transaction should be an instance of Expense");
    assertEquals(-amount, addedTransaction.getBalance(),
        "Balance should be negative and equal to the transaction amount");
  }

  @Test
  public void testGetTransactionsReturnsEmptyListInitially() throws Exception {
    ArrayList<Transaction> retrievedTransactions = FinancialManager.getTransactions();

    assertTrue(retrievedTransactions.isEmpty(), "Initially retrieved transactions should be empty");
  }

  @Test
  public void testGetTransactionsReturnsPopulatedList() throws Exception {
    double amount = 100.0;
    Date date = new Date();
    IncomeCategory incomeCategory = IncomeCategory.SALARIO;
    ExpenseCategory expenseCategory = ExpenseCategory.ALIMENTACAO;
    String incomeFilePath = tempDir.resolve("data.csv").toString();
    String expenseFilePath = tempDir.resolve("data.csv").toString();

    FinancialManager.addTransaction(amount, date, incomeCategory, incomeFilePath);
    FinancialManager.addTransaction(amount, date, expenseCategory, expenseFilePath);

    ArrayList<Transaction> retrievedTransactions = FinancialManager.getTransactions();

    assertEquals(2, retrievedTransactions.size(), "Retrieved transactions should contain the added transactions");
  }

  @Test
  public void testGetTransactionsWithTypeIncome() throws Exception {
    double amount = 100.0;
    Date date = new Date();
    String filePath = tempDir.resolve("transactions.csv").toString();
    FinancialManager.addTransaction(amount, date, IncomeCategory.SALARIO, filePath);
    FinancialManager.addTransaction(amount, date, ExpenseCategory.ALIMENTACAO, filePath);

    ArrayList<Transaction> incomeTransactions = FinancialManager.getTransactions("RECEITA");

    assertEquals(1, incomeTransactions.size(), "Should only retrieve income transactions");
    assertTrue(incomeTransactions.get(0) instanceof Income, "Retrieved transaction should be an instance of Income");
  }

  @Test
  public void testGetTransactionsWithTypeExpense() throws Exception {
    double amount = 100.0;
    Date date = new Date();
    String filePath = tempDir.resolve("transactions.csv").toString();
    FinancialManager.addTransaction(amount, date, IncomeCategory.SALARIO, filePath);
    FinancialManager.addTransaction(amount, date, ExpenseCategory.ALIMENTACAO, filePath);

    ArrayList<Transaction> expenseTransactions = FinancialManager.getTransactions("DESPESA");

    assertEquals(1, expenseTransactions.size(), "Should only retrieve expense transactions");
    assertTrue(expenseTransactions.get(0) instanceof Expense, "Retrieved transaction should be an instance of Expense");
  }

  @Test
  public void testGetTransactionsWithInvalidType() {
    Exception exception = assertThrows(InvalidParameterException.class, () -> {
      FinancialManager.getTransactions("INVALID_TYPE");
    });

    String expectedMessage = "Tipo de transação inválido";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage),
        "Should throw InvalidParameterException with the correct message");
  }

}
