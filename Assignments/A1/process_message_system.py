import atexit
import json
import os
import stat
import sys
import time

class Message:
    def __init__(self, label, action=None, guard=None):
        self.label = label
        self.action = action
        self.guard = guard

def getpath(pid):
    return "/tmp/" + str(pid)

class MessageProc:

    def getpath(self):
        return getpath(os.getpid())

    def start(self, *args):
        pid = os.fork()
        if pid == 0:
            self.main()
        else:
            return pid

    def delpipe(self):
        if self.path == self.getpath():
            #print(str(os.getpid()) + ": Deleting pipe " + self.path)
            self.fifo_read.close()
            os.unlink(self.path)
            #print("Pipe " + self.path + " cleaned")

    def main(self):
        self.path = self.getpath()
        atexit.register(self.delpipe)

        os.mkfifo(self.path)
        self.fifo_read = open(self.path, "r")

    def give(self, pid, label, *values):
        dst_path = getpath(pid)

        if fifo_write == None:
            fifo_write = open(dst_path, "w")
        elif fifo_write.name != "dst_path":
            fifo_write.close()
            fifo_write = open(dst_path, "w")

        while True:
            try:
                if stat.S_ISFIFO(os.stat(dst_path).st_mode): # checks if pipe is open
                    fifo_write.write(json.dumps({"label": label, "values": list(values)}) + "\n")
                    time.sleep(0.0001)
                    break
                else:
                    print("Error: Pipe closed")
            except FileNotFoundError:
                pass



    def receive(self, *messages):

        line = self.fifo_read.readline()

        try:
            contents = json.loads(line)

            for message in messages:
                if message.label == contents["label"]:
                    if len(contents["values"]) == 0:
                        message.action()
                    elif len(contents["values"]) == 1:
                        message.action(contents["values"][0])
                    else:
                        message.action(contents["values"])

        except json.decoder.JSONDecodeError:
            print("="+ line + "=")
