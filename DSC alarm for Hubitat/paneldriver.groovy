/**
 *  Hubitat Device Handler: DSC Alarm Panel
 *
 *  Original Author: redloro@gmail.com, updated for Hubitat by bubba@bubba.org
 *  Modified and forked by github.com/ManuRiver from the work of github.com/Welasco/NodeAlarmV2,
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
  definition (name: "DSC Alarm Panel", namespace: "manuriver", author: "manuriver@river.org") {
    capability "PushableButton"
    capability "Alarm"
    capability "Sensor"
    capability "Actuator"
    capability "Refresh"
      
    //command "partition"
    command "armStay"
    command "armAway"
  //command "armInstant"
    command "disarm"
   // command "trigger1"
   // command "trigger2"
    command "chime"
    command "panic"
    command "alarmSetDate"
  //  command "bypass"
    
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"partition", type: "generic", width: 6, height: 4) {
      tileAttribute ("device.dscpartition", key: "PRIMARY_CONTROL") {
        attributeState "ready", label: 'Ready', icon:"st.Home.home2"
        attributeState "notready", label: 'Not Ready', backgroundColor: "#ffcc00", icon:"st.Home.home2"
        attributeState "arming", label: 'Arming', backgroundColor: "#ffcc00", icon:"st.Home.home3"
        attributeState "armedstay", label: 'Armed Stay', backgroundColor: "#79b821", icon:"st.Home.home3"
        attributeState "armedaway", label: 'Armed Away', backgroundColor: "#79b821", icon:"st.Home.home3"
        attributeState "armedinstant", label: 'Armed Instant Stay', backgroundColor: "#79b821", icon:"st.Home.home3"
        attributeState "armedmax", label: 'Armed Instant Away', backgroundColor: "#79b821", icon:"st.Home.home3"
        attributeState "alarmcleared", label: 'Alarm in Memory', backgroundColor: "#ffcc00", icon:"st.Home.home2"
        attributeState "alarm", label: 'Alarm', backgroundColor: "#ff0000", icon:"st.Home.home3"
      }
      tileAttribute ("panelStatus", key: "SECONDARY_CONTROL") {
        attributeState "panelStatus", label:'${currentValue}'
      }
    }

    standardTile("armAwayButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      state "default", label: 'Away', action: "armAway", icon: "st.security.alarm.on", backgroundColor: "#79b821"
    }

    standardTile("armStayButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      state "default", label: 'Stay', action: "armStay", icon: "st.security.alarm.on", backgroundColor: "#79b821"
    }

    //standardTile("armInstantButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      //state "default", label: 'Instant', action: "armInstant", icon: "st.security.alarm.on", backgroundColor: "#79b821"
    //}

    standardTile("disarmButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      state "default", label: 'Disarm', action: "disarm", icon: "st.security.alarm.off", backgroundColor: "#C0C0C0"
    }

    //standardTile("trigger1Button","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      //state "default", label: 'Trigger 1', action: "trigger1", icon: "st.Home.home30"
    //}

    //standardTile("trigger2Button","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      //state "default", label: 'Trigger 2', action: "trigger2", icon: "st.Home.home30"
    //}

    //standardTile("chimeButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
    //  state "default", label: 'Chime', action: "chime", icon: "st.custom.sonos.unmuted"
    //}

    //standardTile("bypassButton","device.button", width: 2, height: 2, canChangeIcon: true, decoration: "flat") {
      //state "default", label: 'Bypass', action: "bypass", icon: "st.locks.lock.unlocked"
    //}

    standardTile("chime", "device.chime", width:1, height: 1, canChangeIcon: false, canChangeBackground: false) {
                        state "chimeOff", label:'Chime', action:'chimeToggle', icon:"st.secondary.off", backgroundColor: "#ffffff"
                        state "chimeOn", label:'', action:'chimeToggle', icon:"st.secondary.beep", backgroundColor: "#ffffff"
      
    } 
    standardTile("refresh", "device.refresh", inactiveLabel: false, width: 1, height: 1, canChangeIcon: false, canChangeBackground: false) {
                        state "default", action:"refresh", icon:"st.secondary.refresh"  
    }     
      
    standardTile("alarmsetdate", "device.alarmsetdate", width: 1, height: 1, canChangeIcon: false, canChangeBackground: true) {
                        state "alarmsetdate", label:'DateTime', action:"alarmsetdate", icon:"st.Office.office6"
                }  
      
      main "partition"

    details(["partition",
             "armAwayButton", "armStayButton", "disarmButton", "chime", "bypassButton","refresh","alarmsetdate"])
  }

 // preferences {
   // input name: "bypassZones", type: "text", title: "Bypass Zones", description: "Comma delimited list of zones to bypass", required: false
  //}
}

    
    
def partition(String state, String alpha) {

if ( state.substring(0, 2) == "RD" ) {
            if (state.substring(3) == "0") {
                //parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm notready")
                sendEvent(name: "alarmStatus", value: "notready")
                // When status is "Not Ready" we cannot arm
                sendEvent(name: "awaySwitch", value: "off")
                sendEvent(name: "staySwitch", value: "off")
                sendEvent(name: "contact", value: "open")
            }
            else {
                //parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm ready")
                //parent.updateAlarmSystemStatus("ready")
                sendEvent(name: "alarmStatus", value: "ready")
                // When status is "Ready" we can arm
                sendEvent(name: "awaySwitch", value: "off")
                sendEvent(name: "staySwitch", value: "off")
                sendEvent(name: "switch", value: "off")
                sendEvent(name: "panic", value: "off")
                sendEvent(name: "contact", value: "open")
                sendEvent(name: "systemStatus", value: "System Status:No events")
            }
        // Process arm update
        } else if ( state.substring(0, 2) == "AR" ) {
            if (state.substring(3) == "0") {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm disarmed")
               // parent.updateAlarmSystemStatus("ready")
                sendEvent(name: "alarmStatus", value: "disarmed") 
                sendEvent(name: "awaySwitch", value: "off")
                sendEvent(name: "staySwitch", value: "off")
                sendEvent(name: "switch", value: "off")
                sendEvent(name: "contact", value: "open")
            }
            else if (state.substring(3) == "1") {
                if (state.substring(5) == "0") {
                 //   parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Away")
                  //  parent.updateAlarmSystemStatus("armedaway")
                    sendEvent(name: "alarmStatus", value: "away")
                    sendEvent(name: "awaySwitch", value: "on")
                    sendEvent(name: "staySwitch", value: "off")
                    sendEvent(name: "switch", value: "on")
                    sendEvent(name: "contact", value: "closed")
                }
                
                else if (state.substring(5) == "2") {
                   // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Stay")
                   // parent.updateAlarmSystemStatus("armedstay")
                    sendEvent(name: "alarmStatus", value: "stay")
                    sendEvent(name: "awaySwitch", value: "off")
                    sendEvent(name: "staySwitch", value: "on")
                    sendEvent(name: "switch", value: "on")
                    sendEvent(name: "contact", value: "closed")
                }
            }
            else if (state.substring(3) == "2") {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Arming")
               // parent.updateAlarmSystemStatus("arming")
                sendEvent(name: "alarmStatus", value: "arming")
                sendEvent(name: "awaySwitch", value: "off")
                sendEvent(name: "staySwitch", value: "off")
                sendEvent(name: "switch", value: "on")
            }
        } else if ( state.substring(0, 2) == "SY" ) {
         // Process various system statuses
            if ( state.substring(3, 6) == "658")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Keypad Lockout")
                sendEvent(name: "systemStatus", value: "System Status\nKeypad Lockout")
            }
            else if ( state.substring(3, 6) == "670")  {
              //  parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Invalid Access Code")
                sendEvent(name: "systemStatus", value: "System Status\nInvalid Access Code")
            }
            else if ( state.substring(3, 6) == "672")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Failed to Arm")
                sendEvent(name: "systemStatus", value: "System Status\nFailed to arm")
            }
            else if ( state.substring(3, 6) == "802")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Panel AC Trouble")
                sendEvent(name: "systemStatus", value: "System Status\nPanel AC Trouble")
            }
            else if ( state.substring(3, 6) == "803")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Panel AC Trouble Rest")
                sendEvent(name: "systemStatus", value: "System Status\nPanel AC Trouble Rest")
            }
            else if ( state.substring(3, 6) == "806")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status System Bell Trouble")
                sendEvent(name: "systemStatus", value: "System Status\nSystem Bell Trouble")
            }
            else if ( state.substring(3, 6) == "807")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status System Bell Trouble Rest")
                sendEvent(name: "systemStatus", value: "System Status\nSystem Bell Trouble Rest")
            }
            else if ( state.substring(3, 6) == "810")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status TLM line 1 Trouble")
                sendEvent(name: "systemStatus", value: "System Status\nTLM line 1 Trouble")
            }
            else if ( state.substring(3, 6) == "811")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status TLM line 1 Trouble Rest")
                sendEvent(name: "systemStatus", value: "System Status\nTLM line 1 Trouble Rest")
            }
            else if ( state.substring(3, 6) == "812")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status TLM line 2 Trouble")
                sendEvent(name: "systemStatus", value: "System Status\nTLM line 2 Trouble")
            }
            else if ( state.substring(3, 6) == "813")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status TLM line 2 Trouble Rest")
                sendEvent(name: "systemStatus", value: "System Status\nTLM line 2 Trouble Rest")
            }
            else if ( state.substring(3, 6) == "821")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Low Battery")
                sendEvent(name: "systemStatus", value: "System Status\nLow Battery")
            }
            else if ( state.substring(3, 6) == "822")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Low Battery Rest")
                sendEvent(name: "systemStatus", value: "System Status\nLow Battery Rest")

            }
            else if ( state.substring(3, 6) == "829")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Sytem Tamper")
                sendEvent(name: "systemStatus", value: "System Status\nSystem Tamper")
            }
            else if ( state.substring(3, 6) == "830")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Sytem Tamper Rest")
                sendEvent(name: "systemStatus", value: "System Status\nSystem Tamper Rest")
            }
            else if ( state.substring(3, 6) == "840")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Trouble Status (LCD)")
                sendEvent(name: "systemStatus", value: "System Status\nTrouble Status(LCD)")
            }
            else if ( state.substring(3, 6) == "841")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Trouble Status (LCD) Rest")
                sendEvent(name: "systemStatus", value: "System Status\nTrouble Status Rest")

            }
            else if ( state.substring(3, 6) == "896")  {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Keybus fault")
                sendEvent(name: "systemStatus", value: "System Status\nKeybus fault")
            }
            else if ( state.substring(3, 6) == "897")  {
               /// parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System Status Keybus fault Rest")
                sendEvent(name: "systemStatus", value: "System Status\nKeybus Fault Rest")
            }
         
        // Process alarm update
        } else if ( state.substring(0, 2) == "AL" ) {
            if (state.substring(3) == "1") {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm System AL")
                sendEvent(name: "alarmStatus", value: "alarm")
            }
        // Process chime update
        } else if ( state.substring(0, 2) == "CH" ) {
            if (state.substring(3) == "1") {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Chime On")
                sendEvent(name: "chime", value: "chimeOn")
            } else {
               // parent.writeLog("DSCAlarmSmartAppV2 AlarmPanel Device Type - Parse msg - Alarm Chime Off")
                sendEvent(name: "chime", value: "chimeOff")
            }    
        }


  sendEvent (name: "DSC panel status", value: "${state}", descriptionText: "${alpha}")
 // sendEvent (name: "panelStatus", value: "${alpha}", displayed: false)
}


def armAway() {
  parent.sendCommandPlugin('/alarmArmAway');
}

def armStay() {
  parent.sendCommandPlugin('/alarmArmStay');
}

//def armInstant() {
  //parent.sendCommandPlugin('/armInstant');
//}

def disarm() {
  parent.sendCommandPlugin('/alarmDisarm');
}

//def trigger1() {
  //parent.sendCommandPlugin('/trigger/17');
//}

//def trigger2() {
  //parent.sendCommandPlugin('/trigger/18');
//}

def chime() {
  parent.sendCommandPlugin('/alarmChimeToggle');
}

def panic() {
parent.sendCommandPlugin('/alarmPanic');
}


def alarmSetDate() {
parent.sendCommandPlugin('/alarmSetdate');
}    


//def bypass() {
  //parent.sendCommandPlugin('/bypass/'+settings.bypassZones);
//}
def refresh() {
    
    parent.sendCommandPlugin('/alarmUpdate');
}    

def parse(description) {
	parent.ifDebug('DSC Alarm Panel: ' + description)
	// send parent app any LAN communications sent to the Partition. 
	parent.lanResponseHandler(description)
}

// Implement "switch" (turn alarm on/off)
def on() {
    armAway()
}

def off() {
    disarm()
}

def away() {
    armAway()
}

def strobe() {
    panic()
}

def siren() {
    panic()
} 

def both() {
    panic()
}