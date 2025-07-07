package org.gig.myplayrightapp.util;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.dto.InsertPlayerDTO;
import org.gig.myplayrightapp.service.PlayerService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RegistrationDataUtils {

    private static final AtomicInteger emailCounter = new AtomicInteger(1);
    private static final String EMAIL_PREFIX = "testme";
    private static final String EMAIL_DOMAIN = "@gmail.com";
    private static final char[] DNI_LETTERS = {
            'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'
    };
    private static final Random random = new Random();
    private static final String LOG_FILE = "generated-users.csv";

    public static String generateNextEmail() {
        int count = emailCounter.getAndIncrement();
        return EMAIL_PREFIX + String.format("%03d", count) + EMAIL_DOMAIN;
    }

    public static String generateRandomDNI() {
        int number = random.nextInt(100_000_000);
        int letterIndex = number % 23;
        return String.format("%08d", number) + DNI_LETTERS[letterIndex];
    }

    public static String generateUniqueUsername() {
        String prefix = "user";
        String suffix = String.valueOf(System.currentTimeMillis());

        // Trim the suffix to fit within 16 characters total
        int maxLength = 16 - prefix.length();
        if (suffix.length() > maxLength) {
            suffix = suffix.substring(suffix.length() - maxLength); // use last digits
        }

        return prefix + suffix;
    }

    public static String generateRandomPhoneNumber() {
        // Spanish mobile numbers start with 6 or 7 and have 9 digits total
        int firstDigit = random.nextBoolean() ? 6 : 7;
        int remainingDigits = 100_000_000 + random.nextInt(900_000_000);
        String phone = String.valueOf(remainingDigits);
        phone = firstDigit + phone.substring(1);
        return phone;
    }


    public static void logGeneratedUser(String email, String dni, String username, String screenshotPath) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.printf("%s,%s,%s,%s%n", email, dni, username, screenshotPath);
        } catch (IOException e) {
            log.error("❌ Failed to write user data to {}: {}", LOG_FILE, e.getMessage());
        }
    }

    public static InsertPlayerDTO generateUniqueInsertPlayerDTO(PlayerService playerService) {
        InsertPlayerDTO dto;

        do {
            String email = generateNextEmail();
            String dni = generateRandomDNI();
            String userName = generateUniqueUsername();
            String phoneNumber = generateRandomPhoneNumber();

            dto = InsertPlayerDTO.builder()
                    .firstName("test")
                    .middleName("testtest")
                    .lastName("testesttest")
                    .gender(1)
                    .birthDate(LocalDate.of(1990, 1, 21))
                    .nationalId(dni)
                    .email(email)
                    .phone(phoneNumber)
                    .address("carrer copernic 80")
                    .state(277)
                    .taxState(12)
                    .city("ABELEDA")
                    .zipCode("27513")
                    .alias(userName)
                    .password("Password1")
                    .securityQuestion("Lugar de nacimiento de tu padre")
                    .securityResponse("barcelona")
                    .build();

            log.info("🔍 Checking uniqueness: alias={}, email={}, phone={}, dni={}",
                    dto.alias(), dto.email(), dto.phone(), dto.nationalId());

        } while (playerService.existsAnyMatching(dto.alias(), dto.phone(), dto.nationalId(), dto.email()));

        log.info("✅ Unique player to be inserted: {}", dto.email());

        return dto;
    }

}
