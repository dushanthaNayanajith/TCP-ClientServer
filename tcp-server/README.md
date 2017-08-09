# Simple TCP  Server Program

This project contains a simple TCP server which is capable of handling multiple concurrent clients. The maximum number of client serviceable at a given time is decided by "maximumThreads" parameter and the minimum number of threads are decided by "minimumThread" parameter while the queue size is governed by "queueSize" .

When the maximum number of client is reached, and new request are reached, such client request will be queued for a period of time until the queue size is reached.

### Classes involved

TCPServer  - The server class that handles the connection with the client requests and implement the server requests

ServerWorker - This class bears the responsibility of handling actual processing of the work

### Running the Script

  e.g.
  
  sh sever.sh 5 10 15
  
  Initially the server script should be run and secondly the client script should be executed.