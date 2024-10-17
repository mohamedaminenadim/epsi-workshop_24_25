package com.epsi.workshop.fig.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class BlockedService {

    @Autowired
    private HostsFileService hostsFileService;

    // This map stores the domains and the expiration times of the block
    private final Map<String, LocalDateTime> blockedDomains = new HashMap<>();

    /**
     * Blocks the provided list of domains for a specified duration.
     *
     * @param domains  the list of domains to block
     * @param blockTime the block duration in minutes
     */
    public void blockDomains(Set<String> domains, int blockTime) {
        LocalDateTime blockExpiry = LocalDateTime.now().plusMinutes(blockTime);

        domains.forEach((domain)-> {
            try {
                hostsFileService.blockDomain(domain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        for (String domain : domains) {
            blockedDomains.put(domain, blockExpiry);
        }
    }

    /**
     * Blocks the provided list of domains for a specified duration.
     *
     * @param domains  the list of domains to block
     */
    public void unblockDomains(Set<String> domains) {

        domains.forEach((domain)-> {
            try {
                hostsFileService.unblockDomain(domain);
                blockedDomains.remove(domain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    /**
     * Checks if a domain is currently blocked.
     *
     * @param domain the domain to check
     * @return true if the domain is blocked, false otherwise
     */
    public boolean isBlocked(String domain) {
        LocalDateTime expirationTime = blockedDomains.get(domain);

        if (expirationTime == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(expirationTime)) {
            blockedDomains.remove(domain);
            return false;
        }

        return true;
    }

    /**
     * Returns the time left for a blocked domain.
     *
     * @param domain the domain to check
     * @return the remaining time in minutes, or -1 if the domain is not blocked
     */
    public long getTimeRemaining(String domain) {
        LocalDateTime expirationTime = blockedDomains.get(domain);

        if (expirationTime == null) {
            return -1;
        }

        return java.time.Duration.between(LocalDateTime.now(), expirationTime).toMinutes();
    }
}

