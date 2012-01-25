# ProcessingAdb
Processing-android library that enables basic communication between a Processing-android sketch and a Arduino device. This library uses the ADB interface for communication... not the ADK.
This library is based on the work done by **Niels Brouwers**, **Mads Hobye**, and **Benjamin Weber**.

## Requirements
* Arduino MEGA ADK (or compatible setup)
* Android device with a functional ADB interface (rooted or stock)

## Tested android devices
* Nexus S

## Installation
1. A simple...
2. ...list...
3. ...of...
4. ...instructions.

# Getting started

## Create the library instance and connect to the ADB device
``` java
/* The standard instantiation of libraries in Processing */
ProcessingAdb adb = new ProcessingAdb(this);
adb.connect();
```

## Add the __required__ event callback method
``` java
int readValue = 0;
void adbEvent(int val) {
  readValue = val;
}
```

## Detect ADB connection status
``` java
/* Note: This doesn't seem fully stable yet, and it's sometimes slow to react. */
if ( adb.STATE == ProcessingAdb.STATE_DISCONNECTED) {
  // TODO add your code here for the "disconnected" status
} else if ( adb.STATE == ProcessingAdb.STATE_CONNECTED ) {
  // TODO add your code here for the "connected" status
}
```

## Send a message (byte array) to the ADB device
``` java
/* 
  Currently, the only implemented methods of sending data are:
  byte[], String, and char
*/
int value = 255;
adb.write( new byte[]{(byte)value});
```