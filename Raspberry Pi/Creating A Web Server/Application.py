#imports
from flask import request, Flask, render_template
import RPi.GPIO as GPIO
import NEWLiquidCrystalPi
import requests, time, cgi, cgitb, time

#use chips pin numbering system
GPIO.setmode(GPIO.BCM)

# initializes LCD screen
LCD = NEWLiquidCrystalPi.LCD(24, 25, 23, 17, 18, 22)

#set warnings to false
GPIO.setwarnings(False)

#LED
redNum = 16
blueNum = 20
GPIO.setup(redNum, GPIO.OUT) #red LED
GPIO.setup(blueNum, GPIO.OUT) #blue LED

#set pin 26 as an output
GPIO.setup(26, GPIO.IN, pull_up_down=GPIO.PUD_UP)

# initialize variables
myid = "de1e0183b5d5c25d23c9d7b1dd03609d"
url = "https://api.darksky.net/forecast/de1e0183b5d5c25d23c9d7b1dd03609d/43.397937,-79.819690?units=si" #gets Burlington, ON weather in celsius

app = Flask(__name__) # create Flask app

#function that gets pin status
@app.route('/pinStatus')
def pin_status():
    pinNum = request.args['pinNum']
    print(pinNum)
    if (pinNum == '16'):
        if GPIO.input(redNum) == 1:
            return 'LED Staus: ON'
        else:
            return 'LED Staus: OFF'
    elif (pinNum == '20'):
        if GPIO.input(20) == 1:
            return 'LED Staus: ON'
        else:
            return 'LED Staus: OFF'
    elif (pinNum == '26'):
        # if button is pressed
        if GPIO.input(26) == 0:
            my_weather = requests.get(url)
            my_weatherJSON = my_weather.json()
            temp = str(my_weatherJSON['currently']['temperature'])
            print(my_weatherJSON['currently']['temperature']) #prints weather
            LCD.begin(16,2)
            time.sleep(0.5)
            # prints to LCD screen
            LCD.write('Temp: ' + temp)
            return 'Button Status: Pressed       Current Temperature: ' + temp
        else:
            return 'Button Status: Not Pressed'
    else:
        return 'Unused Pin Number'


# function that posts request in organized JSON form
@app.route('/post', methods=['POST'])
def post():
    #requests data in json format
    req_data = request.get_json()
    
    #reads key:value pair
    redLED = req_data['redLED']
    blueLED = req_data['blueLED']
    
    #if user sets redLED on
    if (redLED == 'on'):
        GPIO.output(redNum, GPIO.HIGH)
    else:
        GPIO.output(redNum, GPIO.LOW)
    
    #if user sets blueLED on
    if (blueLED == 'on'):
        GPIO.output(blueNum, GPIO.HIGH)
    else:
        GPIO.output(blueNum, GPIO.LOW)
    
    #returns user input
    return '''
            The redLED is: {}
            The blueLED is: {}'''.format(redLED,blueLED)

# uses a form to get data from user
@app.route('/form', methods=['GET','POST']) # allow both GET and POST requests
def form():
    if request.method == 'POST':
        redLED = request.form.get('redFORM')
        blueLED = request.form.get('blueFORM')
        
        #if user sets redLED on
        if (redLED == 'on'):
            GPIO.output(redNum, GPIO.HIGH)
        else:
            GPIO.output(redNum, GPIO.LOW)
        #if user sets blueLED on
        if (blueLED == 'on'):
            GPIO.output(blueNum, GPIO.HIGH)
        else:
            GPIO.output(blueNum, GPIO.LOW)
    
    #clears form
    return '''<form method="POST">
                Red LED: <input type="text" name="redFORM"><br>
                Blue LED: <input type="text" name="blueFORM"><br>
                <input type="submit" value="Submit"><br>
            </form>'''

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000) # run app in debug on port 5000
