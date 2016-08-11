import json
import os
import sys
import time

class Message:
    def __init__(self, label, action=None, guard=None):
        self.label = label
        self.action = action
        self.guard = guard


class MessageProc:

    def start(self, *args):
        pid = os.fork()
        if pid == 0:
            self.main()
        else:
            return pid

    def main(self):
        self.last_msg = None

    def give(self, pid, label, *values):
        print(pid, label, values)
        wp = open("p1", "w")
        item = {label: values}
        wp.write(json.dumps(item))
        wp.close()
        time.sleep(0.1)

    def receive(self, *messages):
        wr = open("p1", "r")
        raw = wr.read()
        wr.close()

        if raw == "":
            return

        if raw == self.last_msg:
            return
        self.last_msg = raw

        data = json.loads(raw)
        if list(data.keys()).count("data") > 0:
            print(data["data"][0])
        elif list(data.keys()).count("stop") > 0:
            sys.exit()
