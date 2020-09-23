#include <Servo.h>

Servo servo1; //Waist servo
Servo servo2; //Shoulder Servo
Servo servo3; //Elbow Servo
Servo servo4; //Wrist roll servo
Servo servo5; //Wrist Vertical Motion Servo
Servo servo6; //Grip Servo

int speedDelay = 20;
String recData;

void setup() {
  servo1.attach(6); 
  servo2.attach(7);
  servo3.attach(8);
  servo4.attach(5);      
  servo5.attach(9);
  servo6.attach(10);
  
  Serial.begin(9600);

  // Set the initial positions of the servos; Allows the usage of Servo.read()
  positionReset();
}

void loop() {
  if (Serial.available() > 0) {
    recData = Serial.readString();
    String data2 = recData.substring(1);  //Angle or the menu choice
    if (recData.startsWith("1")){ //Servo 1 (waist) motion
      moveServo(servo1, data2.toInt());
    }
    else if (recData.startsWith("2")){  //Servo 2 (shoulder) motion
      moveServo(servo2, data2.toInt());
    }
    else if (recData.startsWith("3")){  //Servo 3 (elbow) motion
      moveServo(servo3, data2.toInt());
    }
    else if (recData.startsWith("4")){  //Servo 4 (Wrist roll) motion
      moveServo(servo4, data2.toInt());
    }
    else if (recData.startsWith("5")){  //Servo 5 (Wrist Vert) motion
      moveServo(servo5, data2.toInt());
    }
    else if (recData.startsWith("6")){  //Servo 6 (Grip) motion
      moveServo(servo6, data2.toInt());
    }
    else if (recData.startsWith("M")) { //One of the pre-saved motions
      motionChoice(data2.toInt());
    }
    Serial.println(recData);
  } 
}

void moveServo(Servo s, int newPos) {
  int currPos = s.read();
  if (currPos > newPos) {
    for (int j = currPos; j >= newPos; j--) {
      s.write(j);
      delay(speedDelay);
    }
  }
  else if (currPos < newPos) {
    for (int j = currPos; j <= newPos; j++) {
      s.write(j);
      delay(speedDelay);
    }
  }
}

void motionChoice(int choice) {
  switch(choice) {
    case 1: //Return to original position
      positionReset();
    case 2: //Pick up item
      pickup();
    case 3: //Drop Item
      drop();
    case 4: //Toss item
      toss();
    default:
      break;
  }
}

//Reset servos to original positions
void positionReset() {
  servo1.write(90);
  servo2.write(135);
  servo3.write(45);
  servo4.write(90);
  servo5.write(90);
  servo6.write(85);
}

//Control servos to pickup an item
void pickup() {
  
}

//Control servos to drop an item
void drop() {

}

//Control servos to toss an item
void toss() {
  
}
