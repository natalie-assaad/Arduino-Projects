# imports
from flask_socketio import SocketIO, emit
from flask import Flask, render_template, url_for, copy_current_request_context
from gpiozero import Button, MotionSensor
import RPi.GPIO as GPIO
import pygame, time, threading, requests

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
app.config['DEBUG'] = True

# turn the flask app into a socketio app
socketio = SocketIO(app)
GPIO.setwarnings(False)

# initialize LEDs
GPIO.setup(21, GPIO.OUT, initial=GPIO.HIGH)
GPIO.setup(26, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(13, GPIO.OUT, initial=GPIO.LOW)

# initialize boolean variables
armed = False
motion = False

# initialize PIR Sensor
GPIO.setup(20, GPIO.IN) #PIR sensor initialization
GPIO.setup(16, GPIO.IN)
##GPIO.setup()
pir1 = MotionSensor(20)
pir2 = MotionSensor(16)


# when the client enters the correct IP Address and Port number, they will be connected to   # this website
@app.route('/')
def index():
    #only by sending this page first will the client be connected to the socketio instance
    return render_template('Index.html')

# this method handles input taken from the site and sets boolean armed as well as which     # LEDs are on depending on whether the switch was clicked or not.
@socketio.on('switch')
def led_function(data):
    global armed
    if (data%2 == 0): # if switch is turned off
        GPIO.output(21, GPIO.HIGH) # Blue LED on, signaling user that the room is not armed
        GPIO.output(26, GPIO.LOW)
        armed = False # set global boolean armed to false
    else: # switch is turned on
        GPIO.output(21, GPIO.LOW)
        GPIO.output(26, GPIO.HIGH) # Red LED on, signaling user that the room is armed
        armed = True # set global boolean armed to true


# this method waits for motion and, if motion is detected, it sets the boolean variable motion # to true and calls the intruder function, passing the boolean motion in the parameters
def detectMotion():
    motion = True
    intruder(Motion)
    
# this method executes a POST request only if motion and armed are true
def intruder(motion):
    if motion:
        if armed:
            GPIO.output(13, GPIO.HIGH) # turn yellow LED on to indicate disturbance
            r = requests.post('https://maker.ifttt.com/trigger/motion_detected/with/key/b6AvuLhNZVdm19s0r8vwDm') # makes POST request
            print(r) # prints POST request status


# uses gpiozero to detect motion without the use of a while loop
pir1.when_motion = detectMotion
pir2.when_motion = detectMotion

if __name__ == '__main__':
    socketio.run(app, host = '0.0.0.0')