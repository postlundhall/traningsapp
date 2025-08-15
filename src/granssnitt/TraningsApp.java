package granssnitt;

public class TraningsApp {
    public static void main(String[] args) {
        TraningsMeny meny = new TraningsMeny();

        int menyVal = 0;
        while (menyVal != 6) {
            menyVal = meny.visaMeny();
            switch (menyVal) {
                case 1:
                    meny.visa();
                    break;
                case 2:
                    meny.laggTill();
                    break;
                case 3:
                    meny.taBort();
                    break;
                case 4:
                    meny.uppdatera();
                    break;
                case 5:
                    System.out.println("--- Programmet avslutas. ---");
                    System.out.println("----------------------------");
                    System.exit(0);
                default:
                    System.out.println("Ogiltigt val. Försök igen.");

            }
        }

    }
}