import processing.serial.*;
Serial port;

int echo;
void setup() {

    println(Serial.list());


    port = new Serial(this, Serial.list()[2], 115200);
}

void draw() {
}

void mouseClicked()
{
    port.write(65);
}

void serialEvent(Serial port)
{
    echo = port.read();
    println("IN: " + echo);
}
