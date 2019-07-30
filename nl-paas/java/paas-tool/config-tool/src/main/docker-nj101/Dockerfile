FROM 172.32.150.15/newland/nl.taas.jdk:1.8.201
RUN groupadd paasopt
RUN useradd -g paasopt -m paasopt -u 2000
RUN mkdir /usr/local/nldata
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ADD config-tool*.jar /usr/local/nldata
ADD start.sh /usr/local/nldata
ADD .boss_auth_file.cfg /etc
RUN chown -R paasopt:paasopt /usr/local/nldata
USER paasopt
ENTRYPOINT ["sh","/usr/local/nldata/start.sh"]
EXPOSE 8080