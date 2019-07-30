package com.newland.paas.paastool.etcdtool;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.options.GetOption;
import com.newland.paas.paastool.etcdtool.utils.SslUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;

import javax.net.ssl.SSLException;
import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EtcdClient {

    public static void main(String[] args) throws IOException {



        File key = new File("F:\\newland\\yjy\\tools\\etcdkeeper\\etcd\\etcd-key.pem");
        PrivateKey privateKey = SslUtil.toPrivateKeyFromPKCS1(key);

        File pem = new File("F:\\newland\\yjy\\tools\\etcdkeeper\\etcd\\etcd.pem");
        X509Certificate[] keyCertChain;
        try {
            keyCertChain = SslUtil.toX509Certificates(pem);
        } catch (Exception e) {
            throw new IllegalArgumentException("Input stream not contain valid certificates.", e);
        }

        Client client = null;
        try {
            SslProvider provider = OpenSsl.isAlpnSupported() ? SslProvider.OPENSSL : SslProvider.JDK;
            //SslProvider provider = SslProvider.OPENSSL ;
            SslContext sslContext = SslContextBuilder.forClient()
                    .enableOcsp(false)
                    .keyManager(privateKey, keyCertChain)
                    .sslProvider(provider)
                    .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .applicationProtocolConfig(new ApplicationProtocolConfig(
                            ApplicationProtocolConfig.Protocol.ALPN,
                            // NO_ADVERTISE is currently the only mode supported by both OpenSsl and JDK providers.
                            ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                            // ACCEPT is currently the only mode supported by both OpenSsl and JDK providers.
                            ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                            ApplicationProtocolNames.HTTP_2))
                    .build();
            client = Client.builder().sslContext(sslContext).endpoints("https://192.168.11.63:12379").build();
            client.getKVClient().put(ByteSequence.fromString("/traefik"), ByteSequence.fromString("ca")).get();;
            getWithKey(client,"/traefik");
        }catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (null != client) {
                client.close();
            }
        }
    }

    public static void getWithKey(Client client,String prefix) throws ExecutionException, InterruptedException {
        GetOption getOption = GetOption.newBuilder().withPrefix(ByteSequence.fromString(prefix)).build();

         List<KeyValue> rsts=client.getKVClient().get(ByteSequence.fromString(prefix), getOption).get().getKvs();
        rsts.size();
    }
}

