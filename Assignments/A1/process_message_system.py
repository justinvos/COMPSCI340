import atexit
import json
import os
import sys
import time
import stat
class Message:
    def __init__(self, label, action=None, guard=None):
        self.label = label
        self.action = action
        self.guard = guard

def get_path(pid):
    return "/tmp/" + str(pid)

class MessageProc:
    def __init__(self):
        self.incoming_messages = []
        self.writestreams = {}
        self.readstream = None

    def get_path(self):
        return get_path(os.getpid())

    def main(self):
        print(str(os.getpid()) + ":MAIN")

    def start(self):
        pid = os.fork()
        if pid == 0:
            print(str(os.getpid()) + " started")
            self.main()
        else:
            return pid

    def receive(self, *message_types):
        if self.readstream == None:
            path = self.get_path()
            print("Pipe:" + path)
            os.mkfifo(path)
            self.readstream = open(path, "r")
            atexit.register(self.close)
            print("Pipe created")

        line = self.readstream.readline()
        contents = json.loads(line)

        for message_type in message_types:
            if contents["label"] == message_type.label:
                if len(contents["values"]) == 1:
                    message_type.action(contents["values"][0])
                else:
                    message_type.action()

    def give(self, pid, label, *values):
        if str(pid) not in self.writestreams:

            while True:
                try:
                    if stat.S_ISFIFO(os.stat(get_path(pid)).st_mode):
                        break
                except FileNotFoundError:
                    pass

            self.writestreams[str(pid)] = open(get_path(pid), "w")

        self.writestreams[str(pid)].write(json.dumps({"label": label, "sender": os.getpid(), "values": list(values)}) + "\n")

    def close(self):
        self.readstream.close()
        os.unlink(self.get_path())
        print("Pipe cleaned")
