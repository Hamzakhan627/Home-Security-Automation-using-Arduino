#include <Servo.h>           // For motor control
#include <EEPROM.h>           // For storing admin settings

// Constants for pins
const int lockButtonPin = 2;
const int unlockButtonPin = 3;
const int irSensorPin = 4;
const int servoPin = 9;         // Servo motor pin
const int ledPinR = 7;          // RGB LED Red pin
const int ledPinG = 5;          // RGB LED Green pin
const int ledPinB = 6;          // RGB LED Blue pin
const int potPin = A0;          // Potentiometer for brightness control

// Variables
Servo doorMotor;
int systemState = 1;  // 0: locked, 1: unlocked (Initialize in unlocked state)
bool personDetected = false;
int lockPressCount = 0;
unsigned long lastLockPressTime = 0;
unsigned long doorOpenTime = 0;
bool doorOpen = false;

// Admin Menu Variables
int motorSpeed = 50;           // Default motor speed
int doorTiming = 3000;         // Default door open time (3 seconds)
int adminState = 0;            // Admin menu state
const String adminPassword = "1234";  // Admin password
bool adminLoggedIn = false;
bool exitAdminMenu = false;

// Setup
void setup() {
  Serial.begin(9600);
  pinMode(lockButtonPin, INPUT);
  pinMode(unlockButtonPin, INPUT);
  pinMode(irSensorPin, INPUT);

  // RGB LED pins
  pinMode(ledPinR, OUTPUT);
  pinMode(ledPinG, OUTPUT);
  pinMode(ledPinB, OUTPUT);

  // Initialize motor
  doorMotor.attach(servoPin);
  delay(100);  // Ensure servo is initialized
  doorMotor.write(0);  // Start with door closed

  // Initialize timer
  doorOpenTime = millis();

  Serial.println("System ready.");
  Serial.println("System initialized in UNLOCKED state.");
}

// Loop
void loop() {
  checkButtons();
  checkIRSensor();
  controlMotor();

  // Update the potentiometer value and brightness in real-time
  int brightness = readPotentiometer();  // Separate function to read potentiometer value
  updateStatusLight(brightness);         // Pass the brightness value to LED update function

  handleAdminMenu();  // Keep the admin menu at the end so it does not block updates
}

// Button Logic
void checkButtons() {
  if (digitalRead(lockButtonPin) == HIGH) {  // Button is pressed (HIGH state)
    if (millis() - lastLockPressTime > 300) {  // Debouncing
      lastLockPressTime = millis();
      lockPressCount++;
      if (lockPressCount >= 3) {
        systemState = 0;  // Force Lock
        lockPressCount = 0;
        Serial.println("Emergency locking activated.");
      }
    }
  }

  static unsigned long lastUnlockPressTime = 0;
  if (digitalRead(unlockButtonPin) == HIGH && millis() - lastUnlockPressTime > 300) {  // Button is pressed (HIGH state)
    lastUnlockPressTime = millis();  // Debouncing
    systemState = 1;  // Unlock
    Serial.println("System unlocked.");
  }
}

// IR Sensor Detection
void checkIRSensor() {
  personDetected = digitalRead(irSensorPin) == HIGH;  // Assuming IR sensor outputs HIGH when a person is detected

  if (personDetected && systemState == 1 && !doorOpen) {
    doorOpen = true;
    doorOpenTime = millis();
    Serial.println("Person detected. Door opening...");
  }
}

// Motor Control (Servo Motor)
void controlMotor() {
  if (doorOpen) {
    doorMotor.write(90);  // Open door fully
    delay(1000);  // Allow the servo time to move
    if (millis() - doorOpenTime > doorTiming) {
      doorOpen = false;
      doorMotor.write(0);  // Close door
      Serial.println("Door closing...");
    }
  }
}

// Status Light (RGB LED)
void updateStatusLight(int brightness) {
  if (doorOpen) {
    // Blinking red when the door moves
    int blinkState = (millis() / 500) % 2;
    analogWrite(ledPinR, blinkState ? brightness : 0);
    analogWrite(ledPinG, 0);
    analogWrite(ledPinB, 0);
  } else {
    // Green for unlocked, blue for locked
    if (systemState == 1) {
      analogWrite(ledPinR, 0);
      analogWrite(ledPinG, brightness);
      analogWrite(ledPinB, 0);
    } else {
      // Blinking blue when locked
      //int blinkState = (millis() / 500) % 2;
      analogWrite(ledPinR, 0);
      analogWrite(ledPinG, 0);
      analogWrite(ledPinB, brightness);
    }
  }
}

// Function to read the potentiometer value in real-time
int readPotentiometer() {
  return analogRead(potPin) / 4;  // Scale down the value for analogWrite (0-255 range)
}

// Admin Menu for Configurations
void handleAdminMenu() {
  if (Serial.available() > 0 && !adminLoggedIn) {  // Check for serial input only if not logged in
    String inputPassword = readSerialInput();
    if (inputPassword == adminPassword) {
      adminLoggedIn = true;
      Serial.println("Welcome to Admin Menu!");
    } else {
      Serial.println("Access denied.");
    }
  }

  if (adminLoggedIn && !exitAdminMenu) {
    // Admin is logged in, display menu
    Serial.println("Admin logged in. Modify settings:");
    motorSpeed = getNewSetting("Motor Speed (0-100):", motorSpeed);
    doorTiming = getNewSetting("Door Open Time (ms):", doorTiming);

    // Exit Option
    Serial.println("Enter 'exit' to log out of the Admin Menu.");
    String exitCommand = readSerialInput();
    if (exitCommand == "exit") {
      exitAdminMenu = true;
      Serial.println("Exiting Admin Menu...");
    }

    // Reset the admin state and exit
    adminLoggedIn = false;
    exitAdminMenu = false;  // Reset the exit flag for future use
  }
}

// Helper to read serial input
String readSerialInput() {
  String input = "";
  while (Serial.available() == 0) {}  // Wait for input
  input = Serial.readStringUntil('\n');
  input.trim();
  return input;
}

// Helper to get new setting value
int getNewSetting(String prompt, int currentValue) {
  Serial.print(prompt);
  Serial.print(" (Current: ");
  Serial.print(currentValue);
  Serial.println(")");
  String input = readSerialInput();
  int newValue = input.toInt();
  return (newValue > 0) ? newValue : currentValue;  // If invalid input, keep current
}
