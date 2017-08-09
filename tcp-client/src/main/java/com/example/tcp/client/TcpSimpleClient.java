package com.example.tcp.client;


//import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.zip.InflaterInputStream;

public class TcpSimpleClient implements Runnable {

   // private static final Logger logger = Logger.getLogger(TcpSimpleClient.class);
    private int portNumber = 19999;
    private String hostName = "localhost";
    private volatile Socket socketConnectionWithServer=null;
   // private Socket socketConnectionWithServer;


    /**
     * constructor of the TcpSimpleClient
     *
     * @param portNumber int port Number the client want to start comunication with
     * @param hostName   String host Name of the server
     */
    public TcpSimpleClient(int portNumber, String hostName){
        this.hostName = hostName;
    }

    /**
     * The main method the starting point of the client
     * @param args
     */
    public static void main(String ... args){

        for (int i = 0; i < 1; i++) {
           Thread thread= new Thread(new TcpSimpleClient(19999,"localhost"),"thread TCPClient "+i);
           thread.start();
            System.out.println(thread.getName() +" started successfully --");
        }
    }

    /**
     * run() inherited from Runnable
     */
    public void run() {

        try {
            this.handleClientCommunication();
        } catch (IOException e) {
            System.out.println("run () : IOException--");
        }

    }

    /**
     * handleClientCommunication method intends to handle the
     * responsibility of client communication with the TCPServer
     * @throws IOException
     */
    public void handleClientCommunication() throws IOException {

        try {
            // establishes the server and the client connection
             socketConnectionWithServer = this.getServerConnection();

            // pass the user input to the server side.
            this.passClientInputToTheTCPServer(socketConnectionWithServer);
            // get the server input and displays.
            System.out.println( this.getTCPServerOutPut(socketConnectionWithServer));

        } catch (IOException ioExp) {
            //logger.error("handleClientCommunication: IOException ", ioExp);
            System.out.println("handleClientCommunication: IOException "+ ioExp.getMessage());
            throw ioExp;
        }
    }

    /**
     * getServerConnection method establishes the connection with the server
     * over a given port number.
     * @throws IOException
     * @return Socket it returns the socket connection with the server
     */

    private Socket getServerConnection() throws IOException {

        Socket socketConnectionWithServer =null;
        /* Obtain an address object of the server */
        try {
            InetAddress serverAddress = InetAddress.getByName(this.getHostName());

            // establish the socket connection with the server using the
            // InetAddress object localhost/127.0.0.1 and port number which is
            // selected to establish the communication channel.
            socketConnectionWithServer = new Socket(serverAddress, this.getPortNumber());
            //logger.info("Socket has initialized----");
            System.out.println("Socket has initialized----");
            return  socketConnectionWithServer;

        } catch (UnknownHostException e) {
           // logger.error("getServerConnection: UnknownHostException ", e);
            System.out.println("getServerConnection: UnknownHostException: "+ e.getMessage());
            throw e;
        } catch (IOException ioExp) {
            //logger.error("getServerConnection: IOException ", ioExp);
            System.out.println("getServerConnection: IOException: "+ ioExp.getMessage());
            throw ioExp;
        }
    }


    /**
     * readUserInput method reads the input from the standard input
     * and return it to the out put.
     * Method does not check for the correctness of the value been read
     * and think it is a integer number.
     * @return int number that the user entered.
     */
    private int readUserInput() {

        Scanner scanner = new Scanner(System.in);

        /** Read in the user in put from the terminal */
        System.out.println("Enter any number");
        int number = scanner.nextInt();
        scanner.close();
        return number;

    }

    /**
     * passClientInputToTheTCPServer method passes the content
     * to the server from the client.
     * @throws IOException
     */
    private void passClientInputToTheTCPServer(Socket socketConnectionWithServer) throws IOException {

			/*
             * get the output stream to the server i.e. bound to the Print
			 * stream thus any thing the print stream writes is exposed to the
			 * server via the connections output stream
			 */
        PrintStream printstrm = null;
        try {
            OutputStream opstrm =  socketConnectionWithServer.getOutputStream();
            printstrm = new PrintStream(opstrm);
            printstrm.println(readUserInput());

        } catch (IOException ioExp) {
           // logger.error("readUserInput: IOException ", ioExp);
            System.out.println("eadUserInput: IOException: "+ ioExp.getMessage()) ;
            throw ioExp;
        } finally {
            if(printstrm != null) printstrm.close();
        }
    }


    /**
     * getTCPServerOutPut method get the content that passes in to the Client
     * from the server side.
     * @return String the server input to the client.
     * @throws IOException
     */
    private String getTCPServerOutPut(Socket socketConnectionWithServer) throws IOException {

        String message ="";
            /* Instantiate the Scanner instance for reading the incoming socket
			 * streams (from the server side). Whenever the server sends a
			 * processed response the client catches it and do what is necessary
			 * according to the business logic.
			 */
        //Scanner scanner1 = null;
        try {
           // scanner1 =    new Scanner(socketConnectionWithServer.getInputStream());
           // InputStreamReader instrm =  new InputStreamReader(socketConnectionWithServer.getInputStream());
            System.out.println("SERVER_CLIENT SOCKET STTUS isClosed ? :"+socketConnectionWithServer.isClosed());

           /* InputStream inptstrm = socketConnectionWithServer.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inptstrm);
            BufferedReader br = new BufferedReader(inputStreamReader);*/
            //message = br.readLine();
            InputStream inputStream = new InflaterInputStream(socketConnectionWithServer.getInputStream());
            System.out.println(inputStream.read());


        } catch (IOException ioExp) {
           // logger.error("getTCPServerOutPut: IOException ", ioExp);
            System.out.println("getTCPServerOutPut: IOException " + ioExp.getMessage());
            throw ioExp;
        }



        return  message;



    }



    /*.
     * Getters and setters
     */

    public String getHostName(){
        return hostName;
    }

    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    public int getPortNumber(){
        return portNumber;
    }

    public void setPortNumber(int portNumber){
        this.portNumber = portNumber;
    }


}
