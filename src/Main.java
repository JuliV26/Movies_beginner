import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
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
                case 1 : addMovie();
                case 2 :rateMovie();
                case 3 : reviewMovie();
                case 4 : showMovies();
                case 5 : {
                    saveToJson();
                    System.out.println("Данните са запазени. Довиждане!");
                    return;
                }
                default : System.out.println("Грешен избор!");
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
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();


            gson.toJson(movies, writer);
        } catch (IOException e) {
            System.out.println("Грешка при запис!");
        }
    }

    // JSON Load
    static void loadFromJson() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Movie>>() {
            }.getType();
            movies = gson.fromJson(reader, listType);
            if (movies == null) movies = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Грешка при зареждане.");
        }
    }


    // Movie class
    static class Movie {
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
            return title + " (" + year + ")\nЖанр: " + genre +
                    "\nРейтинг: " + (rating == 0 ? "няма" : rating) +
                    "\nРевю: " + (review.isEmpty() ? "няма" : review);
        }
    }

}