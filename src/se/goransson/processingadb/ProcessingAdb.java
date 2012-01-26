/**
 *
 * ##copyright##
 *
 * Basic implementation of an ADB interface for Processing-android sketches.
 *
 * Copyright (C) 2012  Andreas Göransson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

package se.goransson.processingadb;

import java.io.IOException;
import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import processing.core.PApplet;

/**
 * Basic implementation of an ADB interface for Processing-android sketches.
 * 
 * Based on the work by Niels Brouwers, Mads Hobye, and Benjamin Weber.
 * 
 * @author Andreas Goransson
 * 
 * 
 */
public class ProcessingAdb {

	/* TODO */
	// available()
	// read()
	// ** connect()
	// readChar()
	// readBytes()
	// readBytesUntil()
	// readString()
	// readStringUntil()
	// buffer()
	// bufferUntil()
	// last()
	// lastChar()
	// list()
	// ** write()
	// clear()
	// stop()
	// ** adbEvent()

	// myParent is a reference to the parent sketch
	private PApplet myParent;
	private Method adbEvent;

	public final static String VERSION = "##version##";

	// Server instance
	private Server mServer;

	public static final int STATE_DISCONNECTED = 100;
	public static final int STATE_CONNECTED = 101;
	public int STATE = STATE_DISCONNECTED;

	public boolean DEBUG = false;

	/**
	 * Create the ProcessingAdb library instance.
	 * 
	 * @param theParent
	 */
	public ProcessingAdb(PApplet theParent) {
		myParent = theParent;
		welcome();

		// Instantiate the adb-event method
		try {
			adbEvent = myParent.getClass().getMethod("adbEvent", int.class);
		} catch (Exception e) {
			e.printStackTrace();
			PApplet.println("Hmm... did you forget the adbEvent(int value){} method?");
		}
	}

	/**
	 * Connect to the Adb Device, default port 4567.
	 */
	public void connect() {
		connect(4567);
	}

	/**
	 * Connect to the Adb Device.
	 * 
	 * @param port
	 *            The port on which the Adb Device is running.
	 */
	public void connect(int port) {
		// Create TCP server
		mServer = null;
		try {
			mServer = new Server(eventHandler, port);
			mServer.start();
		} catch (IOException e) {
			e.printStackTrace();
			PApplet.println(e.getMessage());
			STATE = STATE_DISCONNECTED;
		}
	}

	private Handler eventHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Server.SERVER_STARTED:
				if (DEBUG)
					PApplet.println("Adb Server started");
				break;
			case Server.SERVER_STOPPED:
				if (DEBUG)
					PApplet.println("Adb Server stopped");
				break;
			case Server.CLIENT_DISCONNECTED:
				if (DEBUG)
					PApplet.println("Adb Client disconnected");
				STATE = STATE_DISCONNECTED;
				break;
			case Server.CLIENT_CONNECTED:
				if (DEBUG)
					PApplet.println("Adb Client connected");
				STATE = STATE_CONNECTED;
				break;
			case Server.CLIENT_RECEIVE:
				if (DEBUG)
					PApplet.println("Adb Client received data");
				byte[] data = (byte[]) msg.obj;

				if (data.length < 2)
					return;

				// Invoke the adb-event method with the sensor value as argument
				if (adbEvent != null) {
					try {
						adbEvent.invoke(myParent,
								new Object[] { (data[0] & 0xff)
										| ((data[1] & 0xff) << 8) });
					} catch (Exception e) {
						PApplet.println("I failed to invoke the adbEvent method for some reason... I'm not sure why");
						e.printStackTrace();
						adbEvent = null;
					}
				}

				break;
			}
		}
	};

	private void welcome() {
		System.out.println("##name## ##version## by ##author##");
	}

	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * Write a string to the adb device.
	 * 
	 * @param message
	 *            The message to send.
	 */
	public void write(String message) {
		write(message.getBytes());
	}

	/**
	 * Write a character to the adb device.
	 * 
	 * @param c
	 *            The character to send.
	 */
	public void write(char c) {
		write(new byte[] { (byte) c });
	}

	/**
	 * 
	 * @param value
	 *            the value to write
	 */
	public void write(byte[] value) {
		try {
			mServer.send(value);
		} catch (IOException e) {
			e.printStackTrace();
			PApplet.println(e.getMessage());
		}
	}

	/**
	 * Stop the communication with the adb device.
	 */
	public void stop() {
		mServer.stop();
	}

}
