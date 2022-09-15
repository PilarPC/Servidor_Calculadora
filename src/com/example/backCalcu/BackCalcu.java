package com.example.backCalcu;

public class BackCalcu extends Thread {

    public static void main (String[] args){
        Proceso hilo1 = new Proceso(); //solo para el extends Thread
        //para arrancar los hilos, simpre se inician despues de instanciar todos los hilos, para que funcionen de forma simultánea
        hilo1.start();

    }
}
