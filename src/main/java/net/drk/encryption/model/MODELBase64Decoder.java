package net.drk.encryption.model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MODELBase64Decoder {

    private static final String SECRET_KEY = "&J8fPz4qW#7gLh9vTs!2yRq1kQeX5nNc"; // Must be 16 bytes for AES
    private int filesDecoded = 0;
    private int totalBytesWritten = 0;
    private Map<String, Integer> fileTypeCount = new HashMap<>();
    private int maxDepth = 0;
    private int failedDecodes = 0;

    public void decodeDirectory(String inputDirPath, String outputDirPath) {
        File inputDir = new File(inputDirPath);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.out.println("Input directory does not exist: " + inputDirPath);
            return;
        }

        File outputDir = new File(outputDirPath);
        outputDir.mkdirs();

        processFiles(inputDir, outputDir, 0);
        printStatistics("Decoding");
    }

    private void processFiles(File inputDir, File outputDir, int depth) {
        if (depth > maxDepth) maxDepth = depth;

        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File outputFile = new File(outputDir, getDecodedOutputPath(file));
                if (file.isDirectory()) {
                    processFiles(file, outputFile, depth + 1);
                } else if (file.isFile()) {
                    boolean success = decodeBase64ToFile(file, outputFile);
                    if (success) {
                        filesDecoded++;
                        updateFileTypeCount(outputFile);
                        if (file.delete()) {
                            System.out.println("Deleted original Base64 file: " + file.getName());
                        } else {
                            System.out.println("Failed to delete original Base64 file: " + file.getName());
                        }
                    } else {
                        failedDecodes++;
                    }
                }
            }
        }
    }

    private String getDecodedOutputPath(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".txt")) {
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            return baseName.contains("json") ? baseName + ".json" : baseName + ".png";
        }
        return fileName;
    }

    private boolean decodeBase64ToFile(File base64File, File outputFile) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(Files.readAllBytes(base64File.toPath()));
            byte[] decryptedBytes = decrypt(decodedBytes, SECRET_KEY);

            outputFile.getParentFile().mkdirs();
            Files.write(outputFile.toPath(), decryptedBytes);
            totalBytesWritten += decryptedBytes.length;
            System.out.println("Decoded file saved to: " + outputFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        System.out.println("Files processed: " + filesDecoded);
        System.out.println("Total bytes written: " + totalBytesWritten);
        System.out.println("Max directory depth: " + maxDepth);
        System.out.println("File types processed: " + fileTypeCount);
        System.out.println("Failed operations: " + failedDecodes);
    }

    private byte[] decrypt(byte[] data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}
