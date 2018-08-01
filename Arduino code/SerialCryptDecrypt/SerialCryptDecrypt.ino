#include <Cape.h>

char key[] = "9LKSUR&#5A";    // Encryption key (keep private and safe)

char source[] = "HELLOWORLD"; // Source content to be crypted
char destination[11];         // Result buffer needs an additional byte for the initialization vector

Cape cape(key, 10);           // Insert secret key and its length

void setup() {
  Serial.begin(115200);
};

void loop() {
  char temp;
  Serial.println();

  // Set it as the encryption key
  cape.set_key(key, 10);

  unsigned long time = micros();
  cape.encrypt(source, destination, 10);
  time = micros() - time;
  // Destroy original content
  for(int i = 0; i < 10; i++) source[i] = 0;
  Serial.print("CRIPTED:  ");
  for(int i = 0; i < 10; i++)
    Serial.print(destination[i]);
  Serial.print(" Masked IV: ");
  Serial.print((uint8_t)destination[11], DEC);
  cape.decrypt(destination, source, 11);
  // Result is 1 byte longer to contain the added initialization vector
  Serial.print("ORIGINAL: ");
  for(int i = 0; i < 10; i++)
    Serial.print(source[i]);
  Serial.print("   Real IV: ");
  Serial.print((uint8_t)source[11], DEC);

  // Erase buffer
  for(int i = 0; i < 10; i++) destination[i] = 0;

  Serial.println();
  Serial.println();
  delay(250);
};
