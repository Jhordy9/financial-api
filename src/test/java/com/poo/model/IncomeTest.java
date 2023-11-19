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
        // Arrange
        double amount = 100.0;
        Date date = new Date();
        IncomeCategory category = IncomeCategory.SALARIO;
        String filePath = tempDir.resolve("data.csv").toString();

        // Act
        new Income(amount, date, category, filePath);

        // Assert
        File csvFile = tempDir.resolve("data.csv").toFile();
        assertTrue(csvFile.exists(), "File should exist");

        List<String> lines = Files.readAllLines(csvFile.toPath());
        assertFalse(lines.isEmpty(), "File should have content");

        String expectedLineStart = String.format("%s;RECEITA;%s;",
                "Expected-UUID",
                category.toString());

        String lastLine = lines.get(lines.size() - 1);
        assertTrue(lastLine.startsWith(expectedLineStart), "Last line should start with the expected values");

    }
}
