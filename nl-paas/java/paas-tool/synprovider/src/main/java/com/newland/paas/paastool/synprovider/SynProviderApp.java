package com.newland.paas.paastool.synprovider;


import com.coreos.jetcd.data.ByteSequence;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paastool.etcdtool.EtcdConnection;
import com.newland.paas.paastool.etcdtool.impl.EtcdConImpl;
import com.newland.paas.paastool.etcdtool.utils.EurekaProvider;
import com.newland.paas.paastool.etcdtool.utils.ProviderOper;
import com.newland.paas.paastool.eureka.vo.EurekaAppInfos;
import com.newland.paas.paastool.vo.Provider;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;


/**
 * @author Administrator
 */
public class SynProviderApp {
    private static  Log log = LogFactory.getLogger(SynProviderApp.class);
    private static RestTemplate restTemplate = new RestTemplate();
    public static void main(String[] args)  {
        Args myArgs =new Args();
        CmdLineParser parserpub =null;
        try {
            parserpub = new CmdLineParser(myArgs);
            parserpub.parseArgument(args);
        }catch (CmdLineException e) {
            log.info(LogProperty.LOGTYPE_DETAIL, "Command line error: " + e.getMessage());
            System.out.println("Command line error: " + e.getMessage());
            showHelp(parserpub);
            System.exit(-99);
        }

        //etcd地址
        String etcdEndPoint=myArgs.etcdEndPoints;
        //etcd证书
        String etcdPrivateKey=myArgs.etcdKey ;
        //etcd密钥
        String etcdKeyCertChain=myArgs.etcdCrt;
        //框架ID
        Long frwId= Long.parseLong(myArgs.frwId);
        //traefik地址
        String traefikEndPoint=myArgs.traefikEndPoints;
        //注册中心地址
        String eurekaEndPoint=myArgs.eurekaEndPoints;
        ByteSequence shareKey=null;
        //ca证书
        String caFile=myArgs.caFile;
        EtcdConnection connection=new EtcdConImpl(etcdEndPoint,etcdPrivateKey,etcdKeyCertChain,caFile);
        try {
            ProviderOper providerOper = new ProviderOper(connection);
            String alias = providerOper.getAliasKey(frwId);
            providerOper.initTraefikConf(frwId);
            shareKey = connection.lock(alias);
            Provider etcdPro = getProviderFromTraefik("etcdv3", traefikEndPoint);
            Provider eurekaPro = getProviderFromRegCenter(eurekaEndPoint);

            if (eurekaPro != null) {
                Provider providerRst = EurekaProvider.sync(etcdPro, eurekaPro);
                providerOper.putProvider(providerRst, frwId);
            }
        }catch(Exception ep){
            log.info(LogProperty.LOGTYPE_DETAIL,ep.getMessage());

        }finally {

            try {
                if(shareKey!=null){
                    connection.unLock(shareKey);
                }
                connection.close();

            } catch (Exception e) {
                log.info(LogProperty.LOGTYPE_DETAIL,e.getMessage());
            }

        }
        System.exit(0);

    }

    private static void showHelp(CmdLineParser parser){
        System.out.println("useage:SynProviderApp  [arguments...]");
        parser.printUsage(System.out);
    }

    private static Provider getProviderFromTraefik(String providerName,String endPoints)  {
        String providerApi = "/api/providers/{provider}";
        log.debug(LogProperty.LOGTYPE_DETAIL, "调用网关访问点：", endPoints);
        String[] endPointss=endPoints.split(",");
        for(String endPoint :endPointss) {
            Provider providerObject =
                    restTemplate.getForObject(endPoint + providerApi, Provider.class, providerName);
            if(providerObject!=null){
                return providerObject;
            }
        }

        return new Provider();
    }
    private static Provider getProviderFromRegCenter(String endPoints)  {
        log.debug(LogProperty.LOGTYPE_DETAIL, "调用注册中心访问点：", endPoints);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Accept", "application/json");
        requestHeaders.add("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        String[] endPointss=endPoints.split(",");
        for(String endPoint :endPointss) {
            try{
                ResponseEntity<EurekaAppInfos> rst = restTemplate.exchange(endPoint+"/apps", HttpMethod.GET, requestEntity, EurekaAppInfos.class);
                EurekaAppInfos eurekaAppInfos= rst.getBody();
                return EurekaProvider.create(eurekaAppInfos.getApplications());}
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return new Provider();
    }
    private static class Args {
        @Option(name = "-frwId", usage = "framework’s Id", required = true)
        private String frwId;

        @Option(name = "-traefik.endpoints", usage = "traefik EndPoints", required = true)
        private String traefikEndPoints;
        @Option(name = "-eureka.endpoints", usage = "eureka EndPoints", required = true)
        private String eurekaEndPoints;
        @Option(name = "-etcd.endpoints", usage = "etcd EndPoints", required = true)
        private String etcdEndPoints;


        @Option(name = "-etcd.key", usage = "etcd PrivateKey file", required = true)
        private String etcdKey;
        @Option(name = "-etcd.crt", usage = "etcd KeyCertChain file", required = true)
        private String etcdCrt;
        @Option(name = "-etcd.ca", usage = "etcd CACertChain file" )
        private String caFile;


    }
}

