package com.epsi.workshop.fig.controller;

import com.epsi.workshop.fig.service.BlockedService;
import com.epsi.workshop.fig.service.HostsFileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class WebsiteBlockController {

    @Autowired
    private BlockedService blockedService; // Assuming this is for session management or similar

    @Autowired
    private HostsFileService hostsFileService; // New service for managing the hosts file

    @PostMapping("/block")
    public String blockSites(HttpSession session, @RequestParam String domain, @RequestParam int blockDuration) {
        System.out.println("Block duration: " + blockDuration);
        Set<String> domains = Set.of(domain);

        long currentTimeMillis = System.currentTimeMillis();
        long blockEndTime = currentTimeMillis + ((long) blockDuration * 60 * 1000);

        session.setAttribute("blockedDomains", domains);
        session.setAttribute("blockEndTime", blockEndTime);

        blockedService.blockDomains(domains, blockDuration);
        return "redirect:/blocked?success=true";
    }

    @PostMapping("/unblock")
    public String unblockSites(HttpSession session, @RequestParam String domain) {
        Set<String> domains = Set.of(domain);
        // Use the HostsFileService to unblock the domain
        blockedService.unblockDomains(domains);
        return "redirect:/blocked?success=true";
    }

    @GetMapping("/blocked")
    public String showBlockedPage(Model model, HttpSession session) {
        Long blockEndTime = (Long) session.getAttribute("blockEndTime");
        long currentTime = System.currentTimeMillis();
        long timeRemaining = blockEndTime - currentTime;

        model.addAttribute("timeRemaining", timeRemaining > 0 ? timeRemaining : 0);
        return "blocked";
    }
}

