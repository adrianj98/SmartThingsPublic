/**
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
 *  Momentary Button Tile
 *
 *  Author: SmartThings
 *
 *  Date: 2013-05-01
 */
metadata {
	definition (name: "Momentary Button Tile", namespace: "smartthings", author: "SmartThings") {
		capability "Actuator"
		capability "Momentary"
		capability "Button"
		capability "Sensor"
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.button", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Push', action: "momentary.push", backgroundColor: "#ffffff", nextState: "on"
			state "pushed", label: 'Push', action: "momentary.push", backgroundColor: "#53a7c0"
		}
		main "button"
		details "button"
	}
}

def parse(String description) {
}

def push() {
    log.trace "pushed"
	//sendEvent(name: "switch", value: "on", isStateChange: true, display: false)
//	sendEvent(name: "switch", value: "off", isStateChange: true, display: false)/
	//sendEvent(name: "button", value: "pushed", isStateChange: true)
    
   	sendEvent(name: "button", value: "pushed", data: [buttonNumber: 1], descriptionText: "Pushed button $button was pushed", isStateChange: true)
	
}

def on() {
	log.trace "on"
	push()
}

def off() {
	log.trace "off"
	push()
}
