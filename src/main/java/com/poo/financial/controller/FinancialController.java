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

@RestController
@RequestMapping("/api")
public class FinancialController {
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

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() throws Exception {
        double balance = FinancialManager.getBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @GetMapping("/current-balance")
    public ResponseEntity<Double> getCurrentBalance() throws Exception {
        double balance = FinancialManager.getCurrentBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/income")
    public ResponseEntity<?> addIncome(@RequestBody JsonNode incomeData) {
        try {
            double amount = incomeData.get("amount").asDouble();
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(incomeData.get("date").asText());
            IncomeCategory category = IncomeCategory.valueOf(incomeData.get("category").asText());

            FinancialManager.addTransaction(amount, date, category);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/expense")
    public ResponseEntity<?> addExpense(@RequestBody JsonNode incomeData) {
        try {
            double amount = incomeData.get("amount").asDouble();
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(incomeData.get("date").asText());
            ExpenseCategory category = ExpenseCategory.valueOf(incomeData.get("category").asText());

            FinancialManager.addTransaction(amount, date, category);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}