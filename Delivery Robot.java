// variables
  // DC Motors
const int rightMotor = 12; // list pins for motors
const int leftMotor = 11;

  // PIR Sensor
const int pirInput = A0; // input pin for PIR Sensor
int detectMotion = 0; // variable for reading pirInput status
int pirState = LOW; // start with no motion detected, thus PIR State low

   // IR Sensor
const int irInputLeft = A5; // left sensor
const int irInputRight = A3; // right sensor
const int irInputMid = A4;
int IRleft;
int IRright;
int IRmid;

  // Ultrasonic Sensor
const int trigPin = 6; // define pin numbers
const int echoPin = 7;
long duration; // define variables
int distance;


  // LCD Screen
#include <LiquidCrystal.h> // includes the LiquidCrystal Library 
const int rs = 8, en = 5, d4 = 4, d5 = 3, d6 = 2, d7 = 9;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);


void setup() {

  Serial.begin(9600); // starts the serial communication
    // Motors
  pinMode(rightMotor, OUTPUT); // declare DC Motors as outputs
  pinMode(leftMotor, OUTPUT);
    // PIR Sensor
  pinMode(pirInput, INPUT); // declare PIR Sensor input
    // IR Sensor
  pinMode(irInputRight, INPUT); // declare IR sensors to inputs
  pinMode(irInputLeft, INPUT);
  pinMode(irInputMid, INPUT);
    // Ultrasonic Sensor
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
    // LCD Screen
  lcd.begin(16,2);
  lcd.print("Object in path"); 

}

void loop() {

  detectMotion = digitalRead(pirInput); // reads PIR input value
  
  if (detectMotion == HIGH) { // if motion is detected from PIR sensor
    pirState = HIGH; // PIR state is high
  } else {
    pirState = LOW; // else if no motion is detected the PIR state is low
  }

  //Reads all IR sensors
  IRleft = analogRead(irInputLeft);
  IRright = analogRead(irInputRight);
  IRmid = analogRead(irInputMid);
  
  if (pirState == HIGH) { // if PIR state is high (because motion was detected)
      // Clears the trigPin
      digitalWrite(trigPin, LOW);
      delayMicroseconds(2);
      // Sets the trigPin on HIGH state for 10 micro seconds 
      digitalWrite(trigPin, HIGH);
      delayMicroseconds(10);
      digitalWrite(trigPin, LOW);
      // Reads the echoPin, returns the sound wave travel time in microseconds
      duration = pulseIn(echoPin, HIGH);
      // Calculating the distance
      distance= duration*0.034/2;
      Serial.println(distance);
    
     if (distance > 15) { // if there is no object in the robots path
       if ((IRleft < 900) && (IRright < 900) && (IRmid > 900)) { // move forward
            analogWrite(leftMotor, 150); // all DC motors begin to rotate clockwise
            analogWrite(rightMotor, 150);
        }
    
       if ((IRleft < 900) && (IRright > 900) && (IRmid < 900)) { // turn right
           analogWrite(leftMotor, 190);
           analogWrite(rightMotor, 75);
       }
       
       if ((IRleft < 900) && (IRright > 900) && (IRmid < 900)) { // turn right
           analogWrite(leftMotor, 190);
           analogWrite(rightMotor, 75);
       }
  
        if ((IRleft > 900) && (IRright < 900) && (IRmid < 900)) { // turn left
           analogWrite(leftMotor, 75);
           analogWrite(rightMotor, 190);
       }

        if ((IRleft < 900) && (IRright > 900) && (IRmid > 900)) { // turn left
           analogWrite(leftMotor, 75);
           analogWrite(rightMotor, 190);
       }
    
        if ((IRleft < 900) && (IRright < 900) && (IRmid < 900)) { // full stop
            analogWrite(leftMotor, 0);
            analogWrite(rightMotor, 0);
       }
     } else if (distance < 15) {  // else if there is an object detected in front of the robots
        lcd.display();   // displays message that an object is in the robots path
        analogWrite(leftMotor, 0); // stops motors until object is removed
        analogWrite(rightMotor, 0);
     }      
  }
}
