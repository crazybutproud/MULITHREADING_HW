import java.util.concurrent.Semaphore;

// 2)12 грузовиков подъезжают к контрольному пункту для проверки веса автомобиля.
//Всего въездов, контрольных весов и выездов по 5 штук.
// (обратить внимание на то, что лочиться весы и семафор, который отвечает за въезд/выезд могут по-разному)
//Предлагается создать приложение, эмулирующее проведение взвешивания.
public class Second_task {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5); // создаем семафор с 5 разрешаниями на въезд
        for (int i = 1; i < 13; i++) { // цикл для создания 12 объектов класса грузовик
            new Car(semaphore,i).start();
        }
    }
}
class Car extends Thread {
    Semaphore semaphore; // ограничитель количества машин
    int id; // номер машины

    Car (Semaphore semaphore, int id) {
        this.semaphore = semaphore;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire(); //Запрашиваем у семафора разрешение на выполнение
            System.out.println("\u001B[32m" + "Грузовик " + id + " въехал на взвешивание");
            sleep(300); // процесс взвешивания
            System.out.println("\u001B[31m" +"  Грузовик " + id + " выехал со взвешивания");
            semaphore.release(); // освобождаем семафор
        } catch (InterruptedException e) {
            throw new RuntimeException("Проблемы с грузовиком " + id);
        }
    }
}
