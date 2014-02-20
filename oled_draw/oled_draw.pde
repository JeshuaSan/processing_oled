import processing.serial.*;
Serial port;

// 2D Array of objects
OledDisplay[][] oled;

// Number of columns and rows in the oled
int cols = 128;
int rows = 64;
int displayScale = 8;
int displayCols = cols * displayScale;
int displayRows = rows * displayScale;

void setup() {
  size(displayCols+1,displayRows+1);
  port = new Serial(this, Serial.list()[2], 115200);
  oled = new OledDisplay[cols][rows];
  oledClear();
}

void draw() {
  // only events

}

void serialEvent(Serial port)
{
    int echo = port.read();
    println("IN: " + echo);
}

void oledClear()
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

void mouseDragged()
{
  int x = (int(mouseX)-2) / displayScale;
  int y = (int(mouseY)-2) / displayScale;
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

// A OledDisplay object
class OledDisplay {
  // A OledDisplay object knows about its location in the oled as well as its size with the variables x,y,w,h.
  float _x, _y;   // x,y location
  float _w, _h;   // width and height
  color _fillColor = color(0);

  // OledDisplay Constructor
  OledDisplay(float x, float y, float w, float h, color fillColor) {
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
