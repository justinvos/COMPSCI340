import drive

def addAfterPadding(content, padding, length):
    amount = length - len(str(content))
    return str(content) + amount * padding

def addBeforePadding(content, padding, length):
    amount = length - len(str(content))
    return amount * padding + str(content)


class Volume:
    ROOT_ENTRY_NUM = 6
    NORMAL_ENTRY_NUM = 8
    ROOT_INFO_BLOCK = 0

    def __init__(self, drive):
        self.drive = drive
        self.ready = False

    def format(self):
        self.drive.format()
        self.bitmap = "-" * drive.Drive.DRIVE_SIZE
        self.setBitmap(Volume.ROOT_INFO_BLOCK, True)
        block = self.bitmap
        for index in range(Volume.ROOT_ENTRY_NUM):
            block += str(Entry(Volume.ROOT_INFO_BLOCK, index))
        self.drive.write_block(Volume.ROOT_INFO_BLOCK, block)
        self.ready = True

    def load(self):
        self.drive.reconnect()
        self.bitmap = self.drive.read_block(Volume.ROOT_INFO_BLOCK)[0:drive.Drive.DRIVE_SIZE]

    def setBitmap(self, address, flag):
        b = "-"
        if flag:
            b = "+"
        self.bitmap = self.bitmap[:address] + b + self.bitmap[address + 1:]

    def getBitmap(self, address):
        return self.bitmap[address:address + 1] == "+"

    def write_entry(self, entry):
        start = 0
        if entry.getAddress() == Volume.ROOT_INFO_BLOCK:
            start = drive.Drive.DRIVE_SIZE
        start += entry.getIndex() * Entry.LENGTH
        block = self.drive.read_block(entry.getAddress())
        block = block[:start] + str(entry) + block[start + Entry.LENGTH:]
        self.drive.write_block(entry.getAddress(), block)

    def read_entry(self, block, address):
        start = 0
        if entry.getAddress() == Volume.ROOT_INFO_BLOCK:
            start = drive.Drive.DRIVE_SIZE
        start += entry.getIndex() * Entry.LENGTH
        block = self.drive.read_block(entry.getAddress())
        block = block[:start] + str(entry) + block[start + Entry.LENGTH:]

    def getRoot(self):
        root = Entry(0, 0)
        root.setRoot()
        root.setDir()
        root.setBlocks([0])
        return root

    def getChild(self, parent, name):
        for slot in range(len(parent.getBlocks())):
            address = parent.getBlock(slot)
            if parent.isRoot() and address == 0 or address != 0:
                block = self.drive.read_block(address)
                start = 0
                entryNum = Volume.NORMAL_ENTRY_NUM
                if address == 0:
                    start = drive.Drive.DRIVE_SIZE
                    entryNum = Volume.ROOT_ENTRY_NUM
                for index in range(entryNum):
                    entryStart = start + index * Entry.LENGTH
                    entry = Entry.Parse(address, index, block[entryStart:entryStart + Entry.LENGTH])
                    if entry.getName() == name:
                        return entry

    def findEntry(self, path):
        path = path.strip("/")
        names = path.split("/")
        parent = self.getRoot()
        for level in range(len(names)):
            entry = self.getChild(parent, names[level])
            if entry == None:
                return None
            else:
                parent = entry
        return parent

    def ls(self, parent):
        blocks = parent.getBlocks()

        print("name     type size")
        for slot in range(len(blocks)):
            address = parent.getBlock(slot)

            start = 0
            entryNum = Volume.NORMAL_ENTRY_NUM
            if address == 0:
                start = 128
                entryNum = Volume.ROOT_ENTRY_NUM
            content = self.drive.read_block(address)
            for index in range(entryNum):
                entryStart = start + index * Entry.LENGTH
                entry = Entry.Parse(address, index, content[entryStart:entryStart + Entry.LENGTH])
                if entry.getName() != "":
                    print(addAfterPadding(entry.getName()," ", 8) + " " + entry.getType() + " " + addBeforePadding(entry.getSize(),"0", 4))

    def getEntryContent(self, entry):
        blocks = entry.getBlocks()

        content = ""
        for slot in range(len(blocks)):
            address = blocks[slot]
            if address != 0:
                content += self.drive.read_block(address)

        return content.strip()

    def appendEntry(self, entry, data):
        content = self.getEntryContent(entry) + data
        print(len(content))

        chunks = []
        for slot in range(len(entry.getBlocks())):
            start = slot * drive.Drive.BLK_SIZE
            end = min(len(content), slot * drive.Drive.BLK_SIZE + drive.Drive.BLK_SIZE)


            address = entry.getBlock(slot)
            if start < len(content):
                print(start, end)
                chunks += [addAfterPadding(content[start:end], " ", drive.Drive.BLK_SIZE)]

                if address == 0:
                    address = self.getEmptyBlock()
                    entry.setBlock(slot, address)
                    self.write_entry(entry)
                self.drive.write_block(address, chunks[slot])
            elif address != 0:
                entry.setBlock(slot, 0)
                self.drive.write_block(address, drive.Drive.EMPTY_BLK)

    def getEmptyBlock(self):
        for address in range(drive.Drive.DRIVE_SIZE):
            content = self.drive.read_block(address)
            if content == drive.Drive.EMPTY_BLK:
                return address

    def getEmptyEntry(self, parent):
        return self.getChild(parent, "")

    def addDirectoryBlock(self, parent):
        slot = parent.getEmptySlot()
        if slot != -1:
            address = self.getEmptyBlock()
            if address != -1:
                print()
                content = ""
                for index in range(Volume.NORMAL_ENTRY_NUM):
                    content += str(Entry(address, index))
                self.drive.write_block(address, content)
                parent.setBlock(slot, address)
                self.write_entry(parent)


    def makefile(self, parent, name):
        if self.getChild(parent, name) == None:
            entry = self.getEmptyEntry(parent)
            if entry == None:
                self.addDirectoryBlock(parent)
                entry = self.getEmptyEntry(parent)
            entry.setName(name)
            self.write_entry(entry)

    def makedir(self, parent, name):
        if self.getChild(parent, name) == None:
            entry = self.getEmptyEntry(parent)
            if entry == None:
                self.addDirectoryBlock(parent)
                entry = self.getEmptyEntry(parent)
            entry.setName(name)
            entry.setDir()
            self.write_entry(entry)

    def delfile(self, entry):
        for slot in range(len(entry.getBlocks())):
            address = entry.getBlock(slot)
            if address != 0:
                self.drive.write_block(address, drive.Drive.EMPTY_BLK)
                entry.setBlock(slot, 0)
                self.write_entry(entry)
        self.write_entry(Entry(entry.getAddress(), entry.getIndex()))

    def deldir(self, entry):
        for slot in range(len(entry.getBlocks())):
            address = entry.getBlock(slot)
            if address != 0:
                self.vol.drive.write_block(address, drive.Drive.EMPTY_BLK)
                entry.setBlock(slot, 0)
                self.vol.write_entry(entry)
        self.write_entry(Entry(entry.getAddress(), entry.getIndex()))

class Entry:
    LENGTH = 64
    ADDRESS_NUM = 12

    def Parse(address, index, line):
        entry = Entry(address, index)
        if line[0:2] == "d:":
            entry.setDir()
        entry.setName(line[2:10])
        entry.setSize(int(line[11:14]))
        for slot in range(Entry.ADDRESS_NUM):
            start = 16 + slot * 4
            entry.setBlock(slot, int(line[start:start+3]))
        return entry

    def __init__(self, address, index):
        self.isroot = False
        self.address = address
        self.index = index
        self.isdir = False
        self.name = ""
        self.size = 0
        self.blocks = [0] * Entry.ADDRESS_NUM

    def setRoot(self):
        self.isroot = True

    def isRoot(self):
        return self.isroot

    def getAddress(self):
        return self.address

    def getIndex(self):
        return self.index

    def isDir(self):
        return self.isdir

    def getType(self):
        if self.isDir():
            return "dir "
        else:
            return "file"

    def setDir(self):
        self.isdir = True

    def setFile(self):
        self.isdir = False

    def getName(self):
        return self.name

    def setName(self, name):
        self.name = name.strip()

    def getSize(self):
        return self.size

    def setSize(self, size):
        self.size = size

    def getBlock(self, slot):
        return self.blocks[slot]

    def setBlock(self, slot, address):
        self.blocks[slot] = address

    def getBlocks(self):
        return self.blocks

    def setBlocks(self, blocks):
        self.blocks = blocks

    def getEmptySlot(self):
        for slot in range(len(self.blocks)):
            if self.getBlock(slot) == 0:
                return slot
        return -1

    def __str__(self):
        s = "f:"
        if self.isDir():
            s = "d:"
        s += addAfterPadding(self.getName(), " ", 8) + " "
        s += addBeforePadding(self.getSize(), "0", 4) + ":"
        for slot in range(Entry.ADDRESS_NUM):
            s += addBeforePadding(self.getBlock(slot), "0", 3) + " "
        return s


mydrive = drive.Drive("a")
vol = Volume(mydrive)
vol.load()

#vol.ls(vol.getRoot())

vol.makedir(vol.getRoot(), "d")
d = vol.findEntry("/d")

vol.makedir(d, "stuff")
vol.makefile(vol.findEntry("/d/stuff"), "code")
#code = vol.findEntry("/code")
vol.ls(vol.getRoot())

#print(vol.getEntryContent(code))

#vol.makedir(vol.getRoot(), "d")
