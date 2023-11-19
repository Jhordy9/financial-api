package com.poo.model;

import org.junit.jupiter.api.Test;

import com.poo.financial.model.Transaction;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

public class TransactionTest {

  @Test
  public void testTransactionConstructorWithAmountAndDate() {
    // Arrange
    double expectedAmount = 200.0;
    Date expectedDate = new Date();

    // Act
    Transaction transaction = new Transaction(expectedAmount, expectedDate);

    // Assert
    assertEquals(expectedAmount, transaction.getAmount(), "Amount should match the expected value");
    assertEquals(expectedDate, transaction.getDate(), "Date should match the expected value");
    assertNotNull(transaction.getUuid(), "UUID should not be null");
  }

  @Test
  public void testTransactionConstructorWithAmountDateAndId() {
    // Arrange
    double expectedAmount = 200.0;
    Date expectedDate = new Date();
    String expectedId = UUID.randomUUID().toString();

    // Act
    Transaction transaction = new Transaction(expectedAmount, expectedDate, expectedId);

    // Assert
    assertEquals(expectedAmount, transaction.getAmount(), "Amount should match the expected value");
    assertEquals(expectedDate, transaction.getDate(), "Date should match the expected value");
    assertEquals(expectedId, transaction.getUuid(), "ID should match the expected value");
  }
}
