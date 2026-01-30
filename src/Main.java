import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;

public class Main {
    static ArrayList<Movie> movies = new ArrayList<>();
    static final String FILE_NAME = "movies.json";
    static ArrayList<User> users = new ArrayList<>();
    static final String USERS_FILE = "users.json";
    static Scanner scanner = new Scanner(System.in);

    static Movie IndexVerification(int index) {

        if (index >= 0 && index < movies.size()) {

            return movies.get(index);

        }

        System.out.println("Грешка: Филм с такъв номер не съществува!");

        return null;

    }

    public static void main(String[] args) {
        loadFromJson();
        loadJson_Users();

        while (true) {
            System.out.println("\n==== Начална страница ====");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("3. Изход");
            System.out.print("Избери: ");

            int choice_1 = scanner.nextInt();
            scanner.nextLine();

            switch (choice_1) {
                case 1:
                    register();


                    break;
                case 2:

                    if (login()) {
                        Menu_movies();
                    }

                    break;
                case 3:
                    saveJson_Users();
                    System.out.println("Данните са запазени. Довиждане!");
                    return;
                default:
                    System.out.println("Грешен избор!");
            }
        }
    }
    static void Menu_movies() {
        while (true) {
            System.out.println("\n==== Movie App ====");
            System.out.println("1. Добави филм");
            System.out.println("2. Напиши ревю или рейтинг");
            System.out.println("3. Покажи всички филми");
            System.out.println("4. Изход ");
            System.out.print("Избери: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addMovie();
                    break;

                case 2:
                    review_rate_Movie();
                    break;

                case 3:
                    showMovies();
                    break;

                case 4:
                    return;
                default:
                    System.out.println("Грешен избор!");
            }
        }
    }
    static void register () {
        System.out.print("Вашето име и фамилия: ");
        String name = scanner.nextLine();

        System.out.print("Личен номер: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Имейл: ");
        String email = scanner.nextLine();

        System.out.print("Парола: ");
        String password = scanner.nextLine();

        User newUser = new User(name, number, email, password);
        users.add(newUser);
        saveJson_Users();

        System.out.println("Регистрацията е успешна!");


    }

    static boolean login () {

        System.out.print("Вашето име и фамилия: ");
        String name = scanner.nextLine();

        System.out.print("Парола: ");
        String password = scanner.nextLine();

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.name.equals(name) && u.password.equals(password)) {

                System.out.println("Успешен вход!");

                return true;
            }
        }

        System.out.println("Грешно име или парола!");
        return false;
    }

      ;


    //  Добавяне на филм
    static void addMovie () {
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

    //  Добавяне на рейтинг и ревю
    static void review_rate_Movie () {
        if (movies.isEmpty()) {
            System.out.println("Няма филми.");
            return;
        }

        showMovieTitles();

        System.out.print("Избери номер на филм: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (IndexVerification(index) == null)
            return;


        System.out.println("\n==== Movie App ====");
        System.out.print("1.Оцени от (0-10) ");
        System.out.println("2.Напиши ревю ");
        System.out.println("3.И двете ");
        System.out.print("4.Връщане към менюто ");
        System.out.print("Избери: ");


        int choice = scanner.nextInt();
        scanner.nextLine();
        double rating = 0;
        String reviewText = "";


        switch (choice) {
            case 1:
                System.out.println("Оцени от (0-10): ");
                rating = scanner.nextDouble();
                if (rating < 0 || rating > 10) {
                    System.out.println("Невалиден рейтинг.");
                    return;
                }
                scanner.nextLine();
                break;
            case 2:
                System.out.println("Напиши ревю: ");
                reviewText = scanner.nextLine();
                break;
            case 3:
                System.out.println("Оцени от  (0-10): ");
                rating = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Напиши ревю: ");
                reviewText = scanner.nextLine();
                break;
            case 4:
                return;
            default:
                System.out.println("Невалиден избор.");
                return;

        }
        Review newReview = new Review(rating, reviewText);
        movies.get(index).reviews.add(newReview);
        saveToJson();
    }

    // Показване на всички филми
    static void showMovies () {

        if (movies.isEmpty()) {
            System.out.println("Няма филми.");
            return;
        }

        int pageSize = 10;
        int currentPage = 1;

        while (true) {

            List<Movie> page = paginateList(movies, currentPage, pageSize);

            if (page.isEmpty()) {
                System.out.println("Няма повече страници.");
                return;
            }

            System.out.println("\n Страница " + currentPage);

            for (int i = 0; i < page.size(); i++) {
                System.out.println(page.get(i));
            }

            System.out.println("\n n Следваща | p Предишна | q Изход ");
            System.out.print("Избор: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("n")) {
                currentPage++;
            } else if (input.equals("p")) {
                if (currentPage > 1) {
                    currentPage--;
                } else {
                    System.out.println("Това е първата страница.");
                }
            } else if (input.equals("q")) {
                return;
            } else {
                System.out.println("Невалиден избор.");
            }

        }

    }
    // Pagination
    public static <T > List < T > paginateList(List < T > fullList, int pageNumber, int pageSize){
        int offset = (pageNumber - 1) * pageSize;
        int limit_offset = Math.min(offset + pageSize, fullList.size());

        if (offset >= fullList.size() || offset < 0) {
            return Collections.emptyList();
        }

        return fullList.subList(offset, limit_offset);
    }


    static void showMovieTitles () {

        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).metadata.title);
        }
    }


    // JSON Save
    static void saveToJson () {
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
    static void loadFromJson () {
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

        double rating;
        String review;

        Review(double rating, String review) {

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
                sb.append("Ревюта:");
                for (Review r : reviews) {
                    sb.append("\n  - Оценка: " + r.rating + " | Коментар: " + r.review);
                }
            }
            return sb.toString() + "\n";
        }


    }
    // Save Json - Users
    static void saveJson_Users() {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Грешка при запис!");

        }
    }
    //Load From Json -Users

    static void loadJson_Users () {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            users = gson.fromJson(reader, listType);
            if (users == null) users = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Грешка при зареждане.");
        }
    }


    //клас за User
    static class User {
        String name;
        int number;
        String email;
        String password;

        User(String name, int number, String email, String password) {
            this.name = name;
            this.number = number;
            this.email = email;
            this.password = password;
        }
    }


}