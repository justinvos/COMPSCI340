import os
import json
import stat, os




def give(pid, label, *values):
    fifo_write = open("/tmp/" + str(pid), "w")
    fifo_write.write(json.dumps({"label": label, "values": list(values)}) + "\n")
    fifo_write.close()

pid = 14054

try:
    if stat.S_ISFIFO(os.stat("/tmp/" + str(pid)).st_mode): # checks if pipe is open
        give(pid, "data", "somthing")
        give(pid, "stop")
    else:
        print("Error: Pipe closed")
except FileNotFoundError:
    print("Error: Pipe closed")
