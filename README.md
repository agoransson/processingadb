# ProcessingAdb
Processing-android library that enables basic communication between a Processing-android sketch and a Arduino device. This library uses the ADB interface for communication... not the ADK.

## Requirements
* Arduino MEGA ADK (or compatible setup)
* Android device with a functional ADB interface (rooted or stock)

## Tested android devices
* Nexus S, version 2.3.6
* Nexus One, version 2.3.4 (CyanogenMod)
* ZTE Blade, version 2.1-update1

## Installation
1. Download the latest [version](https://github.com/agoransson/processingadb/downloads) of the library.
2. Unzip the archive into your <processing-sketchbook>/libraries/ folder
3. Restart Processing
4. Open the example "adbExample" located inside the Processing IDE menu, File-Examples-Contributed-ProcessingAdb

# Getting started

## Set the required USES_PERMISSIONS for the sketch
Make sure that the INTERNET permission is selected for the sketch.
Menu: __Android__ - __Sketch Permissions__ - INTERNET

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

# License
``` java
Copyright (C) 2012  Andreas Göransson

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

# Credits
This library is based on the work done by **Niels Brouwers**, **Mads Hobye**, and **Benjamin Weber**.

## Want to help?
If you want to help me develop this library, let me know!