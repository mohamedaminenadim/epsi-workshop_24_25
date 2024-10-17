package com.epsi.workshop.fig.service;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostsFileService {

    private static final String HOSTS_FILE_PATH = "C:\\Windows\\System32\\drivers\\etc\\hosts"; // For Windows
    // For Linux/Mac, you would use "/etc/hosts"

    private static final String BACKUP_FILE_PATH = "C:\\Windows\\System32\\drivers\\etc\\hosts.bak"; // Backup path
    // For Linux/Mac, you might use "/etc/hosts.bak"

    // IP address to block domains
    private static final String BLOCKED_IP = "127.0.0.1";

    public void blockDomain(String domain) throws IOException {
        createBackup();  // Create a backup before modifying the hosts file

        List<String> lines = Files.readAllLines(Paths.get(HOSTS_FILE_PATH));

        // Check if the domain is already blocked
        if (!lines.stream().anyMatch(line -> line.contains(domain))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOSTS_FILE_PATH, true))) {
                writer.write(BLOCKED_IP + " " + domain);
                writer.newLine();
            }
            flushDnsCache();
        }
    }

    public void unblockDomain(String domain) throws IOException {
        createBackup();  // Create a backup before modifying the hosts file

        List<String> lines = Files.readAllLines(Paths.get(HOSTS_FILE_PATH));

        // Remove the blocked domain
        List<String> updatedLines = lines.stream()
                .filter(line -> !line.contains(domain))
                .collect(Collectors.toList());

        // Write the updated lines back to the hosts file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOSTS_FILE_PATH))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        }
        flushDnsCache();
    }

    private void createBackup() throws IOException {
        // Check if the backup file already exists; if not, create a backup
        Path backupPath = Paths.get(BACKUP_FILE_PATH);
        if (!Files.exists(backupPath)) {
            Files.copy(Paths.get(HOSTS_FILE_PATH), backupPath);
        }
    }

    private void flushDnsCache() {
        // This command works for Windows. Adjust it for Linux/Mac as needed.
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "ipconfig /flushdns");
            pb.inheritIO();
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
