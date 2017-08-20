pkill -9 homepisec-control -f
CONTROL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5001"
#CONTROL_OPTIONS=" $CONTROL_OPTIONS logging.level.org.homepisec=DEBUG"
nohup java $CONTROL_OPTIONS -jar ./homepisec-control-0.0.1-SNAPSHOT.jar >control.out &