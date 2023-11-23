package com.poo.financial.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poo.financial.model.ExpenseCategory;
import com.poo.financial.model.IncomeCategory;
import com.poo.financial.model.Transaction;
import com.poo.financial.service.FinancialManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Rest Controller that handles financial transactions and balances.
 * Provides endpoints for retrieving and managing financial records.
 */
@RestController
@RequestMapping("/api")
public class FinancialController {
    /**
     * Retrieves a list of transactions, optionally filtered by type.
     * 
     * @param type Optional request parameter to filter transactions by type,
     *             such as "RECEITA" for income or "DESPESA" for expenses.
     * @return A {@link ResponseEntity} containing a list of {@link Transaction}
     *         objects and HTTP status.
     * @throws Exception If there is a parsing exception or any internal exception.
     */
    @GetMapping("/transactions")
    public ResponseEntity<ArrayList<Transaction>> getTransactions(@RequestParam(required = false) String type)
            throws Exception {
        ArrayList<Transaction> transactions;
        try {
            if (type == null || type.isEmpty()) {
                transactions = FinancialManager.getTransactions();
            } else {
                transactions = FinancialManager.getTransactions(type);
            }
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        return new ResponseEntity<>(new ArrayList<>(transactions), HttpStatus.OK);
    }

    /**
     * Retrieves the current balance from all transactions.
     * 
     * @return A {@link ResponseEntity} with the current balance as a {@link Double}
     *         and HTTP status.
     * @throws Exception If there is an exception while calculating the balance.
     */
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() throws Exception {
        double balance = FinancialManager.getBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    /**
     * Retrieves the current balance of the day's transactions.
     * 
     * @return A {@link ResponseEntity} with the current day's balance as a
     *         {@link Double} and HTTP status.
     * @throws Exception If there is an exception while calculating the balance.
     */
    @GetMapping("/current-balance")
    public ResponseEntity<Double> getCurrentBalance() throws Exception {
        double balance = FinancialManager.getCurrentBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    /**
     * Adds an income transaction to the financial records.
     * 
     * @param income A {@link JsonNode} containing the income data: amount, date,
     *               and category.
     * @return A {@link ResponseEntity} with HTTP status indicating the result of
     *         the operation.
     */
    @PostMapping("/income")
    public ResponseEntity<?> addIncome(@RequestBody JsonNode income) {
        try {

            double amount = Double.parseDouble(income.get("amount").asText().replace(",", "."));
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(income.get("date").asText());
            IncomeCategory category = IncomeCategory.valueOf(income.get("category").asText());

            FinancialManager.addTransaction(amount, date, category, "src/main/java/com/poo/financial/model/data.csv");

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds an expense transaction to the financial records.
     * 
     * @param expense A {@link JsonNode} containing the expense data: amount, date,
     *                and category.
     * @return A {@link ResponseEntity} with HTTP status indicating the result of
     *         the operation.
     */
    @PostMapping("/expense")
    public ResponseEntity<?> addExpense(@RequestBody JsonNode expense) {
        try {
            double amount = Double.parseDouble(expense.get("amount").asText().replace(",", "."));
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(expense.get("date").asText());
            ExpenseCategory category = ExpenseCategory.valueOf(expense.get("category").asText());

            FinancialManager.addTransaction(amount, date, category, "src/main/java/com/poo/financial/model/data.csv");

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}