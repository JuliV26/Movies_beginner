import java.io.*;
import java.util.*;

public class Main {

    static ArrayList<Movie> movies = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "movies.json";

    public static void main(String[] args) {

        loadFromJson();

        while (true) {
            System.out.println("\n==== Movie App ====");
            System.out.println("1. Добави филм");
            System.out.println("2. Рейтинг на филм");
            System.out.println("3. Напиши ревю");
            System.out.println("4. Покажи всички филми");
            System.out.println("5. Изход");
            System.out.print("Избери: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addMovie();
                case 2 -> rateMovie();
                case 3 -> reviewMovie();
                case 4 -> showMovies();
                case 5 -> {
                    saveToJson();
                    System.out.println("Данните са запазени. Довиждане!");
                    return;
                }
                default -> System.out.println("Грешен избор!");
            }
        }
    }

    //  Добавяне
    static void addMovie() {
        System.out.print("Име на филма: ");
        String title = scanner.nextLine();

        System.out.print("Жанр: ");
        String genre = scanner.nextLine();

        System.out.print("Година: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        movies.add(new Movie(title, genre, year));
        saveToJson();
        System.out.println("Филмът е добавен!");
    }

    //  Рейтинг
    static void rateMovie() {
        if (movies.isEmpty()) {
            System.out.println("Няма филми.");
            return;
        }

        showMovieTitles();
        System.out.print("Избери номер на филм: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= movies.size()) {
            System.out.println("Невалиден номер.");
            return;
        }

        System.out.print("Оцени от (0-10): ");
        double rating = scanner.nextDouble();
        scanner.nextLine();

        movies.get(index).rating = rating;
        saveToJson();
        System.out.println("Рейтингът е записан!");
    }

    //  Ревю
    static void reviewMovie() {
        if (movies.isEmpty()) {
            System.out.println("Няма филми.");
            return;
        }

        showMovieTitles();
        System.out.print("Избери номер на филм: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= movies.size()) {
            System.out.println("Невалиден номер.");
            return;
        }

        System.out.print("Напиши ревю: ");
        String review = scanner.nextLine();

        movies.get(index).review = review;
        saveToJson();
        System.out.println("Ревюто е записано!");
    }

    // Показване
    static void showMovies() {
        if (movies.isEmpty()) {
            System.out.println("Няма филми.");
            return;
        }

        for (int i = 0; i < movies.size(); i++) {
            System.out.println("\n" + (i + 1) + ". " + movies.get(i));
        }
    }

    static void showMovieTitles() {
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).title);
        }
    }

    // JSON Save
    static void saveToJson() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("[");
            for (int i = 0; i < movies.size(); i++) {
                Movie m = movies.get(i);
                writer.print("  {\"title\":\"" + escape(m.title) +
                        "\",\"genre\":\"" + escape(m.genre) +
                        "\",\"year\":" + m.year +
                        ",\"rating\":" + m.rating +
                        ",\"review\":\"" + escape(m.review) + "\"}");
                if (i < movies.size() - 1) writer.println(",");
            }
            writer.println("\n]");
        } catch (IOException e) {
            System.out.println("Грешка при запис!");
        }
    }

    // JSON Load
    static void loadFromJson() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line, json = "";
            while ((line = br.readLine()) != null) json += line;

            json = json.replace("[", "").replace("]", "");
            if (json.trim().isEmpty()) return;

            String[] objects = json.split("\\},\\{");

            for (String obj : objects) {
                obj = obj.replace("{", "").replace("}", "");

                String[] parts = obj.split(",");
                String title = "", genre = "", review = "";
                int year = 0;
                double rating = 0;

                for (String p : parts) {
                    if (p.contains("\"title\"")) title = p.split(":")[1].replace("\"", "");
                    else if (p.contains("\"genre\"")) genre = p.split(":")[1].replace("\"", "");
                    else if (p.contains("\"year\"")) year = Integer.parseInt(p.split(":")[1]);
                    else if (p.contains("\"rating\"")) rating = Double.parseDouble(p.split(":")[1]);
                    else if (p.contains("\"review\"")) review = p.split(":")[1].replace("\"", "");
                }

                Movie m = new Movie(title, genre, year);
                m.rating = rating;
                m.review = review;
                movies.add(m);
            }

        } catch (Exception e) {
            System.out.println("Грешка при зареждане.");
        }
    }

    static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}

// Movie class
class Movie {
    String title;
    String genre;
    int year;
    double rating;
    String review;

    Movie(String title, String genre, int year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = 0;
        this.review = "";
    }

    public String toString() {
        return  title + " (" + year + ")\nЖанр: " + genre +
                "\nРейтинг: " + (rating == 0 ? "няма" : rating) +
                "\nРевю: " + (review.isEmpty() ? "няма" : review);
    }
}
