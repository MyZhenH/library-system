package com.example.library_system.utils;

public class Sanitizer {

    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }

        // Ta bort skript-taggar och andra potentiellt farliga taggar
        input = input.replaceAll("(?i)<script.*?>.*?</script.*?>", ""); // Tar bort <script>
        input = input.replaceAll("(?i)<iframe.*?>.*?</iframe.*?>", ""); // Tar bort <iframe>
        input = input.replaceAll("(?i)<.*?on[a-z]+\\s*=\\s*['\"].*?['\"].*?>", "<>"); // Tar bort eventhanterare som onclick

        // Ersätt HTML-tecken så att de inte tolkas som HTML
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("&", "&amp;");
        input = input.replaceAll("\"", "&quot;");
        input = input.replaceAll("'", "&#39;");

        return input;
    }


}
