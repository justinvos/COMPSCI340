import threading
import time

class Thing(threading.Thread):
    def __init__(self, lock):
        super().__init__()
        self.lock = lock
        self.n = 0

    def run(self):
        self.main()

    def main(self):
        with lock:
            print("b start")
            lock.wait(10)
            print("b done")


lock = threading.Condition()

a = Thing(lock)
a.start()

with lock:
    time.sleep(1)
    #lock.notify_all()
    print("a done")
