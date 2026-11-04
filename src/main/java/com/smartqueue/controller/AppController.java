package com.smartqueue.controller;

import com.smartqueue.model.QueueToken;
import com.smartqueue.service.QueueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {

    private final QueueService queueService;

    public AppController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalTokens", queueService.countAllTokens());
        model.addAttribute("waitingTokens", queueService.countWaitingTokens());
        model.addAttribute("servedTokens", queueService.countServedTokens());
        model.addAttribute("currentToken", queueService.getCurrentToken());
        model.addAttribute("waitingList", queueService.getWaitingTokens());
        return "dashboard";
    }

    @GetMapping("/token")
    public String tokenPage(Model model) {
        model.addAttribute("latestToken", queueService.getCurrentToken());
        return "token";
    }

    @PostMapping("/token")
    public String generateToken(@RequestParam String customerName,
                               @RequestParam String serviceType,
                               RedirectAttributes redirectAttributes) {
        QueueToken token = queueService.generateToken(customerName, serviceType);
        redirectAttributes.addFlashAttribute("successMessage",
            "Token " + token.getTokenNumber() + " created successfully for " + token.getCustomerName());
        return "redirect:/token";
    }

    @GetMapping("/queue")
    public String queuePage(Model model) {
        model.addAttribute("waitingTokens", queueService.getWaitingTokens());
        model.addAttribute("currentToken", queueService.getCurrentToken());
        return "queue";
    }

    @PostMapping("/queue/serve")
    public String serveNextToken(RedirectAttributes redirectAttributes) {
        QueueToken servedToken = queueService.serveNextToken();
        if (servedToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No queue token is waiting to be called.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage",
                "Called next token " + servedToken.getTokenNumber() + " for " + servedToken.getCustomerName());
        }
        return "redirect:/queue";
    }

    @GetMapping("/history")
    public String historyPage(Model model) {
        model.addAttribute("tokenHistory", queueService.getHistory());
        return "history";
    }
}
