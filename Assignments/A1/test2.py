import multiprocessing as mp, os

class MessageProc:
    def start(self):
        print(str(os.getpid()) + ":hi")

me = MessageProc()


p = mp.Process(target=me.start, args=())
p.start()
print(str(os.getpid()) + ":hey" + str(p.pid))
