import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

// 3)	Задача с очередью пациентов- реализовать
// чтение из файла, сбор объекта и передачу в список объектов класса Patient (использование Callable и Future). ***
public class Third_task {
    public static List<String> patients1;
    public static DequeWrapper patients2 = new DequeWrapper();
    public static List<Patient> patients2List = new ArrayList<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // первый вариант реализации с классом Dump(Callable,Future) 1 Thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> future = executorService.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return Dump.getDump();
            }
        });

        patients1 = future.get();
        patients1.forEach(s -> System.out.println(s + "\n"));
        executorService.shutdown();
        System.out.println("//----------------------------------// \n");
        ///////////////////////////////////////////////////////
        // второй вариант реализации c классом ReadFileAndAdd и TakeFromDequeToList (Deque,Runnable) 2 Threads
        Thread secondPart = new Thread(new ReadFileAndAdd());
        Thread secondPart2 = new Thread(new TakeFromDequeToList());

        secondPart.start();
        secondPart2.start();

        secondPart.join();
        secondPart2.join(5000);

        patients2List.forEach(System.out::println);
    }
}

class Dump { // класс для работы с файлом
    static String file = "C:\\Users\\Анна\\PROJECTS\\Patients\\src\\dump";

    public static List<String> getDump() {
        try (Stream<String> strings = Files.lines(Paths.get(file))) { // считываем файл в строки
            List<String> rs = strings
                    .map(e -> e
                            .replaceAll(",", "") // убираем знаки
                            .replaceAll("'", "")
                            .replace("(", "")
                            .replace(")", ""))
                    .toList();
            return rs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class ReadFileAndAdd implements Runnable {
    static String file = "C:\\Users\\Анна\\PROJECTS\\Patients\\src\\dump";

    @Override
    public void run() {
        try (Scanner sc = new Scanner(new FileReader(file))) {
            String patient;
            while (sc.hasNextLine()) {
                patient = sc.nextLine();
                Third_task.patients2.add(patient);
            }
        } catch (FileNotFoundException exception) {
            throw new RuntimeException("File not found");
        }
    }
}

class TakeFromDequeToList implements Runnable  {

    @Override
    public void run() {
        while (true) {
            String temp;
            try {
                temp = Third_task.patients2.pop().replaceAll(",", "") // убираем знаки
                        .replaceAll("'", "")
                        .replace("(", "")
                        .replace(")", "");
                Patient newPatient = new Patient(temp);
                Third_task.patients2List.add(newPatient);
            } catch (InterruptedException e) {
                throw new RuntimeException("Empty patient");
            }
        }
    }
}

class DequeWrapper {
    final Deque<String> newPatientsDeque;

    DequeWrapper() {
        this.newPatientsDeque = new ArrayDeque<>();
    }

    public synchronized void add(String str) {
        newPatientsDeque.add(str);
        notify();
    }

    public synchronized String pop() throws InterruptedException {
        while (newPatientsDeque.isEmpty()) {
            wait();
        }
        return newPatientsDeque.pop();
    }

    public synchronized boolean isEmpty() {
        return newPatientsDeque.isEmpty();
    }
}

