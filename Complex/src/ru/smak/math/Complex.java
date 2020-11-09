package ru.smak.math;

import java.lang.StringBuilder;

/**
 * Класс для работы с комплексными числами
 */
public class Complex {

    private double re;
    private double im;

    /**
     * Конструктор по умолчанию для создания нулевого комплекного числа
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Конструктор для создания комплексного числа с заданными действительной и мнимой частями
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Конструктор для создания комплексного числа с ненулевой действительной частью
     */
    public Complex(double re) {
        this(re, 0);
    }

    /**
     * Метод сложения комплексных чисел
     * @param other второе комплексное число
     * @return  новое комплексное число
     */
    public Complex plus(Complex other) {
        return new Complex(
                re + other.re,
                im + other.im
        );
    }

    /**
     * Метод вычитания для комплексных чисел
     * @param other вычитаемое комплексное число
     * @return новое комплексное число, равное разности данного числа и вычитаемого
     */
    public Complex minus(Complex other) {
        return new Complex(
                re - other.re,
                im - other.im
        );
    }

    /**
     * Оператор умножения
     * @param other второе комплексное число для умножения
     * @return комплексное число, представляющее собой произведение двух исходных комплекксных чисел
     */
    public Complex times(Complex other) {
        return new Complex(
                re * other.re - im * other.im,
                re * other.im + im * other.re
        );
    }

    /**
     * Оператор деления комплексных чисел
     * @param other комплексное число - делитель
     * @return частное от деления делимого числа на делитель
     */
    public Complex div(Complex other){
        //Вычисление знаменателя
        double zn = other.re * other.re + other.im * other.im;

        double r = (re * other.re + im * other.im) / zn;
        double i = (im * other.re - re * other.im) / zn;

        return new Complex(r, i);
    }

    /**
     * Вычисление значения 1 / z, для данного комплексного числа z
     * @return результат вычисления числа обратного для данного
     */
    public Complex not() {
        return new Complex(1.0).div(this);
    }

    /**
     * Нахождение числа, сопряженного к данному
     * @return сопряженное комплексное число
     */
    public Complex getConjugate() {
        return new Complex(re, -im);
    }

    /**
     * Вычисление модуля комплексного числа
     */
    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Вычисление квадрата модуля комплексного числа
     */
    public double abs2() {
        return re * re + im * im;
    }

    /**
     * Строковое представление комплексного числа
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        // Если вещественная часть не нулевая,
        // а также если мнимая - нулевая
        if (neq(re, 0) || eq(im, 0))
        // Добавляем вещественную часть в выод
        s.append(re);
        // Если мнимая часть не нулевая
        if (neq(im, 0)) {
            //Добавляем знак (+ или -)
            s.append((im < 0) ? " - " : " + ");
            // Если мнимая часть по модулю не 1, выводим ее в строку
            if (neq(Math.abs(im), 1.0))
                s.append(Math.abs(im));
            // Добавляем букву "i"
            s.append("i");
        }
        return s.toString();
    }

    /**
     * Получение копии комплексного числа
     * @return комплексное число с вещественной и мнимой частью,
     * совпадающей с вещественной и мнимой частями данного числа соответственно
     */
    @Override
    public Complex clone() {
        return new Complex(re, im);
    }

    private static Boolean eq(double d1, double d2){
        return Math.abs(d1 - d2) <
                Math.max(Math.ulp(d1), Math.ulp(d2)) * 2;
    }

    private static Boolean neq(double d1, double d2){
        return !eq(d1, d2);
    }

}
