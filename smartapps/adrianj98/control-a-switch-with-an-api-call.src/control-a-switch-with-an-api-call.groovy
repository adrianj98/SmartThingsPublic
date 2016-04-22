/**
 *  Control a Switch with an API call
 *
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Control a Switch with an API call",
    namespace: "adrianj98",
    author: "SmartThings",
    description: "V2 of 'RESTful Switch' example. Trying to make OAuth work properly.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true)

preferences {
    
    	section { 
	   input( name :"buttonDevice1", type :"capability.momentary", title: "Button1", multiple: false, required: true)	    
   
      input(name: "dash1", type: "enum", title: "Dash 1", options: dashButtons())
    input(name: "dash2", type: "enum", title: "Dash 2", options: dashButtons())
    input(name: "dash3", type: "enum", title: "Dash 3", options: dashButtons())
    input(name: "dash4", type: "enum", title: "Dash 4", options: dashButtons())
   
        }
  
	 
}

private dashButtons() {
   return ["f0:27:2d:7b:22:df", "qwerty"]
}



mappings {
  path("/switches") {
    // GET requests to /switches endpoint go to listSwitches.
    // PUT requests go to updateSwitches
    action: [
      GET: "getSwitches",
      PUT: "updateSwitches"
    ]
  }

}
 
// return a map in the form of [switchName, switchStatus]
// the returned value will be converted to JSON by the platform
def getSwitches() {
    
  /*   if (state == null)
        return []
     if   (state.buttons == null)
        state.buttons = []
    log.debug "listSwitches returning: $state.buttons" */
    return [] //state.buttons
}
 

// execute the command specified in the request
// returns a 400 error if a non-supported command
// is specified (only on, off, or toggle supported)
// assumes request body with JSON in format {"command" : "<value>"}
def updateSwitches() {
  
   def buttons = [dash1,dash2,dash3,dash4]
   log.debug buttons
   log.debug  request.JSON 
 
   log.debug  request.JSON.mac
 
   def buttonIndex = buttons.findIndexOf { it ==  request.JSON.mac}
    log.debug buttonIndex;
    if (buttonIndex > -1 ){
          buttonDevice1.push(buttonIndex + 1)

    }
     log.debug "index"

}

 
 

// called when SmartApp is installed
def installed() {
    log.debug "Installed with settings: ${settings}"
    
    log.debug  state.buttons
}

// called when any preferences are changed in this SmartApp. 
def updated() {

    unsubscribe()
}