pkill -9 homepisec-sensor -f
nohup java -Dlogging.level.org.homepisec=DEBUG -jar ./homepisec-sensor-0.0.1-SNAPSHOT.jar >sensor.out &
