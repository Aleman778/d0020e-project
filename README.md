# D0020E - Software function reuse
Software Function Reuse demonstration using Arrowhead Framework and ROS with LiDAR sensor.

# Ubuntu with ROS library
Start by setting up two separate systems with Ubuntu 16.04 and ROS library, install manually or get your preinstalled
image at:
https://downloads.ubiquityrobotics.com/pi.html

ROS installation manual can be found at:
https://wiki.ros.org/

# LiDAR and SICK software
Setting static IP adress for the LiDAR sensor using SICK software can provide a smoother development. 
Which can be found at:

https://www.sick.com/se/sv/sopas-engineering-tool-2018/p/p367244

**NOTE:** It's recommended to use a network switch connecting the computers and sensor together.

# Arrowhead Framework
Installation guide for Arrowhead Framework (Java) can be found at:
https://github.com/arrowhead-f/core-java/blob/master/documentation/Debian%20Packages/Debian%20Install%20-%20Cutted.pdf

## 1. Clone repo and go to folder
This process is recommended on your regular development machine,
 ```sh
git clone --depth=1 https://github.com/arrowhead-f/core.java.git -b master
```
 ```sh
cd core-java && mvn package
```
 ```sh
find . -name \*.deb
```
```sh
cd ./target
```
```sh
sudo dpkg -i arrowhead-*.deb
```
## 2. Build Arrowhead Debian Packages
Further follow the instructions at:
https://github.com/arrowhead-f/core-java/blob/master/documentation/Debian%20Packages/DEBIAN-INSTALL.md

**NOTE:** The installation process will show prompts asking for input parameters. Certificate passwords need to be at
least 6 character long!

## ... and when you had enough - purge it from your system
 ```sh
apt purge arrowhead-\*
```

## Tips and tricks
Mvn package solution, if "apt-get install ca-certificates-java" doesn't work, run

 ```sh
update-ca-certificates -f
```
Solution found at:
https://stackoverflow.com/questions/4764611/java-security-invalidalgorithmparameterexception-the-trustanchors-parameter-mus#se

