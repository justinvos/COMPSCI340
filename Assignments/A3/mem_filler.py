# A process to use increasing amounts of memory.

import os
import subprocess # run only added in Python 3.5

def show_info(pid):
	subprocess.run(['vmstat', '-a'])
	#subprocess.run(['cat /proc/meminfo | egrep "MemFree|MemAvailable"'], shell=True)
	#subprocess.run(['cat', '/proc/{}/maps'.format(pid)])

big_list = []
LIST_INCREMENT = 10000000
up_to = 0
my_pid = os.getpid()
show_info(my_pid)
for i in range(60):
	big_list += list(range(up_to, up_to + LIST_INCREMENT))
	show_info(my_pid)
	up_to += LIST_INCREMENT
