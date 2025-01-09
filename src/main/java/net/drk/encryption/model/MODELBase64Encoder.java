package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MODELBase64Encoder {

    private static final String SECRET_KEY = "&J8fPz4qW#7gLh9vTs!2yRq1kQeX5nNc"; // Must be 16 bytes for AES
    private int filesEncoded = 0;
    private long totalLettersWritten = 0;
    private Map<String, Integer> fileTypeCount = new HashMap<>();
    private int maxDepth = 0;
    private int failedEncodes = 0;

    public void encodeDirectory(String inputDirPath, String outputDirPath) {
        File inputDir = new File(inputDirPath);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.out.println("Input directory does not exist: " + inputDirPath);
            return;
        }

        File outputDir = new File(outputDirPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        processFiles(inputDir, outputDir, 0);
        printStatistics("Encoding");
    }

    private void processFiles(File inputDir, File outputDir, int depth) {
        if (depth > maxDepth) maxDepth = depth;

        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File outputFile = new File(outputDir, getOutputFileName(file));
                if (file.isDirectory()) {
                    processFiles(file, outputFile, depth + 1);
                } else if (file.isFile()) {
                    boolean success = encodeFileToBase64(file, outputFile);
                    if (success) {
                        filesEncoded++;
                        updateFileTypeCount(file);
                        if (file.delete()) {
                            System.out.println("Deleted original file: " + file.getName());
                        } else {
                            System.out.println("Failed to delete original file: " + file.getName());
                        }
                    } else {
                        failedEncodes++;
                    }
                }
            }
        }
    }

    private boolean encodeFileToBase64(File file, File outputFile) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] encryptedBytes = encrypt(fileBytes, SECRET_KEY);
            String base64String = Base64.getEncoder().encodeToString(encryptedBytes);

            outputFile.getParentFile().mkdirs();
            Files.write(outputFile.toPath(), base64String.getBytes());
            totalLettersWritten += base64String.length();
            System.out.println("Encoded Base64 string saved to: " + outputFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getOutputFileName(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".png")) {
            return fileName.substring(0, fileName.lastIndexOf('.')) + ".txt";
        } else if (fileName.endsWith(".json")) {
            return fileName;
        } else {
            return fileName;
        }
    }

    private void updateFileTypeCount(File file) {
        String extension = getFileExtension(file.getName());
        fileTypeCount.put(extension, fileTypeCount.getOrDefault(extension, 0) + 1);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex >= 0 ? fileName.substring(dotIndex + 1) : "";
    }

    private void printStatistics(String operation) {
        System.out.println("\n" + operation + " Statistics:");
        System.out.println("Files processed: " + filesEncoded);
        System.out.println("Total letters written: " + totalLettersWritten);
        System.out.println("Max directory depth: " + maxDepth);
        System.out.println("File types processed: " + fileTypeCount);
        System.out.println("Failed operations: " + failedEncodes);
    }

    private byte[] encrypt(byte[] data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}
