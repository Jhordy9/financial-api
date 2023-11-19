package com.poo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.poo.financial.model.Income;
import com.poo.financial.model.IncomeCategory;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class IncomeTest {

    @TempDir
    Path tempDir;

    @Test
    public void testIncomeSavesToFile() throws IOException {
        double amount = 100.0;
        Date date = new Date();
        IncomeCategory category = IncomeCategory.SALARIO;
        String filePath = tempDir.resolve("data.csv").toString();

        new Income(amount, date, category, filePath);

        File csvFile = tempDir.resolve("data.csv").toFile();
        assertTrue(csvFile.exists(), "File should exist");

        List<String> lines = Files.readAllLines(csvFile.toPath());
        assertFalse(lines.isEmpty(), "File should have content");

        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(";");
        assertEquals(5, parts.length, "The data line should have five parts");
        assertEquals("RECEITA", parts[1], "The second part should be RECEITA");
        assertEquals(category.toString(), parts[2], "The third part should be the category");
    }

    @Test
    public void testIncomeConstructorWithId() {
        double expectedAmount = 100.0;
        Date expectedDate = new Date();
        String expectedId = "123";
        IncomeCategory expectedCategory = IncomeCategory.SALARIO;

        Income income = new Income(expectedAmount, expectedDate, expectedId, expectedCategory);

        assertEquals(expectedAmount, income.getAmount(), "Amount should match the expected value");
        assertEquals(expectedDate, income.getDate(), "Date should match the expected value");
        assertEquals(expectedId, income.getUuid().toString(), "ID should match the expected value");
        assertEquals(expectedCategory, income.getCategory(), "Category should match the expected value");
        assertEquals("RECEITA", income.getType(), "Type should be RECEITA");
    }
}
