import atexit, json, os, sys, time, threading

ANY = "any"

def get_path(pid):
    return "/tmp/" + str(pid)

class Message:
    def __init__(self, label, action=lambda: None, guard=lambda: True):
        self.label = label
        self.action = action
        self.guard = guard

class MessageProc:
    def __init__(self):
        self.log = []
        self.log_lock = threading.Condition()
        self.pointer = 0
        self.readstream = ReadStreamer(self.log, self.log_lock)

    def get_path(self):
        return get_path(os.getpid())

    def main(self):
        self.readstream.start()
        print(str(os.getpid()) + ":MAIN")

    def start(self):
        pid = os.fork()
        if pid == 0:
            print(str(os.getpid()) + " started")
            self.main()
        else:
            return pid

    def receive(self, *message_types):
        if len(self.log) > self.pointer:
            current_message = self.log[self.pointer]

            for message_type in message_types:
                if message_type.label == ANY:
                    message_type.action()
                elif current_message["label"] == message_type.label:
                    if len(current_message["values"]) == 1:
                        message_type.action(current_message["values"][0])
                    else:
                        message_type.action()

            self.pointer += 1

    def give(self, pid, label, *values):
        param_dict = {"label": label, "sender": os.getpid(), "values": values}

        f = open(get_path(pid), "w")
        f.write(json.dumps(param_dict) + "\n")
        f.close()

    def close(self):
        os.unlink(self.get_path())
        print(str(os.getpid()) + ":Pipe cleaned")

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
        os.mkfifo(path)
        atexit.register(self.close)
        self.readstream = open(path, "r")
        #atexit.register(self.close)
        print(str(os.getpid()) + ":Pipe connected")


        while True:
            line = self.readstream.readline()
            if line != "":
                try:
                    contents = json.loads(line)
                except json.decoder.JSONDecodeError:
                    print(line)
                with self.log_lock:
                    self.log += [contents]
                    #print("LOG: " + str(len(self.log)))

        self.readstream.close()

    def get_path(self):
        return get_path(os.getpid())

    def close(self):
        os.unlink(self.get_path())
        print(str(os.getpid()) + ":Pipe cleaned")


class Consumer(MessageProc):

    def main(self):
        super().main()
        while True:
            self.receive(
                Message(
                    'data',
                    action=lambda x: print(x)),
                Message(
                    'stop',
                    action=lambda: (sys.exit())))
