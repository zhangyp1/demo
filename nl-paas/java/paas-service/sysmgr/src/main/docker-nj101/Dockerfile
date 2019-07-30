FROM 172.32.150.15/newland/nl.taas.jdk:1.8.201_update
RUN groupadd paasopt
RUN useradd -g paasopt -m paasopt -u 2000
RUN mkdir -p /usr/local/nldata/files
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ADD sysmgr*.jar /usr/local/nldata
ADD start.sh /usr/local/nldata
ADD .boss_auth_file.cfg /etc
RUN chown -R paasopt:paasopt /usr/local/nldata
RUN chown -R paasopt:paasopt /usr/local/nldata/files
USER paasopt
ENTRYPOINT ["sh","/usr/local/nldata/start.sh"]
EXPOSE 8080