import processing.serial.*;
Serial port;

// 2D Array of squares, each represents a pixel on the oled screen
OledPixel[][] oled;

// Number of columns and rows in the oled
int cols = 128;
int rows = 64;
int displayScale = 8;
int displayCols = cols * displayScale;
int displayRows = rows * displayScale;

void setup() {
  size(displayCols+1,displayRows+1);
  port = new Serial(this, Serial.list()[2], 115200);    // Serial port
  oled = new OledPixel[cols][rows];                   // oled object
  oledClear();

}

void draw() {
  // draw() not used, serial events control the program flow

}

// Listens for incoming serial data
void serialEvent(Serial port)
{
    int echo = port.read();
    println("IN: " + echo);
}

// Draws a dark background
void oledClear()
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
void mouseClicked()
{
  // Get the "pixel" position
  int x = (int(mouseX)-2) / displayScale;
  int y = (int(mouseY)-2) / displayScale;

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

void keyPressed()
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
  color _fillColor = color(0);

  // OledPixel Constructor
  OledPixel(float x, float y, float w, float h, color fillColor) {
    _x = x;
    _y = y;
    _w = w;
    _h = h;
    _fillColor = fillColor;
  }

  void setColor(color fillColor)
  {
    _fillColor = fillColor;
  }

  void display() {
    fill(this._fillColor);
    stroke(100);
    rect(_x, _y, _w, _h);
  }
}
