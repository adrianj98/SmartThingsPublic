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
			input "buttonDevice1", "capability.momentary", title: "Button1", multiple: false, required: true
	    
			input "buttonDevice2", "capability.momentary", title: "Button2", multiple: false, required: true
	 
     
			input "buttonDevice3", "capability.momentary", title: "Button3", multiple: false, required: true

  		  
        }
  
	 
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
   log.debug state.buttons
   def buttons = [buttonDevice1,buttonDevice2,buttonDevice3]
   request.JSON.id = request.JSON.id as Integer
   log.debug request.JSON.id
   buttons[ request.JSON.id].push()
   // if (state.buttons.findIndexOf { it ==  request.JSON.mac} < 0 ){
   //    state.buttons.push(request.JSON.mac);
  //  }
  //  log.debug state.buttons

}

 
 

// called when SmartApp is installed
def installed() {
    log.debug "Installed with settings: ${settings}"
    
    log.debug  state.buttons
}

// called when any preferences are changed in this SmartApp. 
def updated() {
    log.debug "Updated with settings: ${settings}"
    log.debug buttonDevice1
   // state.buttons = [buttonDevice1,buttonDevice2,buttonDevice3]
    log.debug "buttons: ${state.buttons}"
    unsubscribe()
}