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
import com.poo.financial.model.Transaction;
import com.poo.financial.service.FinancialManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api/transactions")
public class FinancialController {
    @GetMapping
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

    @PostMapping("api/income")
    public ResponseEntity<?> addIncome(@RequestBody double amount, Date date, ExpenseCategory category) {
        try {
            FinancialManager.addTransaction(
                    amount,
                    date,
                    category);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("api/expense")
    public ResponseEntity<?> addExpense(@RequestBody double amount, Date date, ExpenseCategory category) {
        try {
            FinancialManager.addTransaction(
                    amount,
                    date,
                    category);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}