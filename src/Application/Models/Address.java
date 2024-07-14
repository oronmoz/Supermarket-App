package Application.Models;

import java.io.*;
import java.util.Scanner;

public class Address {
    private int num;
    private String street;
    private String city;

    private static final String ELEMENT_SEP = "#";
    private static final String WORD_SEP = "  ";

    public Address() {}

    public Address(int num, String street, String city) {
        this.num = num;
        this.street = street;
        this.city = city;
    }

    public boolean initAddress() {
        Scanner scanner = new Scanner(System.in);
        String allData;
        String[] elements = new String[0];
        boolean ok;
        String msg = String.format("Enter address data\nFormat: street%shouse number%scity\nstreet and city can have spaces\n",
                ELEMENT_SEP, ELEMENT_SEP);

        do {
            System.out.println(msg);
            allData = scanner.nextLine().trim();
            int countSep = countCharInString(allData, ELEMENT_SEP.charAt(0));
            if (countSep != 2) {
                System.out.println("Incorrect number of separators in address. Please use exactly two '#' separators.");
                ok = false;
                continue;
            }

            elements = allData.split(ELEMENT_SEP);
            ok = checkElements(elements);
            if (!ok) {
                System.out.println("Incorrect address format. Please try again.");
            }
        } while (!ok);

        try {
            this.num = Integer.parseInt(elements[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid house number. Please enter a valid integer.");
            return false;
        }

        this.street = fixAddressParam(elements[0]);
        this.city = fixAddressParam(elements[2]);

        return true;
    }

    public void saveAddressToFile(PrintWriter out) {
        out.println(num);
        out.println(street);
        out.println(city);
    }

    public static Address loadAddressFromFile(BufferedReader in) throws IOException {
        int num = Integer.parseInt(in.readLine());
        String street = in.readLine();
        String city = in.readLine();
        return new Address(num, street, city);
    }

    private String fixAddressParam(String param) {
        String[] wordsArray = param.split("\\s+");
        StringBuilder fixParamStr = new StringBuilder();

        for (int i = 0; i < wordsArray.length; i++) {
            if (i == 0 || i < wordsArray.length - 1) {
                wordsArray[i] = capitalize(wordsArray[i]);
            } else {
                wordsArray[i] = wordsArray[i].toLowerCase();
            }
            fixParamStr.append(wordsArray[i]);
            if (i < wordsArray.length - 1) {
                fixParamStr.append(WORD_SEP);
            }
        }

        return fixParamStr.toString();
    }

    private boolean checkElements(String[] elements) {
        if (elements.length != 3) return false;
        if (!elements[1].matches("\\d+")) return false;
        return !elements[0].trim().isEmpty() && !elements[2].trim().isEmpty();
    }

    private int countCharInString(String str, char ch) {
        return (int) str.chars().filter(c -> c == ch).count();
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public String toString() {
        return String.format("%s %d, %s", street, num, city);
    }
}