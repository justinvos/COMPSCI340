import os

class Message:
    def __init__(self, label, action=None, guard=None):
        self.label = label
        self.action = action
        self.guard = guard


class MessageProc:

    def start(self):
        pid = os.fork()
        if pid == 0:
            self.main()
        else:
            return pid

    def main(self):
        print("MAIN")
        # setup named pipes

    def give(self, pid, label, *value):
        print(os.getpid(),"given", label)

        # send stuff through pipes

    def receive(self, *messages):

        # receive stuff from pipes

        label = "stop"

        for message in messages:
            if message.label == label:
                message.action()
