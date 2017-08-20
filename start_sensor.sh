pkill -9 homepisec-sensor -f
SENSOR_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5002"
#SENSOR_OPTIONS=" $SENSOR_OPTIONS logging.level.org.homepisec=DEBUG"
nohup java $SENSOR_OPTIONS -jar ./homepisec-sensor-0.0.1-SNAPSHOT.jar >sensor.out &