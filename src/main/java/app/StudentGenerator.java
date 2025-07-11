package app;

import app.model.Student;
import java.util.Random;

public class StudentGenerator {
    static String[] bolumler = {"Bilgisayar", "Elektrik", "Makine", "İnşaat", "Endüstri"};
    static Random rand = new Random();

    public static Student generate(int i) {
        String ogrenciNo = "2025" + String.format("%06d", i);
        String adSoyad = "Ad Soyad" + i;
        String bolum = bolumler[rand.nextInt(bolumler.length)];
        return new Student(ogrenciNo, adSoyad, bolum);
    }
}
