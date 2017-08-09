#!/bin/bash

min_thread=$1
max_thread=$2
queue_size=$3

java -jar ./tcp-server/target/tcp-server.jar ${min_thread} ${max_thread} ${queue_size}

