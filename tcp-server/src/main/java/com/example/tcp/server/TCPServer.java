package com.example.tcp.server;

//import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TCPServer class intend to handle multiple client calls at a single predefined
 * port # 19999. This simple class demonstrate how multiple clients interact
 * with a single server.
 *
 * @author dushantha
 */
public class TCPServer implements Runnable {

    private int port =19999;
   // public static final Logger logger = Logger.getLogger(TCPServer.class);
    private ServerSocket serverSocket = null;
    private Thread currentThreadInstance = Thread.currentThread();;
    private boolean isServerStoped = false;
    private ExecutorService threadPoolExecutor = null;

    /**
     *
     * @param minThreads
     * @param maxThreads
     * @param queuesize
     */
    public TCPServer(int minThreads,int maxThreads,int queuesize) {

        this.threadPoolExecutor = new ThreadPoolExecutor(
                minThreads,
                maxThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(queuesize));

    }


    public static void main(String[] args) {

        //validation of the incoming arguments

        int minimumThread =(args.length > 0 && args[0] !=null)?Integer.parseInt(args[0]):5;
        int maximumThreads = (args.length > 0 && args[1] !=null)?Integer.parseInt(args[1]):10;
        int queueSize = (args.length > 0 && args[2] !=null)?Integer.parseInt(args[2]):15;

        TCPServer theTCPServeer = new TCPServer(minimumThread,maximumThreads,queueSize);
        new Thread(theTCPServeer).start();

    }

    /**
     * openTheServerSocket method opens the socket
     * for the client communication on the assigned socket
     */
    private void openTheServerSocket() throws IOException {

		/*
         * the server socket class has been used to set up a new server. The
		 * server socket can be created to listen to a particular port thus
		 * accept and handle incoming sockets.
		 */
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server Started  listning to port" + this.port);
        } catch (IOException ioExp) {
            //logger.error("openTheServerSocket: IOException ", ioExp);
            System.out.println("openTheServerSocket: IOException "+ ioExp.getMessage());
            throw ioExp;
        }
    }


    @Override
    public void run() {

        synchronized (this) {
            this.setCurrentThreadInstance(Thread.currentThread());
            try {
                this.openTheServerSocket();
            } catch (IOException e) {
               // logger.error("run: IOException ", e);
                System.out.println("TCPServer.class :run(): IOException " +e.getMessage());
            }

            Scanner scaner = null;


            while (true ) {
                Socket clientSocket = null;
                try{

                 /*
				 * Accept the incoming connection to this socket, till then the
				 * program floor is suspended,i.e. the method is blocked till
				 * the connection is established. It's achieve via the accept()
				 * of ServerSocket class.
				 */
                    clientSocket = this.serverSocket.accept();

                } catch (IOException e) {
                   // logger.error("run: IOException ", e);
                    System.out.println("TCPServer.class: run(): IOException "+ e.getMessage());
                }

                threadPoolExecutor.execute(new ServerWorker(clientSocket));


            }  // EOF while loop

        }   // EOF synchronized block
    } //EOF run


    /**
     *
     * Getters and setters
     */


    public synchronized ServerSocket getServerSocket(){
        return serverSocket;
    }

    public synchronized void setServerSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public Thread getCurrentThreadInstance(){
        return currentThreadInstance;
    }

    public void setCurrentThreadInstance(Thread currentThreadInstance) {
        this.currentThreadInstance = currentThreadInstance;
    }

    public boolean isServerStopped(){
        return isServerStoped;
    }

    public void setServerStoped(boolean serverStopped){
        isServerStoped = serverStopped;
    }

    public ExecutorService getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public  int getPort(){
        return port;
    }

}




