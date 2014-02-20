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

public class oled_sim_com extends PApplet {


Serial port;

// 2D Array of squares, each represents a pixel on the oled screen
OledPixel[][] oled;

// Number of columns and rows in the oled
int cols = 128;
int rows = 64;
int displayScale = 8;
int displayCols = cols * displayScale;
int displayRows = rows * displayScale;

public void setup() {
  size(displayCols+1,displayRows+1);
  port = new Serial(this, Serial.list()[2], 115200);    // Serial port
  oled = new OledPixel[cols][rows];                   // oled object
  oledClear();

}

public void draw() {
  // draw() not used, serial events control the program flow

}

// Listens for incoming serial data
public void serialEvent(Serial port)
{
    int echo = port.read();
    println("IN: " + echo);
}

// Draws a dark background
public void oledClear()
{
  // Draws a dark background on each "pixel"
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      // Initialize each "pixel"
      oled[i][j] = new OledPixel(i*displayScale,j*displayScale,displayScale,displayScale, 0);
    }
  }

  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      oled[i][j].display();
    }
  }
}

// Event triggered when a mouse button is triggered (left, right or middle clicks )
public void mouseClicked()
{
  // Get the "pixel" position
  int x = (PApplet.parseInt(mouseX)-2) / displayScale;
  int y = (PApplet.parseInt(mouseY)-2) / displayScale;

  // Toggle the "pixel" background
  if (oled[x][y]._fillColor == 0)
  {
    oled[x][y].setColor(200);
  }
  else
  {
    oled[x][y].setColor(0);
  }

  // Re-draw the "pixel"
  oled[x][y].display();

  // Send the corresponding pixel position, using 1 index [1-128]
  port.write('J');
  port.write(x+1);
  port.write(y+1);
  port.write('S');

  // Display the clicked "pixel" position
  print("x: " + x);
  println(" y: " + y);
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

/* OledPixel object *********************************************************/
// Squares representing a pixel of the oled screen
class OledPixel {
  float _x, _y;   // x,y location
  float _w, _h;   // width and height
  int _fillColor = color(0);

  // OledPixel Constructor
  OledPixel(float x, float y, float w, float h, int fillColor) {
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
    String[] appletArgs = new String[] { "oled_sim_com" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
