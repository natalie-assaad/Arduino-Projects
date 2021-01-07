// variables

  // photoresistor
int photoPin = A0;
int photoValue;
int photoBright;
int photoDim;
int newPhotoValue;

  // temperature sensor
#include <OneWire.h> // inclide libraries
#include <DallasTemperature.h>
#define ONE_WIRE_BUS 8 
OneWire oneWire(ONE_WIRE_BUS); 
DallasTemperature sensors(&oneWire);
int celcius;
int celHot;
int celCold;
int newCelcius;

  // RGB LED
const int RED_PIN = A1;
const int GREEN_PIN = A2;
const int BLUE_PIN = A3;

  // buzzer
const int buzzer = 12;

  // sequence & millis
int sequence = 1;
int interval = 1000;
long currentTime = 0;
long previousTime = 0;

  // moisture sensor
const int soilPin = A5;
int moistureLevel;
int veryMoist;
int noMoist;

void setup() {
  
  // put your setup code here, to run once:
  Serial.begin(9600);

  // photoresistor
  photoValue = analogRead(photoPin);

  // temperature sensor
  sensors.requestTemperatures(); 
  celcius = sensors.getTempCByIndex(0);  

  // RGB LED
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);

  // buzzer
  pinMode(buzzer, OUTPUT);

  // moisture sensor
  moistureLevel = analogRead(soilPin);

}

void loop() {
  // put your main code here, to run repeatedly:
  currentTime = millis();

  // read photoresistor again to detect for changes
  photoBright = analogRead(photoPin);
  photoDim = analogRead(photoPin);

  // read temperature again to detect for changes
  sensors.requestTemperatures(); 
  celHot = sensors.getTempCByIndex(0);
  celCold = sensors.getTempCByIndex(0);

  // read moisture level again to detect for changes
  veryMoist = analogRead(soilPin);
  noMoist = analogRead(soilPin);

  // print values
  Serial.print("Moisture Level: ");
  Serial.println(moistureLevel);
  Serial.print("Very Moist: ");
  Serial.println(veryMoist);
  Serial.print("No Moist: ");
  Serial.println(noMoist);
  Serial.print("Sequence: ");
  Serial.println(sequence);
  Serial.println("");

  if ((photoValue + 50) < photoBright) { // photoresistor detects too much light
    sequence = 2;
  } else if ((photoValue - 50) > photoDim) { // photoresistor detects less light
    sequence = 3;
  } else if ((celcius + 1) < celHot) { // temperature sensor detects too much heat
    sequence = 4;
  } else if ((celcius - 1) > celCold) { // temperature sensor detects too little heat
    sequence = 5;
  } else if ((moistureLevel + 50) < veryMoist) { // moisture sensor detects too much moisture
    sequence = 6;
  } else if ((moistureLevel - 50) > noMoist) { // moisture sensor does not detect enough moisture
    sequence = 7;
  } else { // else none of the above are true so it goes to 
    sequence = 1;
  }
  
  if (sequence == 1) {
     analogWrite(RED_PIN, 0); // default colour is green
     analogWrite(GREEN_PIN, 255);
     analogWrite(BLUE_PIN, 0);
     noTone(buzzer);
  }

  else if (sequence == 2) { // if photoresistor detects too much light, sequence = 2
    tone(buzzer, 5);
    analogWrite(RED_PIN, 255); // RGB LED changes from yellow
    analogWrite(GREEN_PIN, 255);
    analogWrite(BLUE_PIN, 0);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 255); // to red
      analogWrite(GREEN_PIN, 0);
      analogWrite(BLUE_PIN, 0);
      sequence = 2;
    }
  }

  else if (sequence == 3) { // if photoresistor detects less light
    tone(buzzer, 5);
    analogWrite(RED_PIN, 255); // RGB LED changes from yellow
    analogWrite(GREEN_PIN, 255);
    analogWrite(BLUE_PIN, 0);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 0); // to green
      analogWrite(GREEN_PIN, 255);
      analogWrite(BLUE_PIN, 0);
      sequence = 3;
    }
  }

  else if (sequence == 4) { // temperature sensor detects too much heat
    tone(buzzer, 5);
    analogWrite(RED_PIN, 255); // RGB LED changes from orange
    analogWrite(GREEN_PIN, 255);
    analogWrite(BLUE_PIN, 0);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 255); // to red
      analogWrite(GREEN_PIN, 0);
      analogWrite(BLUE_PIN, 0);
      sequence = 4;
    }
  }

  else if (sequence == 5) { // temperature sensor detects too little heat
    tone(buzzer, 5);
    analogWrite(RED_PIN, 255); // RGB LED changes from orange
    analogWrite(GREEN_PIN, 255);
    analogWrite(BLUE_PIN, 0);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 0); // to blue
      analogWrite(GREEN_PIN, 0);
      analogWrite(BLUE_PIN, 255);
      sequence = 5;
    }
  }

  else if (sequence == 6) { // moisture sensor detects too much moisture
    tone(buzzer, 5);
    analogWrite(RED_PIN, 0); // RGB LED changes from blue
    analogWrite(GREEN_PIN, 0);
    analogWrite(BLUE_PIN, 255);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 255); // to red
      analogWrite(GREEN_PIN, 0);
      analogWrite(BLUE_PIN, 0);
      sequence = 6;
    }
  }

  else if (sequence == 7) { // moisture sensor detects too little moisture
    tone(buzzer, 5);
    analogWrite(RED_PIN, 0); // RGB LED changes from blue
    analogWrite(GREEN_PIN, 0);
    analogWrite(BLUE_PIN, 255);
    if ((currentTime - previousTime) > interval) {
      tone(buzzer, 5);
      previousTime = currentTime;
      analogWrite(RED_PIN, 0); // to green
      analogWrite(GREEN_PIN, 255);
      analogWrite(BLUE_PIN, 0);
      sequence = 7;
    }
  }

}
