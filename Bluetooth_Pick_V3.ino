#include <BleKeyboard.h>
#include "WiiChuck.h"
#include "Wire.h"
#define SDA_PIN 21  // Define your chosen SDA pin
#define SCL_PIN 22  // Define your chosen SCL pin

// Create an instance of the keyboard class
BleKeyboard bleKeyboard;
// Create an instance of the WiiChuck class
Accessory nunchuck1;

// Threshold for detecting a downward swing
const int swingThreshold = 240;
int prevAccelY = 0;
const int joyThreshold = 55;
bool z_button_state = false;
bool attack_button_state = false;
unsigned long lastZPressTime = 0;    // Time of the last Z button press
const unsigned long debounceDelay = 300;  // Debounce delay in milliseconds
unsigned long lastCPressTime = 0;    // Time of the last C button press
const unsigned long debounceCDelay = 300;  // Debounce delay for C button press
unsigned long lastSwingTime = 0;    // Time of the last swing
const unsigned long swingDelay = 50;  // Debounce delay in milliseconds
unsigned long attackPressTime = 0;    // Time of the last attack button press
const unsigned long debounceAttack = 300;  // Debounce delay for attack button in milliseconds
const uint8_t SPACE_BIR = 0x20;

void setup() {
  Serial.begin(9600);
  Serial.println("Starting BLE work!");
  bleKeyboard.begin();
  Wire.begin(SDA_PIN, SCL_PIN);  // Initialize I2C with custom pins
  Wire.setClock(100000);  // Set I2C clock speed to 50 kHz
  Serial.println("I2C Initialized with custom pins");  
  nunchuck1.begin();
  Serial.println("Nunchuck initialized");
}

void loop() {
  nunchuck1.readData();  // Read inputs and update the Nunchuk values

  // Read joystick X and Y values
  int joyX = nunchuck1.getJoyX();
  int joyY = nunchuck1.getJoyY();

  // Print joystick values for debugging
  Serial.print("JoyX: ");
  Serial.print(joyX);
  Serial.print("\tJoyY: ");
  Serial.println(joyY);

  detectJoystickMovement(joyX, joyY);
  
  // Read accelerometer Y-axis
  int accelY = nunchuck1.values[2]; // Accelerometer Y-axis value

  // Print accelerometer Y-axis value for debugging
  Serial.print("AccelY: ");
  Serial.println(accelY);

  // Detect downward swing motion
  detectDownwardSwing(accelY);

  prevAccelY = accelY;

  // Read the Z button
  z_detection();
  
  // Detect attack button press
  attack_detection();
  
  // Detect C button press with debounce
  c_detection();

  delay(10);
}

// Function to detect downward swing motion
void detectDownwardSwing(int accelY) {
  int deltaY = prevAccelY - accelY;

  // If the change in Y-axis exceeds the swing threshold, consider it a downward swing with debounce
  if ((deltaY > swingThreshold) && ((millis() - lastSwingTime) > swingDelay)) {
    Serial.println("SWING AND A MISS!");
    bleKeyboard.write(SPACE_BIR);  // SPACE_BIR key will be pressed once
    lastSwingTime = millis();
  }
}

void detectJoystickMovement(int joyX, int joyY) {
  if (joyX < 130 - joyThreshold) {
    Serial.println("Joystick moved RIGHT");
    bleKeyboard.release('a');
    bleKeyboard.press('d');  // 'd' key will be held down
  } else if (joyX > 130 + joyThreshold) {
    Serial.println("Joystick moved LEFT");
    bleKeyboard.release('d');
    bleKeyboard.press('a');  // 'a' key will be held down
  } else {
    // Ensure to not release keys unintentionally, only release when a new direction is pressed.
    if (joyX < 130 + joyThreshold && joyX > 130 - joyThreshold) {
      bleKeyboard.release('d');
      bleKeyboard.release('a');
    }
  }

  if (joyY < 128 - joyThreshold) {
    Serial.println("Joystick moved UP");
    bleKeyboard.release('s'); 
    bleKeyboard.press('w');  // 'w' key will be held down
  } else if (joyY > 128 + joyThreshold) {
    Serial.println("Joystick moved DOWN");
    bleKeyboard.release('w');
    bleKeyboard.press('s');  // 's' key will be held down
  } else {
    // Ensure to not release keys unintentionally, only release when a new direction is pressed.
    if (joyY < 128 + joyThreshold && joyY > 128 - joyThreshold) {
      bleKeyboard.release('s');
      bleKeyboard.release('w');
    }
  }
}

void z_detection() {
  z_button_state = nunchuck1.getButtonZ();
  // Check if the Z button is pressed and enough time has passed since the last press
  if (z_button_state && (millis() - lastZPressTime) > debounceDelay) {
    Serial.println("Z pressed");
    bleKeyboard.write('p');  // 'p' key will be pressed once
    lastZPressTime = millis();  // Update the last press time
  }
}
