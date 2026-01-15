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
                case 1:
                    addMovie();
                    break;
                case 2:
                    rateMovie();
                    break;
                case 3:
                    reviewMovie();
                    break;
                case 4:
                    showMovies();
                    break;
                case 5: {
                    saveToJson();
                    System.out.println("Данните са запазени. Довиждане!");
                    return;
                }
                default:
                    System.out.println("Грешен избор!");
            }
        }
    }

    //  Добавяне на филм
    static void addMovie() {
        System.out.print("Име на филма: ");
        String title = scanner.nextLine();

        System.out.print("Жанр: ");
        String genre = scanner.nextLine();

        System.out.print("Година: ");
        int year = scanner.nextInt();
        scanner.nextLine();


        Metadata metadata = new Metadata(title, genre, year);


        Movie movie = new Movie(metadata);


        movies.add(movie);

        saveToJson();
        System.out.println("Филмът е добавен!");
    }

    //  Добавяне на рейтинг
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

        System.out.print("Въведи своето потребителско име: ");
        String username = scanner.nextLine();

        System.out.print("Оцени от (0-10): ");
        double rating = scanner.nextDouble();
        scanner.nextLine();


        Review review = new Review(username, rating, "");
        movies.get(index).reviews.add(review);

        saveToJson();
        System.out.println("Рейтингът е записан!");
    }

    //  Добавяне на ревю
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

        System.out.print("Въведи своето потребителско име: ");
        String username = scanner.nextLine();

        System.out.print("Напиши ревю: ");
        String reviewText = scanner.nextLine();



        saveToJson();
        System.out.println("Ревюто е записано!");
    }

    // Показване на всички филми
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
            System.out.println((i + 1) + ". " + movies.get(i).metadata.title);
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
            Type listType = new TypeToken<ArrayList<Movie>>() {}.getType();
            movies = gson.fromJson(reader, listType);
            if (movies == null) movies = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Грешка при зареждане.");
        }
    }

    // Metadata class-данните за филмите
    static class Metadata {
        String title;
        String genre;
        int year;

        Metadata(String title, String genre, int year) {
            this.title = title;
            this.genre = genre;
            this.year = year;
        }
    }

    // Review class
    static class Review {
        String username;
        double rating;
        String review;

        Review(String username, double rating, String review) {
            this.username = username;
            this.rating = rating;
            this.review = review;
        }
    }

    // Movie class
    static class Movie {
        Metadata metadata;
        ArrayList<Review> reviews;

        Movie(Metadata metadata) {
            this.metadata = metadata;
            this.reviews = new ArrayList<>();
        }
  // напомняне- append добавя текст в края на вече съществуващо съдържание
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(metadata.title + " (" + metadata.year + ")\n");
            sb.append("Жанр: " + metadata.genre + "\n");

            if (reviews.isEmpty()) {
                sb.append("Ревюта: няма");
            } else {
                sb.append("Ревюта:\n");
                for (Review r : reviews) {
                    sb.append("- " + r.username + ": " + r.rating + " - " + r.review + "\n");
                }
            }
            return sb.toString();
        }
    }
}
