// src/main/java/com/example/demo/DemoApplication.java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType; // Import MediaType
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.http.HttpHeaders; // Import HttpHeaders


import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    private static boolean specialMode = false;

    public static void main(String[] args) {
        System.out.println("Application starting with args: " + Arrays.toString(args));
        List<String> argList = Arrays.asList(args);

        if (argList.contains("--special")) {
            specialMode = true;
            System.out.println("Special mode activated!");
        } else {
            System.out.println("Running in default mode.");
        }

        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    class GreetingController {

        // Return HTML content now
        @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
        public ResponseEntity<String> greet() {
            String htmlContent = generateHtmlPage(DemoApplication.specialMode);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML); // Ensure browser interprets as HTML
            return ResponseEntity.ok().headers(headers).body(htmlContent);
        }

        // Helper method to generate the HTML page string
        private String generateHtmlPage(boolean isSpecialMode) {
            String title;
            String message;
            String bodyBackgroundColor;
            String messageColor;
            String borderColor;

            if (isSpecialMode) {
                title = "Special Mode Active!";
                message = "Hello from the ✨ SPECIAL ✨ World!";
                bodyBackgroundColor = "#e0f7fa"; // Light cyan background
                messageColor = "#00796b"; // Teal text
                borderColor = "#004d40"; // Darker teal border
            } else {
                title = "Default Mode";
                message = "Hello from the DEFAULT World!";
                bodyBackgroundColor = "#fff3e0"; // Light orange background
                messageColor = "#ef6c00"; // Orange text
                borderColor = "#e65100"; // Darker orange border
            }

            // Construct the HTML string
            // Using text blocks (requires Java 15+) for better readability
            return """
                   <!DOCTYPE html>
                   <html lang="en">
                   <head>
                       <meta charset="UTF-8">
                       <meta name="viewport" content="width=device-width, initial-scale=1.0">
                       <title>%s</title>
                       <style>
                           body {
                               font-family: sans-serif;
                               background-color: %s;
                               display: flex;
                               justify-content: center;
                               align-items: center;
                               min-height: 100vh;
                               margin: 0;
                           }
                           .container {
                               background-color: #ffffff;
                               padding: 40px;
                               border-radius: 10px;
                               box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                               text-align: center;
                               border: 3px solid %s;
                           }
                           h1 {
                               color: %s;
                               font-size: 2.5em; /* Larger font size */
                           }
                       </style>
                   </head>
                   <body>
                       <div class="container">
                           <h1>%s</h1>
                       </div>
                   </body>
                   </html>
                   """.formatted(title, bodyBackgroundColor, borderColor, messageColor, message);
        }

        // Keep the simple health check endpoint
        @GetMapping("/health")
        public String healthCheck() {
            return "{\"status\": \"UP\"}";
        }
    }
}