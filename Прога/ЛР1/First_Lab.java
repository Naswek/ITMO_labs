import java.util.ArrayList; //добавляем библиотеки
import java.util.Arrays;
import java.util.List;


public class First_Lab {
    public static void main(String[] args) {

        //первый массив
        long[] z = new long[16];
        for (int i = 0; i < z.length; i++) {
            z[i] = 3 + i;
        }

        //второй массив
        float[] x = new float[12];
        for (int i = 0; i < x.length; i++) {
            x[i] = ((float) (Math.random() * 24) - 14); //заполняем массив рандомными числами
        }

        double[][] z1 = new double[16][12];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 12; j++) {

                //первая формула и условие
                if (z[i] == 10) {
                    z1[i][j] = Math.pow(((Math.atan(Math.pow(Math.E, ((-1) * (Math.abs(x[j]))))))) / 4, 3);
                }

                //вторая формула и условие
                else if (z[i] == 4 || z[i] == 5 || z[i] == 7 || z[i] == 9 || z[i] == 11 || z[i] == 13 || z[i] == 15 || z[i] == 18) {
                    z1[i][j] = Math.sin(Math.cbrt(Math.asin((x[j] - 2) / 24)));
                }

                //третья формула
                else {
                    z1[i][j] = Math.asin(Math.pow(Math.E, (Math.cbrt((-1)*(Math.acos(Math.pow((x[j] - 2) / 24, 2)))))));
                }
            }
        }
        //вывод массива z1 в виде матрицы с 2 знаками после запятой
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.printf(" [%6.2f] ", z1[i][j]);
            }
            System.out.println();
        }
    }
}
