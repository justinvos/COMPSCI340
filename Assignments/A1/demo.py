from process_message_system import *
import sys



class Consumer(MessageProc):

    def main(self):
        super().main()
        print("CONSUMER MAIN")
        while True:
            self.receive(
                Message(
                    'data',
                    action=a),
                Message(
                    'stop',
                    action=b))
def a():
    print("a")

def b():
    print("b")
    sys.exit()



if __name__=='__main__': # really do need this
    me = MessageProc()
    me.main()
    consumer = Consumer().start()
    for num in range(5):
        me.give(consumer, 'data', num + 1)
    me.give(consumer, 'stop')
