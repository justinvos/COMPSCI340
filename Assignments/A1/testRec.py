import atexit
import json
import os
import sys

path = "/tmp/" + str(os.getpid())

def delpipe():
    os.unlink(path)
    print("Pipe cleaned")

atexit.register(delpipe)



os.mkfifo(path)

print("Pipe:" + path)


fifo_read = open(path, "r")

def receive():
    line = fifo_read.readline()

    if line == "":
        print("EMPTY")
        sys.exit()

    contents = json.loads(line)

    if contents["label"] == "data":
        print(str(contents["sender"]) + ":" + contents["values"][0])
    elif contents["label"] == "stop":
        print("Stopping")
        sys.exit()

while True:
    receive()
