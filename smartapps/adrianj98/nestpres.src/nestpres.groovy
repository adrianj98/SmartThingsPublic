/**
 *  NestPres
 *
 *  Copyright 2016 Adrian Jones
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
 
 include 'asynchttp_v1'
definition(
    name: "NestPres",
    namespace: "adrianj98",
    author: "Adrian Jones",
    description: "status for nest",
    category: "Mode Magic",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true)


preferences {
	section("Title") {
		// TODO: put inputs here
	}
}

def sendStatus(status){
 
    //PUT /structures/M-j7oSYR-m86xc6wpodvBqYKuBu8ASvD6NCyKoxD__1KIoouZ3OyYg HTTP/1.1 
    //Host: developer-api.nest.com
    //Authorization: Bearer c.zNyPGCX1U5TXnz9ujzpw16ot1wPWD4fUHTEPv6EoukyeetLOjHDkbW27BMutBBZ2Wwm9GyTZo3sbzGxM30BxMcEG9lv6aCtOwdkyp88tmc49NzzkzeSXDbIqF7dNbryuR2FsefBmh658z9V1
    //Content-Type: application/json

    def params = [
			uri: 'https://developer-api.nest.com',
			path: '/',
         	contentType: "application/json",
			query: [ "auth": 'c.zNyPGCX1U5TXnz9ujzpw16ot1wPWD4fUHTEPv6EoukyeetLOjHDkbW27BMutBBZ2Wwm9GyTZo3sbzGxM30BxMcEG9lv6aCtOwdkyp88tmc49NzzkzeSXDbIqF7dNbryuR2FsefBmh658z9V1' ],
			body:  ["away" : status]
		]
    log.debug params
   httpPutJson(params) { resp -> 
        log.debug resp?.status
        log.debug resp?.headers?.location
        params["path"] = '/structures/M-j7oSYR-m86xc6wpodvBqYKuBu8ASvD6NCyKoxD__1KIoouZ3OyYg'
        def l = resp?.headers?.Location?.split("/\\?")
        params["uri"] = l[0]
         log.debug params
          httpPutJson(params) { resp2 -> 
       			 log.debug resp2?.status
      		     log.debug resp2?.headers?.location
        }
        }
}

def processResponse(response, data){
    log.debug data
   log.debug "raw response: $response.errorData"
    def headers = response.headers
    headers.each {header, value ->
        log.debug "$header: $value"
    }
}

def modeChangeHandler(evt) {
    log.debug evt.value
    
    if (evt.value == "Away") {
       sendStatus("away")
    } else {
       sendStatus("home")
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    subscribe(location, "mode", modeChangeHandler)
    
	initialize()

}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}

// TODO: implement event handlers