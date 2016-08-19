import atexit
import json
import os
import stat
import sys
import threading
import time

ANY = "any"

class Message:
    def __init__(self, label, action=lambda: None, guard=lambda: True):
        self.label = label
        self.action = action
        self.guard = guard

class TimeOut:
    def __init__(self, delay, action=lambda: None):
        self.delay = delay
        self.action = action

def get_path(pid):
    return "/tmp/" + str(pid)

class MessageProc:
    def __init__(self):
        self.incoming_messages = []
        self.writestreams = {}
        self.readstream = None
        self.backlog = []

    def get_path(self):
        return get_path(os.getpid())

    def main(self):
        print(str(os.getpid()) + ":MAIN")

    def start(self):
        pid = os.fork()
        if pid == 0:
            print(str(os.getpid()) + " started")
            self.main()
            sys.exit()
        else:
            return pid

    def receive(self, *message_types):
        lock_cond = threading.Condition()
        #count_thread = None
        for message_type in message_types:
            if isinstance(message_type, TimeOut):
                count_thread = TimeOutCounter(message_type, lock_cond)
                count_thread.start()

        if self.readstream == None:
            path = self.get_path()
            print("Pipe:" + path)
            os.mkfifo(path)
            self.readstream = open(path, "r")
            atexit.register(self.close)
            print("Pipe created")

        with lock_cond:
            line = self.readstream.readline()
            lock_cond.notify_all()

        contents = json.loads(line)

        self.backlog += [contents]


        for backlog_msg in self.backlog:
            for message_type in message_types:
                if isinstance(message_type, TimeOut):
                    continue
                elif backlog_msg["label"] == message_type.label or message_type.label == ANY:
                    if message_type.guard():
                        if len(backlog_msg["values"]) == 1:
                            ret_val = message_type.action(backlog_msg["values"][0])
                        else:
                            ret_val = message_type.action()
                        self.backlog.remove(backlog_msg)
                        return ret_val




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

class TimeOutCounter(threading.Thread):
    def __init__(self, timeout, cond):
        super().__init__()
        self.timeout = timeout
        self.cond = cond

    def run(self):
        with cond:
            if cond.wait(self.timeout.delay):
                timeout.action()
