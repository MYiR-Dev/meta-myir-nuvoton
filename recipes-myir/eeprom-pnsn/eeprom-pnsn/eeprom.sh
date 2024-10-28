#!/bin/sh

eeprom_test -d /dev/i2c-1 -a 0x50 -s 0x00 -r 48 > /home/root/eeprom.txt
eeprom_test -d /dev/i2c-1 -a 0x50 -s 0x40 -r 48 >> /home/root/eeprom.txt

echo ">>>PN=`cat /home/root/eeprom.txt | grep PN | awk -F : '{print $3}'`" > /dev/ttyS0
echo
echo ">>>SN=`cat /home/root/eeprom.txt | grep SN | awk -F : '{print $3}'`" > /dev/ttyS0
echo
#echo "numaker-som-ma35d16a81 login: root (super user)"

rm /home/root/eeprom.txt
