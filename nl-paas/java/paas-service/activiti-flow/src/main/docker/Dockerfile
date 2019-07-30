FROM 10.1.8.16:5000/nl.taas.jdk8:latest
RUN mkdir /usr/local/nldata
RUN mkdir -p /usr/local/java/jre/lib/fonts/fallback
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ADD activiti-flow*.jar /usr/local/nldata
ADD simsun.ttc /usr/local/java/jre/lib/fonts/fallback
ADD start.sh /usr/local/nldata
ADD .boss_auth_file.cfg /etc
ENTRYPOINT ["sh","/usr/local/nldata/start.sh"]
EXPOSE 8080