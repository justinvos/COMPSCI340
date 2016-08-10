# COMPSCI 340 Notes


## Introduction

A computer system can be divided into four components:
* Hardware
* Operating system
* Application programs
* Users

The **hardware** provides basic computing resources for the system.

An **operating system** is a program that manages a computer's hardware and provies a basis for applications.

The **minimalist view** of operating systems includes only the minimum amount of osftware required to allow the computer to function.

The **maximalist view** of operating systems includes all the software which comes with a standard release.

The **application programs** define that ways in which these resources are used to solve users' computing problems.


The **bootstrap program** or **firmware** is the initial program that is run when the computer is powered on. It is typically stored in ROM (read-only memory) or EEPROM (electrically erasable programmable read-only memory). It then locates the operating system kernal and loads it into memory.

The **kernal** is the one program running at all times on the computer. It loads system processes and system daemons.

**Monolithic** design has all components in one, allowing them to freely interact with each other.

**Onion-layer** design seperates the components into various layers which simplifies verificaition, debugging but can be inefficient with lots of layers to traverse through.

**Modular** design only loads components when needed.

**Microkernals** includes the minimal amount of components in the operating system and all other components are user processes.

## Processes

A **process** is a program in execution.

A **process control block** (PCB) includes the process state, program counter, CPU registers, CPU-scheduling information and memory-management information, accounting information and I/O status information.
