package com.example.backCalcu;

import com.example.paquete.Paquete;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// S porviene de un servidor y va a un cliente claculadora
// C porviene de un cliente calculadora y va a un servidor

public class Proceso extends Thread{
    public int PUERTO_ACTUAL =0;
    int PUERTO_ACTUAL_MIDDLEWARE = 0;

    public void initialize(){
        Thread hilo1 = new Thread(this);
        hilo1.start();
    }

    String resultado;
    public String mns;
    int puertoServidor = 13000;
    int puertoMiddleware = 11020; //a partir de aqui comienzan los puertos que escuchan los servidores

    @Override // para usar polimorfismo
    public void run(){
        while (true){
            try {
                ServerSocket servidor = new ServerSocket(puertoServidor);
                System.out.println("estoy en el puerto"+ puertoServidor);
                PUERTO_ACTUAL_MIDDLEWARE = puertoMiddleware;
                PUERTO_ACTUAL = puertoServidor;

                //puertoServidor++;
                //asignación = false;
                while (true){
                    //recibo
                    System.out.println("espaero mensaje");
                    Socket misocket = servidor.accept();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(misocket.getInputStream());
                    Paquete paqueteRecibido = (Paquete) flujoEntrada.readObject();
                    System.out.println(paqueteRecibido.getMensaje()+" "+ paqueteRecibido.getPuertoEmisor()+ " "+ paqueteRecibido.getIDdireccion());
                    //Pertenece a calculadora o a servidor
                    if (paqueteRecibido.getIDdireccion() == 'S'){
                        System.out.println("Esta información probiene de un servidor");
                    }else if (paqueteRecibido.getIDdireccion() == 'C') {
                        //Calcular
                        calcular(paqueteRecibido.getMensaje());
                        paqueteRecibido.setPuertoEmisor(PUERTO_ACTUAL);
                        paqueteRecibido.setMensaje(resultado);
                        paqueteRecibido.setIDdireccion('S');


                        //ENVIAR RESULTADO
                        Socket SocketMiddleware = new Socket("127.0.0.1", puertoMiddleware);
                        ObjectOutputStream salida =  new ObjectOutputStream(SocketMiddleware.getOutputStream());
                        salida.writeObject(paqueteRecibido);
                        System.out.println(paqueteRecibido.getMensaje()+" "+ paqueteRecibido.getPuertoEmisor()+ " "+ paqueteRecibido.getIDdireccion()+" Envio al puerto "+PUERTO_ACTUAL_MIDDLEWARE);
                        System.out.println("Enviando"+resultado);
                        SocketMiddleware.close();

                    }

                    misocket.close();
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e);
                puertoMiddleware++;
                puertoServidor++;
            }
        }
    }

    public void calcular(String cadena)
    {

        float suma=0f,resta=0f,div=0f,mul=0f;
        float n1=0f,n2=0f;
        char operacion='a';
        int cont=0;
        boolean ban=true;
        while(ban){
            if(cadena.charAt(cont)=='/'|cadena.charAt(cont)=='-'|cadena.charAt(cont)=='+'|cadena.charAt(cont)=='x'){
                operacion=cadena.charAt(cont);
                n1=Float.parseFloat(cadena.substring(0,cont));
                ban=false;

            }else{
                //System.out.println(cadena.substring(cont,cont+1));
                cont=cont+1;
                if(cont==cadena.length()){
                    ban=false;
                }
            }
        }
        n2=Float.parseFloat(cadena.substring(cont+1,cadena.length()));
        //System.out.println("entra");
        //System.out.println("n1: "+n1+" n2:"+n2);
        //System.out.println(operacion);
        switch(operacion) {
            case '+':
                suma=n1+n2;
                System.out.println("Suma "+suma);
                resultado = suma+"";
                break;
            case '-':
                resta=n1-n2;
                System.out.println("Resta "+resta);
                resultado = resta+"";
                break;
            case 'x':
                mul=n1*n2;
                System.out.println("Multiplicacion "+mul);
                resultado = mul+"";
                break;
            case '/':
                div=n1/n2;
                System.out.println("Division "+div);
                resultado = div+"";
                break;
            default:
                System.out.println("Habitación 5");
                break;
        }
    }
}
