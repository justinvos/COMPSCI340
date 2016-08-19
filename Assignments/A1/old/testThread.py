import atexit
import json
import os
import threading
import time

def get_path(pid):
    return "/tmp/" + str(pid)

class ReadStreamer(threading.Thread):

    def __init__(self, log, log_lock, default_timeout=5):
        super().__init__()
        self.log = log
        self.log_lock = log_lock
        self.default_timeout = default_timeout

        path = self.get_path()
        print("Pipe:" + path)
        os.mkfifo(path)
        self.readstream = open(path, "r")
        atexit.register(self.close)
        print("Pipe created")

    def run(self):
        while True:
            line = self.readstream.readline()
            if line != "":

                print(line)
                contents = json.loads(line)

                with log_lock:
                    self.log += [contents]

    def get_path(self):
        return get_path(os.getpid())

    def close(self):
        self.readstream.close()
        os.unlink(self.get_path())
        print("Pipe cleaned")

log = []
log_lock = threading.Lock()

rs = ReadStreamer(log, log_lock)
rs.start()

time.sleep(2)
print(log)
