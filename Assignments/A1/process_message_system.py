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
        self.last_msg = ""

    def give(self, pid, label, value=""):
        wp = open("p1", "w")
        item = {label: value}
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
        for message in messages:
            if list(data.keys()).count(message.label) > 0:
                value = data[message.label]
                if value == "":
                    message.action()
                else:
                    message.action(value)
