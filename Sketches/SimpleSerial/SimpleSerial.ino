
void setup() {
  Serial.begin(9600);
  
  Serial1.begin(9600); //Set BLE baud rate to default
  Serial1.print("AT+CLEAR"); //clear all previous settings
  Serial1.print("AT+ROLE0"); //set the Grove as a BLE slave
  Serial1.print("AT+SAVE1");  //don't save the connection settings
}

char recvChar;

void loop() {
  if(Serial.available())
    {
       recvChar = Serial.read();
       Serial1.print(recvChar);
    }
    
    if(Serial1.available())
    {
       recvChar = Serial1.read();
       Serial.print(recvChar);
    }
 }
