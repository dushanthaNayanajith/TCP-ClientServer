package com.example.tcp.server;

//import org.apache.log4j.Logger;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerWorker implements Runnable {

    // public static final Logger logger = Logger.getLogger(ServerWorker.class);
    private Socket clientSocket;


    public ServerWorker(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        PrintStream printStrm = null;
        Scanner scaner=null;

        try{

            /*Instantiate the scanner instance that's bound to the input stream
		     * object which reads the data sent in by the client.
		     */

            scaner = readFromTCPClient(clientSocket.getInputStream());
            //logger.info("--> Reading from the client :  ");
            System.out.println("--> Reading from the client :  ");

            int number = scaner.nextInt(); // the accepted number is doubled.
            int temp = number * 2;
            System.out.println("testing the composed number-->"+ temp);

            /*printStrm = new PrintStream(clientSocket.getOutputStream());
            printStrm.println(temp);*/
            // logger.info("--> writing to the client :  ");
            System.out.println("--> writing to the client :  ");

            OutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.write(temp);


        } catch(IOException ioExp){
            System.out.println("ServerWorker.class : run () : "+ioExp.getMessage());
        } finally{
            /*if(scaner !=null){
                scaner.close();
            }
            if(printStrm != null){
                printStrm.close();
            }*/
            /*try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("ServerWorker.class : run ()  : "+e.getMessage());
            }*/
        }

    }


    /**
     *
     * @param source
     * @return
     */

    private Scanner readFromTCPClient(InputStream source){
        return new Scanner(source);
    }

}

