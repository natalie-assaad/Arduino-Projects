# import required
import RPi.GPIO as GPIO
import os
import time
import pygame
from gpiozero import LED

# initialize audio
file = "Psycho Scream-SoundBible.com-1441943673.mp3"
file2 = "death.mp3"
pygame.init()
pygame.mixer.init()

# ignore warnings
GPIO.setwarnings(False)

#LEDs
GPIO.setup(26, GPIO.OUT, initial=GPIO.LOW) # sets LEDs low
GPIO.setup(20, GPIO.OUT, initial=GPIO.LOW)

# referring to pins by the "Broadcom SOC channel" number
GPIO.setmode(GPIO.BCM)

#PIR sensor initialization
GPIO.setup(17, GPIO.IN)
Motion = -1 # initializes motion variable to zero

def RCtime (RCpin): # function for photoresistor
    reading = 0
    GPIO.setup(RCpin, GPIO.OUT)
    GPIO.output(RCpin, GPIO.LOW)
    time.sleep(0.1) # gap between readings
    
    GPIO.setup(RCpin, GPIO.IN)
    # takes about 1 millisecond per loop cycle
    while (GPIO.input(RCpin) == GPIO.LOW):
        reading += 1
    return reading

while Motion == -1:
    if GPIO.input(17): # if the PIR Sensor input is high
        print("Motion Detected...")
        pygame.mixer.music.load(file2) # load .mp3 file
        pygame.mixer.music.play() # play loaded.mp3 file
        Motion = 1 # sets motion to high
    else:
        print("No motion")
    time.sleep(0.1) # gap between readings
    
while Motion == 1: # while motion has been detected
    print (RCtime(16)) # read value of photoresistor
    if ((RCtime(16)) < 10000): # if the photoresistor detects light
        pygame.mixer.music.load(file) # load .mp3 file
        pygame.mixer.music.play() # play loaded .mp3 file
        while True:
            GPIO.output(26, GPIO.HIGH) # LEDs flash
            GPIO.output(20, GPIO.HIGH)
            time.sleep(0.5)
            GPIO.output(26, GPIO.LOW) # LEDs flash
            GPIO.output(20, GPIO.LOW)
            time.sleep(0.5)
