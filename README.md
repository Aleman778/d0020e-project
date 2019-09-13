# D0020E - Software Function Reuse
Software Function Reuse demonstration using Arrowhead Framework and ROS with LiDAR sensor.
This was our project in the course D0020E Project in Computer Science and Engineering. This project was not successful, It is not working properly. Due to issues with the Arrowhead Framework not working and the given LiDAR sensor not responding we were not able to finish in time. I do not recommend that you work with this implementation, you can use it as reference but it is better to start from scratch.

# Ubuntu with ROS library
Start by setting up two separate systems with Ubuntu 16.04 and ROS library, install manually or get your pre-installed
(Raspberry Pi) image at:

https://downloads.ubiquityrobotics.com/pi.html

ROS installation manual can be found at:

https://wiki.ros.org/

# LiDAR and SICK software
SICK software for the LiDAR sensor can be found at:

https://www.sick.com/se/sv/sopas-engineering-tool-2018/p/p367244

**NOTE:** It's recommended to set a static IP address on the LiDAR sensor using SICK software. Further, a network switch 
connecting the computers and sensor together can provide a smoother development.

# Arrowhead Framework
## 1. Build Arrowhead Debian Packages
To setup your Arrowhead Framework follow steps 1. through 3. at:

https://github.com/arrowhead-f/core-java/blob/master/documentation/Debian%20Packages/DEBIAN-INSTALL.md

**NOTE:** The installation process will show prompts asking for input parameters. Certificate passwords need to be at
least 6 character long!

## 2. Clone repo and go to folder
Installation guide for Arrowhead Framework (Java) can be found at:

https://github.com/arrowhead-f/core-java/blob/master/documentation/Debian%20Packages/Debian%20Install%20-%20Cutted.pdf

This process is recommended on your regular development machine,
### Linux
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
### MacOS
 ```sh
git clone --depth=1 https://github.com/arrowhead-f/core.java.git -b master
```
 ```sh
brew install maven
```
 ```sh
cd core-java && mvn package
```
 ```sh
cd scripts && bash start_insecure_coresystems.sh
```
## ... and when you have had enough - purge it from your system
 ```sh
apt purge arrowhead-\*
```
## Troubleshooting
Mvn package solution, if "apt-get install ca-certificates-java" doesn't work, run

 ```sh
update-ca-certificates -f
```
Solution found at:

https://stackoverflow.com/questions/4764611/java-security-invalidalgorithmparameterexception-the-trustanchors-parameter-mus#se

The command for installing java JRE 11 might install JRE 10 instead. 
If it does that do this to get the correct version:
 ```sh
sudo add-apt-repository ppa:openjdk-r/ppa \
&& sudo apt-get update -q \
&& sudo apt install -y openjdk-11-jdk
```
Solution found at:

https://stackoverflow.com/questions/52504825/how-to-install-jdk-11-under-ubuntu
