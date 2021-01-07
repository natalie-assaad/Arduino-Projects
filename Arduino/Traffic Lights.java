// first group of LEDs
const int red_2 = 13;
const int yel_2 = 12;
const int gre_2 = 11;

// second group of LEDs
const int red_3 = 10;
const int yel_3 = 9;
const int gre_3 = 8;

// third group of LEDs
const int red_1 = 7;
const int yel_1 = 6;
const int gre_1 = 5;

// variables
int sequence = 1;
long currentTime = 0;
long previousTime = 0;

  // button variables
boolean requestWalk = false; // request to requestWalk
boolean walked = false; // have walked accross the road
const int buttonPin = 3;
const int blu = 4;
int buttonState;

  // advance green variables
boolean advGre = false;
const int photoPin = A0;
int photoValue;
int photoInt = 100;

  // power outage variables
boolean blinkRed = false;
const int petenPin = A1;
int petenValue;
int petenInt = 300;

int interval_1 = 2000;
int interval_2 = 2000;
int interval_3 = 1000;
int interval_4 = 2000;
int interval_5 = 2000;
int interval_6 = 1000;
int interval_7 = 2000;

void setup()
{
  Serial.begin(9600);

  pinMode(red_1, OUTPUT);
  pinMode(yel_1, OUTPUT);
  pinMode(gre_1, OUTPUT);

  pinMode(red_2, OUTPUT);
  pinMode(yel_2, OUTPUT);
  pinMode(gre_2, OUTPUT);

  pinMode(red_3, OUTPUT);
  pinMode(yel_3, OUTPUT);
  pinMode(gre_3, OUTPUT);

  pinMode(blu, OUTPUT);
  pinMode(buttonPin, INPUT);
}

void loop() {
  currentTime = millis();

  photoValue = analogRead(photoPin);
  petenValue = analogRead(petenPin);
  buttonState = digitalRead(buttonPin);
  
  if (photoValue < photoInt) { // checks potentiometer value
    advGre = true;
  }

  if (petenValue > petenInt) { // checks petentiometer value
    blinkRed = true;
  }

  if (buttonState == HIGH) { // checks for request to walk by sensing the button state
    requestWalk = true;
    Serial.println("REQUESTED WALK");
  }
  
  if (sequence == 1) { // sequence 1, red LEDs are on
    digitalWrite(red_1, HIGH);
    digitalWrite(red_2, HIGH);
    digitalWrite(red_3, HIGH);
    digitalWrite(yel_2, LOW);
    digitalWrite(yel_1, LOW);
    if ((currentTime - previousTime) > interval_1) {
      previousTime = currentTime; // resets timer
      sequence = 2;
    }
    if (blinkRed == true) {
      sequence = 9;
    }

  } else if (sequence == 2) { // sequence 2, L3 becomes green
    digitalWrite(red_3, LOW);
    digitalWrite(red_1, HIGH);
    digitalWrite(gre_3, HIGH);
    if ((currentTime - previousTime) > interval_2) {
      previousTime = currentTime;
      sequence = 3;
    }
    if (blinkRed == true) {
      sequence = 9;
    }
    if (requestWalk == true && walked == true) { // turns LED off after the person has walked accross the road
      requestWalk = false;
      walked = false;
      digitalWrite(blu, LOW);
    }

  } else if (sequence == 3) { // sequence 3, L3 becomes yellow 
    digitalWrite(gre_3, LOW);
    digitalWrite(yel_3, HIGH);
    if ((currentTime - previousTime) > interval_3) {
      previousTime = currentTime;
      sequence = 4;
    }
    if (blinkRed == true) {
      sequence = 9;
    }

  } else if (sequence == 4) { // sequence 4, red LEDs are on
    digitalWrite(yel_3, LOW);
    digitalWrite(red_3, HIGH);
    if ((currentTime - previousTime) > interval_4) {
      previousTime = currentTime;
      sequence = 5;
    }
    if (blinkRed == true) {
      sequence = 9;
    }
    if (requestWalk == true) { // allows person to requestWalk through proper sequence
      digitalWrite(blu, HIGH);
      walked = true;
    }

  } else if (sequence == 5) { // sequence 5, L1 and L2 green LEDs are turned on
    if (advGre == true) {
      sequence = 7;
    }
    digitalWrite(red_2, LOW);
    digitalWrite(red_1, LOW);
    digitalWrite(gre_2, HIGH);
    digitalWrite(gre_1, HIGH);
    if ((currentTime - previousTime) > interval_5) {
      previousTime = currentTime;
      sequence = 6;
    }
    if (blinkRed == true) {
      sequence = 9;
    }

  } else if (sequence == 6) { // sequence 6, L1 and L2 yellow LEDs are on
    digitalWrite(gre_2, LOW);
    digitalWrite(gre_1, LOW);
    digitalWrite(yel_2, HIGH);
    digitalWrite(yel_1, HIGH);
    if ((currentTime - previousTime) > interval_6) {
      previousTime = currentTime;
      sequence = 1;
    }
    if (blinkRed == true) {
      sequence = 9;
    }
    
  } else if (sequence == 7) { // advanced green from sequence 5
    digitalWrite(red_2, HIGH);
    digitalWrite(red_3, HIGH);
    digitalWrite(gre_1, HIGH);
    
    digitalWrite(yel_1, LOW);
    digitalWrite(yel_2, LOW);
    digitalWrite(yel_3, LOW);
    digitalWrite(gre_2, LOW);
    digitalWrite(gre_3, LOW);
    digitalWrite(red_1, LOW);
    digitalWrite(blu, LOW);
    if ((currentTime - previousTime) > interval_7) {
      previousTime = currentTime;
      advGre = false;
      digitalWrite(gre_1, LOW);
      sequence = 5;
  }
      if (blinkRed == true) {
      sequence = 9;
    }

  } else if (sequence == 9) { // Red LEDs flash red during power outage, all other LEDs are off
      digitalWrite(red_1, HIGH);
      digitalWrite(red_2, HIGH);
      digitalWrite(red_3, HIGH);
      digitalWrite(gre_1, LOW);
      digitalWrite(gre_2, LOW);
      digitalWrite(gre_3, LOW);
      digitalWrite(yel_1, LOW);
      digitalWrite(yel_2, LOW);
      digitalWrite(yel_3, LOW);
      digitalWrite(blu, LOW);
      delay(100);
      digitalWrite(red_1, LOW);
      digitalWrite(red_2, LOW);
      digitalWrite(red_3, LOW);
      delay(100);
      if (petenValue < petenInt) {
        blinkRed = false;
        sequence = 1;
        previousTime = currentTime;
      }
  }
}
