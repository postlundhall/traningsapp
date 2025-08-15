package granssnitt;

import register.Rader;
import register.Rad;
import databas.Databaskoppling;
import java.util.Objects;
import java.util.Scanner;

public class TraningsMeny {

    private final Scanner inMatning;

    public int visaMeny() {
        System.out.println("\n------ Meny ------");
        System.out.println("1.\tVisa innehåll");
        System.out.println("2.\tLägg till objekt");
        System.out.println("3.\tTa bort objekt");
        System.out.println("4.\tUppdatera objekt");
        System.out.println("5.\tSpara och avsluta\n");
        System.out.print("Ange siffra som motsvarar menyval: ");
        int anvandarVal = inputInt(inMatning);
        return anvandarVal;
    }

    /**
     * Skriver ut modulerna Träningspass, övning, träningsinstruktion och ber därefter om input för att välja en modul.
     *
     * @return den modul användaren valt, i strängformat.
     */
    public String visaTabeller() {
        System.out.println("--- Ange den modul du vill arbeta med  ---");
        System.out.println("1.\tTräningspass");
        System.out.println("2.\tÖvning");
        System.out.println("3.\tTräningsinstruktion");
        System.out.println("4.\tAvbryt");
        System.out.println("Ange siffra som motsvarar menyval: ");

        String tabell = "";
        int menyVal = inputInt(inMatning);

        switch (menyVal) {
            case 1:
                tabell = "traningspass";
                break;
            case 2:
                tabell = "ovning";
                break;
            case 3:
                tabell = "traningsinstruktion";
                break;
            case 4:
                break;
            default:
                System.out.println("Ogiltigt val. Försök igen.");
                visaTabeller();
        }
        return tabell;
    }

    public static int inputInt(Scanner inMatning) {
        int input = 0;
        boolean correctInputValueType = false;

        while (!correctInputValueType) {
            if (inMatning.hasNextInt()) {
                input = Integer.parseInt(inMatning.nextLine());
                correctInputValueType = true;
            } else {
                System.out.println("Inmatningsfel. Vänligen ange ett heltal, t.ex. 5.");
                inMatning.nextLine(); //Rensa inmatning med felaktig datatyp.
            }
        }
        return input;
    }

    public static String inputString(Scanner inMatning) {
        String input = "";
        boolean correctInputValueType = false;

        while (!correctInputValueType) {
            if (inMatning.hasNext()) {
                input = inMatning.nextLine();
                correctInputValueType = true;
            } else {
                System.out.println("Inmatningsfel. Vänligen ange en textsträng t.ex en övning.");
                inMatning.nextLine(); //Rensa inmatning med felaktig datatyp.
            }
        }
        return input;
    }

    public String visaInnehallTraningspass() {
        String passnamnInput = "";
        Rader rader = Databaskoppling.runSelectQuery("traningspass");
        System.out.println("Ange ett passnamn utifrån de träningspass som listats ovan (Obs: case sensitive). Ange 0 för att avbryta.");

        passnamnInput = inputString(inMatning);
        if (Objects.equals(passnamnInput, "0")) {
            return passnamnInput;
        } else {

            for (Rad r : rader.getArrayList()) {
                if (Objects.equals(r.getAttribut(0), passnamnInput)) {
                    String passnamn = String.format("'%s'", passnamnInput);
                    return passnamn;
                }
            }
            System.out.println(String.format("'%s'", passnamnInput) + " är inte ett giltigt registrerat passnamn. Kontrollera stavning och försök igen.");
            visaInnehallTraningspass();
        }
        return passnamnInput;
    }

    public String visaInnehallTraningsinstruktion() {
        Rader rader = Databaskoppling.runSelectQuery("traningsinstruktion");
        System.out.println("Ange id# för träningsinstruktionen som ska behandlas (Obs: case sensitive). Ange 0 för att avbryta.");

        String idInput = inputString(inMatning);
        if (Objects.equals(idInput, "0")) {
            return idInput;
        } else {
            for (Rad r : rader.getArrayList()) {
                if (Objects.equals(r.getAttribut(0), idInput)) {
                    String id = idInput;
                    return id;
                }
            }
            System.out.println(String.format("%s", idInput) + " är inte ett giltigt registrerat id#. Kontrollera stavning och försök igen.");
            visaInnehallTraningsinstruktion();
        }
        return idInput;
    }

    public String visaInnehallOvning() {
        String ovningsnamnInput = "";
        Rader rader = Databaskoppling.runSelectQuery("ovning");
        System.out.println("Ange ett övningsnamn utifrån de övningar som listats ovan (Obs: case sensitive). Ange 0 för att avbryta.");

        ovningsnamnInput = inputString(inMatning);

        if (Objects.equals(ovningsnamnInput, "0")) {
            return ovningsnamnInput;
        } else {
            for (Rad r : rader.getArrayList()) {
                if (Objects.equals(r.getAttribut(0), ovningsnamnInput)) {
                    String ovningsnamn = String.format("'%s'", ovningsnamnInput);
                    return ovningsnamn;
                }
            }
            System.out.println(String.format("'%s'", ovningsnamnInput) + " är inte ett giltigt registrerat passnamn. Kontrollera stavning och försök igen.");
            visaInnehallTraningspass();
        }
        return ovningsnamnInput;
    }

    public void visaTraningspassAttribut(String primaryKey) {
        System.out.println("--- Ange den attribut du vill uppdatera  ---");
        System.out.println("1.\tSyfte");
        System.out.println("2.\tNivå");
        System.out.println("3.\tPassets längd i minuter");
        System.out.println("4.\tAvbryt");
        System.out.println("Ange siffra som motsvarar menyval: ");

        int menyVal = inputInt(inMatning);

        switch (menyVal) {
            case 1:
                System.out.println("--- Ange nytt syfte för träningspasset ---");
                String syfte = String.format("'%s'", inputString(inMatning));
                Databaskoppling.runUpdateQuery("traningspass", "syfte", "passnamn", primaryKey, syfte);
                visaTraningspassAttribut(primaryKey);
                break;

            case 2:
                System.out.println("--- Ange ny nivå för träningspasset ---");
                String niva = String.format("'%s'", inputString(inMatning));
                Databaskoppling.runUpdateQuery("traningspass", "niva", "passnamn", primaryKey, niva);
                visaTraningspassAttribut(primaryKey);
                break;
            case 3:
                System.out.println("--- Ange ny längd i minuter för träningspasset ---");
                String langd_mins = Integer.toString(greaterThanZeroKontroll(inputInt(inMatning)));
                Databaskoppling.runUpdateQuery("traningspass", "langd_mins", "passnamn", primaryKey, langd_mins);
                visaTraningspassAttribut(primaryKey);
                break;
            case 4:
                break;
            default:
                System.out.println("Ogiltigt val. Försök igen.");
                visaTraningspassAttribut(primaryKey);
        }
    }
    public void visaOvningsAttribut(String primaryKey) {
        System.out.println("--- Ange den attribut du vill uppdatera  ---");
        System.out.println("1.\tUtrustning");
        System.out.println("2.\tPrimär muskelgrupp");
        System.out.println("3.\tAvbryt");
        System.out.println("Ange siffra som motsvarar menyval: ");

        int menyVal = inputInt(inMatning);

        switch (menyVal) {
            case 1:
                System.out.println("--- Ange ny utrustningsbeskrivning för övningen ---");
                String utrustning = String.format("'%s'", inputString(inMatning));
                Databaskoppling.runUpdateQuery("ovning", "utrustning", "ovningsnamn", primaryKey, utrustning);
                visaOvningsAttribut(primaryKey);
                break;
            case 2:
                System.out.println("--- Ange ny primär muskelgrupp för övningen ---");
                String primarMuskelgrupp = String.format("'%s'", inputString(inMatning));
                Databaskoppling.runUpdateQuery("ovning", "primar_muskelgrupp", "ovningsnamn", primaryKey, primarMuskelgrupp);
                visaOvningsAttribut(primaryKey);
                break;
            case 3:
                break;
            default:
                System.out.println("Ogiltigt val. Försök igen.");
                visaTraningspassAttribut(primaryKey);
        }
    }

    public void visaTraningsInstrukionsAttribut(String primaryKey) {
        System.out.println("--- Ange den attribut du vill uppdatera  ---");
        System.out.println("1.\tÖvningsnamn för träningsinstruktionen");
        System.out.println("2.\tAntal set för träningsinstruktionen");
        System.out.println("3.\tRep-range för träningsinstruktionen");
        System.out.println("4.\tPassnamn för träningsinstruktionen");
        System.out.println("5.\tAvbryt");
        System.out.println("Ange siffra som motsvarar menyval: ");
        String primaryKeyKolumn = "instruktions_id";
        int menyVal = inputInt(inMatning);

        switch (menyVal) {
            case 1:
                System.out.println("--- Ange nytt övningsnamn för träningsinstruktionen ---");
                String ovningsnamn = visaInnehallOvning();
                Databaskoppling.runUpdateQuery("traningsinstruktion", "ovningsnamn", "instruktions_id", primaryKey, ovningsnamn);
                visaTraningsInstrukionsAttribut(primaryKey);
                break;

            case 2:
                System.out.println("--- Ange nytt antal set för träningsinstruktionen ---");
                String antalSet = Integer.toString(greaterThanZeroKontroll(inputInt(inMatning)));
                Databaskoppling.runUpdateQuery("traningsinstruktion", "antal_set", primaryKeyKolumn, primaryKey, antalSet);
                visaTraningsInstrukionsAttribut(primaryKey);
                break;
            case 3:
                System.out.println("--- Ange nytt rep-range för träningsinstruktionen ---");
                String repRange = String.format("'%s'", inputString(inMatning));
                Databaskoppling.runUpdateQuery("traningsinstruktion", "rep_range", primaryKeyKolumn, primaryKey, repRange);
                visaTraningsInstrukionsAttribut(primaryKey);
                break;
            case 4:
                System.out.println("--- Ange nytt passnamn för träningsinstruktionen ---");
                String passnamn = visaInnehallTraningspass();
                Databaskoppling.runUpdateQuery("traningsinstruktion", "passnamn", primaryKeyKolumn, primaryKey, passnamn);
                visaTraningsInstrukionsAttribut(primaryKey);
                break;
            default:
                System.out.println("Ogiltigt val. Försök igen.");
                visaTraningspassAttribut(primaryKey);
        }
    }

    public TraningsMeny() {
        inMatning = new Scanner(System.in);
    }

    public int greaterThanZeroKontroll(int inmatning) {
        while (inmatning < 1) {
            System.out.println("Inmatningsfel. Vänligen heltal från 1 och uppåt.");
            inmatning = inputInt(inMatning);
        }
        return inmatning;
    }

    public void visa() {
        String tabell = visaTabeller();
        Databaskoppling.runSelectQuery(tabell);
    }

    public void laggTill() {
        String tabell = visaTabeller();
        String kolumner = "";
        String varden = "";

        switch (tabell) {
            case "traningspass":
                kolumner = "passnamn, syfte, niva, langd_mins";
                System.out.println("--- Ange passets namn, eller 0 för att avbryta. ---");
                String passnamn = inputString(inMatning);
                if (Objects.equals(passnamn, "0")) break;

                System.out.println("--- Ange passets syfte, eller 0 för att avbryta. ---");
                String syfte = inputString(inMatning);
                if (Objects.equals(syfte, "0")) break;

                System.out.println("--- Ange passets nivå, eller 0 för att avbryta. ---");
                String niva = inputString(inMatning);
                if (Objects.equals(niva, "0")) break;

                System.out.println("--- Ange passets längd i minuter, eller ange 0 för att avbryta. ---");
                String langd_mins = Integer.toString(greaterThanZeroKontroll(inputInt(inMatning)));
                if (Objects.equals(langd_mins, "0")) break;
                varden = String.format("'%s', '%s', '%s', %s", passnamn, syfte, niva, langd_mins);
                Databaskoppling.runInsertQuery("traningspass", kolumner, varden);
                break;
            case "traningsinstruktion":
                kolumner = "ovningsnamn, passnamn, antal_set, rep_range";
                String instruktionensOvningsnamn = visaInnehallOvning();
                if (Objects.equals(instruktionensOvningsnamn, "0")) break;

                String instruktionensPassnamn = visaInnehallTraningspass();
                if (Objects.equals(instruktionensPassnamn, "0")) break;

                System.out.println("--- Ange träningsinstruktionens antal set, eller 0 för att avbryta. ---");
                String antalSet = Integer.toString(greaterThanZeroKontroll(inputInt(inMatning)));
                if (Objects.equals(antalSet, "0")) break;

                System.out.println("--- Ange träningsinstruktionens rep-range, eller 0 för att avbryta. ---");
                String repRange = inputString(inMatning);
                if (Objects.equals(repRange, "0")) break;

                varden = String.format("%s, %s, %s, '%s'", instruktionensOvningsnamn, instruktionensPassnamn, antalSet, repRange);
                Databaskoppling.runInsertQuery("traningsinstruktion", kolumner, varden);
                break;
            case "ovning":
                kolumner = "ovningsnamn, utrustning, primar_muskelgrupp";
                System.out.println("--- Ange övningens namn, eller 0 för att avbryta. ---");
                String ovningsnamn = inputString(inMatning);
                if (Objects.equals(ovningsnamn, "0")) break;

                System.out.println("--- Ange den utrustning som ska användas för övningen, eller 0 för att avbryta. ---");
                String utrustning = inputString(inMatning);
                if (Objects.equals(utrustning, "0")) break;

                System.out.println("--- Ange primär muskelgrupp för övningen, eller 0 för att avbryta. ---");
                String primarMuskelgrupp = inputString(inMatning);
                if (Objects.equals(primarMuskelgrupp, "0")) break;

                varden = String.format("'%s', '%s', '%s'", ovningsnamn, utrustning, primarMuskelgrupp);
                Databaskoppling.runInsertQuery("ovning", kolumner, varden);
                break;
            default:
                System.out.println("Något gick fel. Startar om");
                break;
        }
    }
    public void uppdatera() {
        String tabell = visaTabeller();
        String primaryKey = "";

         switch (tabell) {
            case "traningspass":
                primaryKey = visaInnehallTraningspass();
                visaTraningspassAttribut(primaryKey);
                break;
            case "traningsinstruktion":
                primaryKey = visaInnehallTraningsinstruktion();
                visaTraningsInstrukionsAttribut(primaryKey);
                break;
            case "ovning":
                primaryKey = visaInnehallOvning();
                visaOvningsAttribut(primaryKey);
                break;
            default:
                System.out.println("Något gick fel. Försök igen.");
                break;
            }
        }

    public void taBort() {
        System.out.println("!!! Innan du försöker ta bort ett träningspass eller övning, säkerställ också " +
                "att träningsinstruktioner inte finns registrerade till träningspasset eller övningen !!!");

        String tabell = visaTabeller();
        switch (tabell) {
            case "traningspass":
                String passnamn = visaInnehallTraningspass();
                if (Objects.equals(passnamn, "0")) {
                    break;
                } else {
                Databaskoppling.runDeleteQuery(tabell, "passnamn", passnamn);
                break;
                }
            case "traningsinstruktion":
                String instruktionsId = visaInnehallTraningsinstruktion();
                if (Objects.equals(instruktionsId, "0")) {
                    break;
                } else {
                    Databaskoppling.runDeleteQuery(tabell, "instruktions_id", instruktionsId);
                    break;
                }
            case "ovning":
                String ovningsnamn = visaInnehallOvning();
                if (Objects.equals(ovningsnamn, "0")) {
                    break;
                } else {
                    Databaskoppling.runDeleteQuery(tabell, "ovningsnamn", ovningsnamn);
                    break;
                }
            default:
                System.out.println("Något gick fel. Startar om");
                break;
        }
    }
}