#include <Servo.h>

#define co2sensor 8
#define servo 9

int incomingInt;
Servo servo1;
int led = 10;
int prevVal = LOW;
long th, tl, h, l, partPerMillion;

//для проектов с несколькими ардуино
char arduinoId = 49;//1
char arduinoServiceWord[3] = {56, 48, 0};//80
char co2sensorServiceWord[3] = {56, 32, 0};//8
char servoServiceWord[3] = {57, 32, 0};//9

void setup() {   
  Serial.begin(9600);
  servo1.attach(servo);    
}

void loop() {
        // send data only when you receive data:
        long tt = millis();
        int myVal = digitalRead(co2sensor);
      
        //Если обнаружили изменение
        if (myVal == HIGH) {
          //digitalWrite(LedPin, HIGH);
          if (myVal != prevVal) {
            h = tt;
            tl = h - l;
            prevVal = myVal;
          }
        }  else {
          //digitalWrite(LedPin, LOW);
          if (myVal != prevVal) {
            l = tt;
            th = l - h;
            prevVal = myVal;
            partPerMillion = 5000 * (th - 2) / (th + tl - 4);
            //Serial.println("PPM = " + String(partPerMillion));
          }
        }
        
        if (Serial.available() > 0) {
                //Serial.println(Serial.available());
                // read the incoming byte:
                incomingInt = Serial.read();// - '0';
                if(0<=incomingInt&&incomingInt<=180){//&&incomingInt!=48){
                  //Serial.print("Simple writing ");
                  //Serial.println(incomingInt); 
                  servo1.write(incomingInt);
                }
                else if(incomingInt==181){
                  for(int i=0;i<=180;i++){
                    servo1.write(i);
                    delay(100);
                  }
                }
                else if(incomingInt==183){
                  int angle = servo1.read();
                  Serial.print(servoServiceWord);
                  Serial.print(angle);
                }else if(incomingInt==184){
                  Serial.print(co2sensorServiceWord);
                  Serial.print(partPerMillion);              
                }else if(incomingInt==185){
                  Serial.print(arduinoServiceWord);
                  Serial.print(arduinoId);  
                }
        }
}
