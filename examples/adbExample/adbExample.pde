import se.goransson.processingadb.*;

ProcessingAdb adb;
int serialRead;

void setup() {
  adb = new ProcessingAdb(this);
  adb.connect();

  noStroke();
  smooth();
}

void draw() {
  background( 255 );

  // Draw "read" value
  fill(255, 0, 0);
  ellipse(width/2, height/2, serialRead, serialRead);

  // Draw "write" value
  fill(0, 255, 0, 200);
  rect(mouseX-20, height/2-50, 40, 100);

  // Draw "connected" value
  if ( adb.STATE == ProcessingAdb.STATE_DISCONNECTED)
    fill( 255, 0, 0 );
  else if ( adb.STATE == ProcessingAdb.STATE_CONNECTED )
    fill( 0, 255, 0 );
  rect( 10, 10, 20, 20 );
}

boolean surfaceTouchEvent(MotionEvent event) {
  float x = (float) ( (float)event.getX() / (float)width );
  float y = (float) ( (float)event.getY() / (float)height );

  adb.write(new byte[] { 
    (byte)(x * 255.0f), (byte)(y * 255.0f )
  } 
  );

  return super.surfaceTouchEvent(event);
}

void adbEvent(int val) {
  serialRead = val;
}
