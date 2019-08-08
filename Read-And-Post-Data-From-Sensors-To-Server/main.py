import serial
import requests
import time
import re
import math
import datetime

ser = serial.Serial('COM8', 9600)


# millis = int(round(time.time() * 1000))
# x = int(millis)
# localMilli = 20700000 + x
# localMilli = x
# animalId = '5d29ff5479ed0d22e0d25ac4'

# x = 'iCAT001s26h68t55p164g2740.90578N,08515.35083E'

def updateAnimal(jsonFromFunction):
    url = "https://animalmonitoringsystem.herokuapp.com/api/updateanimal/" + animalId
    info = 'updateAnimal'
    r = requests.put(url, json=jsonFromFunction)
    print(info, r.status_code, r.reason)


def postHeartBeat(jsonFromFunction):
    url = "https://animalmonitoringsystem.herokuapp.com/api/postheartbeat/" + animalId
    info = 'postHeartBeat'

    r = requests.post(url, json=jsonFromFunction)
    print(info, r.status_code, r.reason)


def postbodytempr(jsonFromFunction):
    url = "https://animalmonitoringsystem.herokuapp.com/api/postbodytempr/" + animalId
    info = 'postbodytempr'

    r = requests.post(url, json=jsonFromFunction)
    print(info, r.status_code, r.reason)


def postsurrtempr(jsonFromFunction):
    url = "https://animalmonitoringsystem.herokuapp.com/api/postsurrtempr/" + animalId
    info = 'postsurrtempr'

    r = requests.post(url, json=jsonFromFunction)
    print(info, r.status_code, r.reason)


def postgeolocation(jsonFromFunction):
    url = "https://animalmonitoringsystem.herokuapp.com/api/postgeolocation/" + animalId
    info = 'postgeolocation'

    r = requests.post(url, json=jsonFromFunction)
    print(info, r.status_code, r.reason)


def checkAnimalId(animalIdFromController):
    global animalId

    if animalIdFromController == 'CAT001':
        animalId = '5d29fe2e79ed0d22e0d25ac3'
    elif animalIdFromController == 'DOG001':
        animalId = '5d29ff5479ed0d22e0d25ac4'
    elif animalIdFromController == 'HUM001':
        animalId = '5d47055e9025e300172796d4'


def josnFileToUpload(animalId, bodyTempr, surrTempr, humidity, geoLocation, heartBeat):
    millis = int(round(time.time() * 1000))
    x = int(millis)
    # localMilli = 20700000 + x
    localMilli = x
    checkAnimalId(animalId)
    jsonUpdateAnimal = {
        "heart_beat": heartBeat,
        "body_tempr": bodyTempr,
        "surr_tempr": surrTempr,
        "geo_location": geoLocation,
        "timestramp": localMilli,
        "humidity": humidity
    }

    jsonPostHeartBeat = {
        "timestramp": localMilli,
        "heart_beat": heartBeat
    }

    jsonPostBodyTempr = {
        "timestramp": localMilli,
        "body_tempr": bodyTempr,
    }

    jsonPostSurrTempr = {
        "timestramp": localMilli,
        "humidity": humidity,
        "surr_tempr": surrTempr
    }

    jsonPostGeoLocation = {
        "timestramp": localMilli,
        "geo_location": geoLocation
    }

    updateAnimal(jsonUpdateAnimal)
    postbodytempr(jsonPostBodyTempr)
    postgeolocation(jsonPostGeoLocation)
    postHeartBeat(jsonPostHeartBeat)
    postsurrtempr(jsonPostSurrTempr)


# iCAT001s26h68t55p164g2740.90578N,08515.35083E
def receivedData(chunkdata):
    dataSample = chunkdata
    print(dataSample)
    animalId = re.search('i(.*)s', dataSample)
    animalId = animalId.group(1)

    bodytemperature = re.search('t(.*)p', dataSample)
    bodytemperature = bodytemperature.group(1)
    bodytemperature = int(bodytemperature)
    bodytemperature = bodytemperature / 2.048

    surroundingTemperature = re.search('s(.*)h', dataSample)
    surroundingTemperature = surroundingTemperature.group(1)
    surroundingTemperature = float(surroundingTemperature)

    humidity = re.search('h(.*)t', dataSample)
    humidity = humidity.group(1)
    humidity = int(humidity)
    # humidity = float(humidity)
    # humidity = float(humidity)
    pulseRate = re.search('p(.*)g', dataSample)
    pulseRate = pulseRate.group(1)
    pulseRate = int(pulseRate)

    latitude = re.search('g(.*)N,', dataSample)
    latitudeFloat = float(latitude.group(1)) / 100
    afterDecimal, beforeDecimal = math.modf(latitudeFloat)
    latitude = beforeDecimal + afterDecimal * 100 / 60

    longitude = re.search('N,(.*)E', dataSample)
    longitudeFloat = float(longitude.group(1)) / 100
    afterDecimal, beforeDecimal = math.modf(longitudeFloat)
    longitude = beforeDecimal + afterDecimal * 100 / 60
    geoLocation = [latitude, longitude]

    print("---------------------------------------Output-------------------------------------------------")
    # print("Data from Microcontroller: ", dataSample, type(dataSample))
    # print("animal id = ", animalId, type(animalId))
    # print("body temperature = ", bodytemperature, type(bodytemperature))
    # print("surroundingTemperature = ", surroundingTemperature, type(surroundingTemperature))
    # print("humidity = ", humidity, type(humidity))
    # print("pulseRate= ", pulseRate, type(pulseRate))
    # print("latitude=  ", latitude, type(latitude))
    # print("longitude= ", longitude, type(longitude))
    # print("geolocation= ", geoLocation, type(geoLocation))
    print("Data from Microcontroller: ", dataSample)
    print("animal id = ", animalId)
    print("body temperature = ", bodytemperature)
    print("surroundingTemperature = ", surroundingTemperature)
    print("humidity = ", humidity)
    print("pulseRate= ", pulseRate)
    print("latitude=  ", latitude)
    print("longitude= ", longitude)
    print("timestramp:", datetime.datetime.now())

    josnFileToUpload(animalId, bodytemperature, surroundingTemperature, humidity, geoLocation, pulseRate)


def serialData(chunkData):
    if chunkData[0] == 'i':
        receivedData(chunkData)


while 1:
    dataSample = ser.readline()
    result = str(dataSample)

    result = result.replace('b', '')
    result = result.replace('\'', '')
    result = result.replace('\\x00', '')
    result = result.replace('\\r\\n', '')

    print(result)
    if result[0] == 'i':
        serialData(result)
