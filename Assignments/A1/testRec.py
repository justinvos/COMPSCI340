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


def receive():
    fifo_read = open(path, "r")
    line = fifo_read.readline()
    print(line)

    contents = json.loads(line)

    if contents["label"] == "data":
        print(contents["values"][0])
    elif contents["label"] == "stop":
        print("Stopping")
        sys.exit()


while True:
    receive()
