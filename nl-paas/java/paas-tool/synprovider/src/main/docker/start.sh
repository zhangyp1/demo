java -jar -Duser.timezone=GMT+08 /usr/local/nldata/synprovider*.jar  \
    -frwId $frwId \
    -traefik.endpoints $traefikEndpoints \
    -eureka.endpoints $eurekaEndpoints \
    -etcd.endpoints $etcdEndpoints \
    -etcd.key $etcdKeyPem \
    -etcd.crt $etcdCrtPem \
