package com.poo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.poo.financial.model.ExpenseCategory;
import com.poo.financial.model.Expense;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class ExpenseTest {

    @TempDir
    Path tempDir;

    @Test
    public void testExpenseSavesToFile() throws IOException {
        double amount = 100.0;
        Date date = new Date();
        ExpenseCategory category = ExpenseCategory.ENTRETENIMENTO;
        String filePath = tempDir.resolve("data.csv").toString();

        new Expense(amount, date, category, filePath);

        File csvFile = tempDir.resolve("data.csv").toFile();
        assertTrue(csvFile.exists(), "File should exist");

        List<String> lines = Files.readAllLines(csvFile.toPath());
        assertFalse(lines.isEmpty(), "File should have content");

        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(";");
        assertEquals(5, parts.length, "The data line should have five parts");
        assertEquals("DESPESA", parts[1], "The second part should be DESPESA");
        assertEquals(category.toString(), parts[2], "The third part should be the category");
    }
}
