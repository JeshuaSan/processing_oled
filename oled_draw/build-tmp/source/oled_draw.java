import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class oled_draw extends PApplet {


Serial port;

// 2D Array of objects
OledDisplay[][] oled;

// Number of columns and rows in the oled
int cols = 128;
int rows = 64;
int displayScale = 8;
int displayCols = cols * displayScale;
int displayRows = rows * displayScale;

public void setup() {
  size(displayCols+1,displayRows+1);
  port = new Serial(this, Serial.list()[2], 115200);
  oled = new OledDisplay[cols][rows];
  oledClear();
}

public void draw() {
  // only events

}

public void serialEvent(Serial port)
{
    int echo = port.read();
    println("IN: " + echo);
}

public void oledClear()
{
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      // Initialize each object
      oled[i][j] = new OledDisplay(i*displayScale,j*displayScale,displayScale,displayScale, 0);
    }
  }

  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      oled[i][j].display();
    }
  }
}

public void mouseDragged()
{
  int x = (PApplet.parseInt(mouseX)-2) / displayScale;
  int y = (PApplet.parseInt(mouseY)-2) / displayScale;
  if ( oled[x][y]._fillColor == 0)
  {
    oled[x][y].setColor(200);

    oled[x][y].display();
    port.write('J');
    port.write(x+1);
    port.write(y+1);
    port.write('S');
    print("x: " + x);
    println(" y: " + y);
  }
}

public void keyPressed()
{
  if (key == 'c')
  {
    oledClear();
    port.write('J');
    port.write(0);
    port.write(0);
    port.write('C');
  }
}

// A OledDisplay object
class OledDisplay {
  // A OledDisplay object knows about its location in the oled as well as its size with the variables x,y,w,h.
  float _x, _y;   // x,y location
  float _w, _h;   // width and height
  int _fillColor = color(0);

  // OledDisplay Constructor
  OledDisplay(float x, float y, float w, float h, int fillColor) {
    _x = x;
    _y = y;
    _w = w;
    _h = h;
    _fillColor = fillColor;
  }

  public void setColor(int fillColor)
  {
    _fillColor = fillColor;
  }

  public void display() {
    fill(this._fillColor);
    stroke(100);
    rect(_x, _y, _w, _h);
  }
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "oled_draw" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
