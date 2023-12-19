package org.NixDB.Storage;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class ComplexMemoryMappedFileHashMap {
    private static final int MAX_KEY_LENGTH = 256;
    private static final int MAX_VALUE_LENGTH = 1024;
    private static final int ENTRY_SIZE = MAX_KEY_LENGTH + MAX_VALUE_LENGTH;

    private FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;
    private Map<String, Integer> hashMap;
    private Map<Integer, Long> offsetMap;
    private int nextAvailableIndex;

    public ComplexMemoryMappedFileHashMap(String filePath) throws IOException {
        File file = new File(filePath);
        boolean fileExists = file.exists();

        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileExists ? fileChannel.size() : 1024);

        hashMap = new HashMap<>();
        offsetMap = new HashMap<>();
        nextAvailableIndex = 0;

        if (fileExists) {
            loadHashMapFromMemory();
        }
    }


    private void loadHashMapFromMemory() {
        while (mappedByteBuffer.position() < mappedByteBuffer.limit()) {
            byte[] keyBytes = new byte[MAX_KEY_LENGTH];
            mappedByteBuffer.get(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8).trim();

            long offset = mappedByteBuffer.getLong();
            if (!key.isEmpty()) {
                hashMap.put(key, nextAvailableIndex);
                offsetMap.put(nextAvailableIndex, offset);
                nextAvailableIndex++;
            }
        }
    }

    public void put(String key, String value) {
        if (key.length() > MAX_KEY_LENGTH || value.length() > MAX_VALUE_LENGTH) {
            throw new IllegalArgumentException("Key or value exceeds maximum length");
        }

        long currentOffset = mappedByteBuffer.position();

        mappedByteBuffer.put(key.getBytes(StandardCharsets.UTF_8));
        mappedByteBuffer.putLong(currentOffset);

        mappedByteBuffer.position((int) currentOffset);
        mappedByteBuffer.put(value.getBytes(StandardCharsets.UTF_8));

        hashMap.put(key, nextAvailableIndex);
        offsetMap.put(nextAvailableIndex, currentOffset);
        nextAvailableIndex++;
    }

    public String get(String key) {
        Integer index = hashMap.get(key);
        if (index != null) {
            long offset = offsetMap.get(index);
            mappedByteBuffer.position((int) offset);
            byte[] valueBytes = new byte[MAX_VALUE_LENGTH];
            mappedByteBuffer.get(valueBytes);
            return new String(valueBytes, StandardCharsets.UTF_8).trim();
        } else {
            return null;
        }
    }

    public void close() throws IOException {
        fileChannel.close();
    }

    public void writeToDisk(String outputFilePath) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
             FileChannel outputChannel = fileOutputStream.getChannel()) {

            // Write
            outputChannel.write(mappedByteBuffer);

            // Close
            fileOutputStream.close();
        }
    }

}
