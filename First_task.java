// Создать класс, в котором будет три метода:
//void first() {sout(“first”)}, по аналогии second и third.
//Запустить 3 потока, которые по очереди вызывали бы соответствующие своему порядку инициализации методы.
//Работу организовать так, чтобы в консоли был вывод:
//Third
//Second
//First
// !Вариант решения с приоритетами не всегда отрабатывает верно!
public class First_task { // класс с 3 методами

    public void first () {
        System.out.println("FIRST THREAD");
    }
    public void second () {
        System.out.println("SECOND THREAD");
    }
    public void third () {
        System.out.println("THIRD THREAD");
    }
}
class Threads1 { // класс с потоками
    public static void main(String[] args) throws InterruptedException {
        First_task methods_class = new First_task(); // создаем экземпляр класса, чтобы вызывать методы

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                methods_class.first();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                methods_class.second();
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                methods_class.third();
            }
        });
        //задаем потокам приоритет, чтобы в любом случае(почти) они выводились в нужном порядке
        thread3.setPriority(10);
        thread2.setPriority(5);
        thread1.setPriority(1);

        // вызываем потоки в неправильном порядке
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
