import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// 4)	Есть объект, в котором 2 счетчика- 1 типа long, 2- integer (оба на старте 0).
//Задача: любыми средствами реализовать инкремент каждой величины в 3 потока по 1000 раз.
// В результате должен получиться объект с 3000 в каждом из полей.
public class Fourth_task {

    public static AtomicInteger Ainteger = new AtomicInteger(0);
    public static AtomicLong Along = new AtomicLong(0);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        Ainteger.getAndIncrement();
                        Along.getAndIncrement();
                    }
                }
            }.start();
        }
        Thread.sleep(1000);
        System.out.println("Integer: " + Ainteger);
        System.out.println("Long: " + Along);
    }
}
