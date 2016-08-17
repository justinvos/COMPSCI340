import os
import json
import stat, os
import time

pid = 7508
fifo_write = open("/tmp/" + str(pid), "w")

def give(pid, label, *values):
    fifo_write.write(json.dumps({"label": label, "sender": os.getpid(), "values": list(values)}) + "\n")


try:
    if stat.S_ISFIFO(os.stat("/tmp/" + str(pid)).st_mode): # checks if pipe is open
        for i in range(20):
            give(pid, "data", str(i+1))
        give(pid, "stop")
    else:
        print("Error: Not a pipe")
except FileNotFoundError:
    print("Error: Pipe closed")
