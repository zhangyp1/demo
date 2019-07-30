mkdir -p $LOG_PATH/$HOSTNAME
eurakaserver=$SPRING_PROFILES_ACTIVE
java -jar -Duser.timezone=GMT+08 /usr/local/nldata/eureka-server*.jar --spring.profiles.active=$eurakaserver \
    $JVM_OPTS \
    -XX:OnOutOfMemoryError="kill -9 %p" \
    -XX:OnError="jstack -F %p >$LOG_PATH/$HOSTNAME/ErrorDump.log"