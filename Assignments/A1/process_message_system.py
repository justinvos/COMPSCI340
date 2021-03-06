import atexit, json, multiprocessing as mp, os, sys, time, threading

ANY = "any"

def get_path(pid):
    return "/tmp/" + str(pid)

class Message:
    def __init__(self, label, action=lambda: None, guard=lambda: True):
        self.label = label
        self.action = action
        self.guard = guard

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
                    #print("LOG: " + str(len(self.log)))
                self.counter = time.time() + 5
            elif time.time() > self.counter:
                break

        self.readstream.close()
        self.close()

    def get_path(self):
        return get_path(os.getpid())

    def close(self):
        os.unlink(self.get_path())
        print(str(os.getpid()) + ":Pipe cleaned")



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
        self.pointer = 0

        os.mkfifo(self.get_path())
        atexit.register(self.close)

        self.readstream = ReadStreamer(self.log, self.log_lock)
        self.readstream.start()

        self.main()



    def receive(self, *message_types):
        if len(self.log) > self.pointer:
            current_message = self.log[self.pointer]

            for message_type in message_types:
                if message_type.label == ANY:
                    message_type.action()
                    self.pointer += 1
                if current_message["label"] == message_type.label:
                    if len(current_message["values"]) == 1:
                        message_type.action(current_message["values"][0])
                    else:
                        message_type.action()
                    self.pointer += 1




    def give(self, pid, label, *values):
        param_dict = {"label": label, "sender": os.getpid(), "values": values}
        print(str(os.getpid()) + " giving " + param_dict["label"] + ":" + str(param_dict["values"]) + " to " + str(pid))

        f = open(get_path(pid), "w")
        f.write(json.dumps(param_dict) + "\n")
        f.close()

    def get_path(self):
        return get_path(os.getpid())

    def close(self):
        os.unlink(self.get_path())
        print(str(os.getpid()) + ":Pipe cleaned")
