import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TransactionApp {

    private static final TransactionDao DAO = new TransactionDao();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String END = "0";
    private static final String ADD_TRANSACTION = "1";
    private static final String UPDATE_TRANSACTION = "2";
    private static final String DELETE_TRANSACTION = "3";
    private static final String PRINT_EXPENSES = "4";
    private static final String PRINT_REVENUE = "5";

    public static void main(String[] args) {
        String choice = "-1";
        while (!choice.equals(END)) {
            printMenu();
            choice = scanner.nextLine();
            switch (choice) {
                case ADD_TRANSACTION -> addTransation();
                case UPDATE_TRANSACTION -> updateTransation();
                case DELETE_TRANSACTION -> deleteTransation();
                case PRINT_EXPENSES -> printExpenses();
                case PRINT_REVENUE -> printRevenue();
                case END -> {
                    System.out.println("Koniec programu");
                    scanner.close();
                    DAO.close();
                }
                default -> System.out.println("Brak takiej opcji");
            }
        }
    }

    private static void printExpenses() {
        List<Transation> expenses = DAO.getTransations("expenses");
        if (!expenses.isEmpty()) {
            expenses.forEach(System.out::println);
        } else {
            System.out.println("Brak Transakcji o typie - wydatek");
        }
    }

    private static void printRevenue() {
        List<Transation> expenses = DAO.getTransations("revenue");
        if (!expenses.isEmpty()) {
            expenses.forEach(System.out::println);
        } else {
            System.out.println("Brak Transakcji o typie - przychod");
        }
    }

    private static void deleteTransation() {
        System.out.println("Usuwanie transakcji, podaj id");
        int id = scanner.nextInt();
        boolean delete = DAO.delete(id);
        if (delete) {
            System.out.println("Trasakcja o id " + id + " usunieta pomyslnie");
        } else {
            System.out.println("Nastapil blad podczas usuwania transakcji");
        }
    }

    private static void updateTransation() {
        System.out.println("Aktualizacjia transakcji");
        System.out.println("Podaj id transakcji:");
        Integer id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Podaj typ transakcji:");
        String transactionType = scanner.nextLine();
        System.out.println("Podaj opis:");
        String transactionDesc = getTypeOfTransaction();
        System.out.println("Podaj kwote");
        double transactionAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Podaj date: format YYYY-MM-RR");
        String transactionDate = scanner.nextLine();
        boolean update = DAO.update(new Transation(id, transactionType, transactionDesc, transactionAmount, transactionDate));
        if (update) {
            System.out.println("Transakcja pomyslnie zaktualizowana");
        }
    }

    private static void printMenu() {
        System.out.println("Wybierz opcje: ");
        System.out.println(END + ". zakoncz dzialanie programu");
        System.out.println(ADD_TRANSACTION + ". dodawanie transakcji");
        System.out.println(UPDATE_TRANSACTION + ". modyfikacja transakcji");
        System.out.println(DELETE_TRANSACTION + ". usuwanie transakcji");
        System.out.println(PRINT_EXPENSES + ". wyswietlanie wszytskich przychodow");
        System.out.println(PRINT_REVENUE + ". wyswietlanie wydatkow");
    }

    private static void addTransation() {
        System.out.println("Dodawanie transakcji");
        System.out.println("Podaj typ transakcji:");
        String transactionType = getTypeOfTransaction();
        System.out.println("Podaj opis:");
        String transactionDesc = scanner.nextLine();
        System.out.println("Podaj kwote:");
        double transactionAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Podaj date:");
        String transactionDate = scanner.nextLine();
        DAO.add(new Transation(transactionType, transactionDesc, transactionAmount, transactionDate));
    }

    private static String getTypeOfTransaction() {
        String typeOfTransation = "";
        boolean isTypeCorrect = false;
        while (!isTypeCorrect) {
            typeOfTransation = scanner.nextLine();
            if (typeOfTransation.equals("revenue") || typeOfTransation.equals("expenses")) {
                isTypeCorrect = true;
            } else {
                System.out.println("Bledny typ transakcji, wpisz revenue lub expenses");
            }
        }
        return typeOfTransation;
    }
}
