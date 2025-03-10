#ifndef Accessory_h
#define Accessory_h

#include "Arduino.h"
#include "Nunchuck.h"

#if defined(ARDUINO_ARCH_ESP32)
#define TWCR 0 // needs to be fixed
#else
#endif

#define WII_I2C_ADDR 0x52

#define WII_BYTE0 0
#define WII_BYTE1 1
#define WII_BYTE2 2
#define WII_BYTE3 3
#define WII_BYTE4 4
#define WII_BYTE5 5
#define WII_UNUSED 255

#define WII_BIT0 0
#define WII_BIT1 1
#define WII_BIT2 2
#define WII_BIT3 3
#define WII_BIT4 4
#define WII_BIT5 5
#define WII_BIT6 6
#define WII_BIT7 7

#define WII_VALUES_ARRAY_SIZE 19
#define dataArraySize 6

typedef enum _controllertype {
    Unknown,
    NUNCHUCK,
} ControllerType;

class Accessory : public Nunchuck { // Corrected inheritance
public:
    Accessory();
    void reset();
    ControllerType type;

    uint8_t* getDataArray();
    void setDataArray(uint8_t data[6]);

    void printInputs(Stream& stream = Serial);

    void begin();
    boolean readData();

    void enableEncryption(bool enc);

    void addMultiplexer(uint8_t iic, uint8_t sw);
    void switchMultiplexer();
    static void switchMultiplexer(uint8_t iic, uint8_t sw);

    int decodeInt(uint8_t msbbyte, uint8_t msbstart, uint8_t msbend,
                  uint8_t csbbyte, uint8_t csbstart, uint8_t csbend, uint8_t lsbbyte,
                  uint8_t lsbstart, uint8_t lsbend);

    int decodeInt(uint8_t mmsbbyte, uint8_t mmsbstart, uint8_t mmsbend,
                  uint8_t msbbyte, uint8_t msbstart, uint8_t msbend, uint8_t csbbyte,
                  uint8_t csbstart, uint8_t csbend, uint8_t lsbbyte, uint8_t lsbstart,
                  uint8_t lsbend);

    bool decodeBit(uint8_t byte, uint8_t bit, bool activeLow);

    void printMaps(Stream& stream = Serial);
    uint8_t getMapCount();
    void removeMaps();
    void removeMap(uint8_t id);
    ControllerType getControllerType();
    static int smap(int16_t val, int16_t aMax, int16_t aMid, int16_t aMin,
                    int16_t sMax, int16_t sZero, int16_t sMin);

    uint8_t values[WII_VALUES_ARRAY_SIZE];
    uint8_t* getValues();

    // Classic Functions
    int getJoyXLeft();
    int getJoyXRight();
    int getJoyYLeft();
    int getJoyYRight();

    // Nunchuck functions
    void printInputsNunchuck(Stream& stream = Serial);
    void getValuesNunchuck(uint8_t* values);

    int getJoyX();
    int getJoyY();

    float getRollAngle();
    float getPitchAngle();
    int getAccelX();
    int getAccelY();
    int getAccelZ();

    boolean getButtonC();
    boolean getButtonZ();

protected:
    bool _encrypted;
    uint8_t _dataarrayTMP[dataArraySize];
    uint8_t _dataarrayReadConsec[dataArraySize];
    uint8_t _dataarray[dataArraySize];
    uint8_t _multiplexI2C = 0;
    uint8_t _multiplexSwitch;

    uint8_t _key_table_1[16] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    uint8_t decryptByte(uint8_t byte, uint8_t address);
    void initBytes();

    ControllerType identifyController();
    boolean _burstRead(uint8_t addr = 0);
    void _writeRegister(uint8_t reg, uint8_t value);
    void _burstWriteWithAddress(uint8_t addr, uint8_t* arr, uint8_t size);

private:
    static void sendMultiSwitch(uint8_t iic, uint8_t sw);

    uint8_t mapCount;
};

#endif
