import atexit, json, multiprocessing as mp, os, sys, time, threading

def get_path(pid):
    return "/tmp/" + str(pid)

class ReadStreamer(threading.Thread):

    def __init__(self, log, log_lock, default_timeout=5):
        super().__init__()
        self.log = log
        self.log_lock = log_lock
        self.default_timeout = default_timeout
        self.readstream = None


    def run(self):
        path = self.get_path()
        print("Pipe:" + path)


        self.readstream = open(path, "r")
        print("a")

        print(str(os.getpid()) + ":Pipe connected")

        self.counter = time.time() + 5

        while True:
            line = self.readstream.readline()
            if line != "":
                try:
                    contents = json.loads(line)
                except json.decoder.JSONDecodeError:
                    print(line)
                with self.log_lock:
                    self.log += [contents]
                    print("LOG: " + str(len(self.log)))
                self.counter = time.time() + 5
            elif time.time() > self.counter:
                break


        print("done")
        self.readstream.close()
        with self.log_lock:
            self.log_lock.notify_all()

    def get_path(self):
        return get_path(os.getpid())



class MessageProc:

    def start(self):
        p = mp.Process(target=self.rec_main, args=())
        p.start()
        time.sleep(2)
        return p.pid

    def main(self):
        print(str(os.getpid()) + ":main")


    def rec_main(self):
        print(str(os.getpid()) + ":rec main")
        self.log = []
        self.log_lock = threading.Condition()

        os.mkfifo(self.get_path())
        atexit.register(self.close)

        self.readstream = ReadStreamer(self.log, self.log_lock)
        self.readstream.start()

        with self.log_lock:
            self.log_lock.wait()

        self.close()

    def receive(self, *message_types):
        pass

    def give(self, pid, label, *values):
        pass

    def get_path(self):
        return get_path(os.getpid())

    def close(self):
        os.unlink(self.get_path())
        print(str(os.getpid()) + ":Pipe cleaned")

class Consumer(MessageProc):
    pass


me = MessageProc()
me.main()
con = Consumer().start()
