mkdir -p $LOG_PATH/$HOSTNAME
java -jar -Dfile.encoding=UTF-8 -Duser.timezone=GMT+08 /usr/local/nldata/activiti-flow*.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE \
    $JVM_OPTS \
    -XX:OnOutOfMemoryError="kill -9 %p" \
    -XX:OnError="jstack -F %p >$LOG_PATH/$HOSTNAME/ErrorDump.log"