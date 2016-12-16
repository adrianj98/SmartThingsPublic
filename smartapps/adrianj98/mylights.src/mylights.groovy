/**
 *  MyLights
 *
 *  Copyright 2016 Adrian Jones
 *
 */
include 'asynchttp_v1'

 
definition(
    name: "MyLights",
    namespace: "adrianj98",
    author: "Adrian Jones",
    description: "For your lights",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true) {
   
}


preferences {
	section("Title") {
		input  ( name : "deviceList", type: "capability.sensor",  title: "Select devices", multiple: true, required: true ) 
	
	}
}

mappings {
  path("/devices") {
    
    action: [
      GET: "getDevices"
    ]
  }
 path("/device/:deviceId/:command") {
    
    action: [
 
      PUT: "updateDevice",
      GET: "updateDevice"
    ]
  }
   path("/device") {
    
    action: [
      PUT: "updateDevice",
      GET: "updateDevice"
    ]
  }
}

def getDevices(){

  def result = []
  def x = [2,3]
  deviceList.each {device ->
    def attrs = device.supportedAttributes
    def at = []
    attrs.each {
     def value = device.currentValue(it.name)
     at.push([name : it.name,values :it.values,value : value, dataType : it.dataType])
    }
  def mySwitchCaps = device.capabilities

// log each capability supported by the "mySwitch" device, along
// with all its supported attributes
  def caps = []
  mySwitchCaps.each {cap ->
    def a = []
    cap.attributes.each {
     def value = device.currentValue(it.name)
     a.push([name : it.name,values :it.values,value : value, dataType : it.dataType])
    }

    def cmds = []
      
       cap.commands.each {cmd ->
        cmds.push([cmd.name,  cmd.arguments])
    }
    caps.push([attr:a,cmds : cmds,name:cap.name])
    
}
  result.push([name:device.name ,
  					displayName : device.displayName  ,
                    attr:at,
                    caps:caps,
                    id : device.id,
                    label : device.label])
  }
  log.debug result
  return result
}

def getDeviceById(deviceId){
      def result = 0
      deviceList.each {device ->
          if (device.id == deviceId){
           result = device
           }
      }
      return result
}

def updateDevice(){
    // use the built-in request object to get the command parameter
    
    def command = params.command
    def deviceId = params.deviceId
    def payload = request.JSON

    def device = getDeviceById(deviceId)
    log.debug device
 
    switch(command) {
        case "on":
            device.on()
            break
        case "off":
            device.off()
            break
            
        case "setLevel":
            log.debug "setLevel"
            device.setLevel(payload.value as Integer)
            break
        default:
            httpError(400, "$command is not a valid command for all switches specified")
    }
    return [result: "OK"]
}

def eventHandler(evt) {
    def value = 0
    try {
        value = evt.jsonValue
    } catch (e) {
        value = evt.value
    }
    
    
    def event = [id : evt.id , device_id :evt.deviceId, date : evt.date,
             value : value , desc : evt.description , text : evt.descriptionText , data :evt.data]
    
    def params = [
        uri: 'https://zw74i4drz6.execute-api.us-east-1.amazonaws.com',
        path: '/prod/events',
        body: [type: 'event', payload : event]
    ]
    log.debug event
    asynchttp_v1.post(processResponse, params)
}

def processResponse(response, data) {  }

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
     deviceList.each {device ->
        subscribe(device, "switch", eventHandler)
        subscribe(device, "temperature", eventHandler)
        subscribe(device, "motion", eventHandler)
        subscribe(device, "presence", eventHandler)
        
          
      }
}



 