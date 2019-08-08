import serial

while 1:
    ser = serial.Serial('COM8', 9600)
    dataSample = ser.readline()
    print(dataSample)
