package com.poo.financial.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.financial.model.ExpenseCategory;
import com.poo.financial.model.IncomeCategory;
import com.poo.financial.model.Transaction;
import com.poo.financial.service.FinancialManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api/transactions")
public class FinancialController {
    @GetMapping
    public ResponseEntity<ArrayList<Transaction>> getTransactions() {
        ArrayList<Transaction> transactions;
        try {
            transactions = FinancialManager.getTransactions();
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        return new ResponseEntity<>(new ArrayList<>(transactions), HttpStatus.OK);
    }

    @PostMapping("/api/income")
    public void addIncome(double amount, Date date, IncomeCategory category) {
        FinancialManager.addTransaction(amount, date, category);
    }

    @PostMapping("/api/expense")
    public void addExpense(double amount, Date date, ExpenseCategory category) {
        FinancialManager.addTransaction(amount, date, category);
    }
}