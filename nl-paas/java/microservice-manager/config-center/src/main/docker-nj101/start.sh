mkdir -p $LOG_PATH/$HOSTNAME
java -jar -Duser.timezone=GMT+08 /usr/local/nldata/config-center*.jar \
    --spring.profiles.active=jdbc \
    --config.profiles=$SPRING_PROFILES_ACTIVE \
    $JVM_OPTS \
    -XX:OnOutOfMemoryError="kill -9 %p" \
    -XX:OnError="jstack -F %p >$LOG_PATH/$HOSTNAME/ErrorDump.log"
