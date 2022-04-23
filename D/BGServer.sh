#!/bin/bash
## $1 : input file (input.in)

java -Djava.rmi.server.hostname=10.20.221.230 BGServer < stdin0
