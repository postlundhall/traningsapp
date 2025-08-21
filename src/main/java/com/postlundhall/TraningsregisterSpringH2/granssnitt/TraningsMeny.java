package com.postlundhall.TraningsregisterSpringH2.granssnitt;

import org.springframework.stereotype.Component;
import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import com.postlundhall.TraningsregisterSpringH2.service.TraningspassService;

import java.util.Scanner;

@Component
public class TraningsMeny {
    private final TraningspassService service;
    private final Scanner scanner = new Scanner(System.in);

    public TraningsMeny(TraningspassService service) {
        this.service = service;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Meny ---");
            System.out.println("1. Visa träningspass");
            System.out.println("2. Lägg till träningspass");
            System.out.println("3. Ta bort träningspass");
            System.out.println("4. Uppdatera träningspass");
            System.out.println("5. Avsluta");

            int val = readInt("Välj ett alternativ: ");

            switch (val) {
                case 1 -> visa();
                case 2 -> laggTill();
                case 3 -> taBort();
                case 4 -> uppdatera();
                case 5 -> { System.exit(0); }
                default -> System.out.println("Ogiltigt val.");
            }
        }
    }

    private void visa() {
        service.findAll().forEach(System.out::println);
    }

    private void laggTill() {
        String namn = readNonEmpty("Passnamn: ");
        String syfte = readNonEmpty("Syfte: ");
        String niva = readNonEmpty("Nivå: ");
        int langd = readInt("Längd i minuter (1-300): ", 5, 300);

        Traningspass pass = Traningspass.builder()
                .passnamn(namn)
                .syfte(syfte)
                .niva(niva)
                .langdMins(langd)
                .build();

        service.save(pass);
        System.out.println("✅ Träningspass sparat!");
    }

    private void taBort() {
        Long id = readLong("Ange ID för objektet du vill ta bort: ");
        try {
            service.deleteById(id);
            System.out.println("✅ Objekt borttaget!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Inget objekt registrerat till ID " + id + " hittades.");
        }
    }

    private void uppdatera() {
        Long id = readLong("Ange ID för det objekt du vill uppdatera: ");
        service.findById(id).ifPresentOrElse(pass -> {
            String nyttSyfte = readNonEmpty("Nytt syfte: ");
            pass.setSyfte(nyttSyfte);
            service.save(pass);
            System.out.println("✅ Uppdaterat!");
        }, () -> System.out.println("❌ Inget objekt registrerat till ID " + id + " hittades."));
    }

    // --- Input-hjälpare ---
    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Fältet får inte vara tomt!");
        }
    }

    private int readInt(String prompt) {
        return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scanner.nextLine());
                if (val >= min && val <= max) return val;
                System.out.println("Värdet måste vara mellan " + min + " och " + max);
            } catch (NumberFormatException e) {
                System.out.println("Ogiltig inmatning, försök igen med ett heltal (t.ex. 20).");
            }
        }
    }

    private Long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt ID, försök igen med ett heltal (t.ex. 20).");
            }
        }
    }
}