package com.newland.paas.paastool.etcdtool.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SslUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static X509Certificate[] toX509Certificates(InputStream in) throws CertificateException {
        ByteBuf[] certs = readCertificates(in);
        return toX509Certificates(certs);
    }

    public static X509Certificate[] toX509Certificates(File file) throws CertificateException {
        ByteBuf[] certs = readCertificates(file);
        return toX509Certificates(certs);
    }
    public static X509Certificate[] toX509Certificates(String certs) throws CertificateException {
        return toX509Certificates(readCertificatesFromStr(certs));
    }
    public static X509Certificate[] toX509Certificates(ByteBuf[] certs) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate[] x509Certs = new X509Certificate[certs.length];

        int i = 0;
        try {
            for (; i < certs.length; i++) {
                InputStream is = new ByteBufInputStream(certs[i], true);
                try {
                    x509Certs[i] = (X509Certificate) cf.generateCertificate(is);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // This is not expected to happen, but re-throw in case it does.
                        throw new RuntimeException(e);
                    }
                }
            }
        } finally {
            for (; i < certs.length; i++) {
                certs[i].release();
            }
        }
        return x509Certs;
    }
    public static PrivateKey toPrivateKeyFromPKCS1(InputStream in) {
        Reader reader = new BufferedReader(new InputStreamReader(in));
        return toPrivateKeyFromPKCS1(reader);
    }

    public static PrivateKey toPrivateKeyFromPKCS1(File key) {
        try {
            Reader reader = new FileReader(key);
            return toPrivateKeyFromPKCS1(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey toPrivateKeyFromPKCS1(Reader key) {
        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(key);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            Object object = pemParser.readObject();
            KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
            return kp.getPrivate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    /**
     * cert file pattern: cert header + base64 text + cert footer
     */
    private static final Pattern CERT_PATTERN = Pattern.compile(
            "-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+" +
                    "([a-z0-9+/=\\r\\n]+)" +
                    "-+END\\s+.*CERTIFICATE[^-]*-+",
            Pattern.CASE_INSENSITIVE);
    /**
     * key file pattern: key header + base64 text + key footer
     */
    private static final Pattern KEY_PATTERN = Pattern.compile(
            "-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+" +
                    "([a-z0-9+/=\\r\\n]+)" +
                    "-+END\\s+.*PRIVATE\\s+KEY[^-]*-+",
            Pattern.CASE_INSENSITIVE);

    static ByteBuf[] readCertificates(File file) throws CertificateException {
        try {
            InputStream in = new FileInputStream(file);

            try {
                return readCertificates(in);
            } finally {
                safeClose(in);
            }
        } catch (FileNotFoundException e) {
            throw new CertificateException("could not find certificate file: " + file);
        }
    }
    static ByteBuf[] readCertificatesFromStr(String content)throws CertificateException{
        List<ByteBuf> certs = new ArrayList<ByteBuf>();
        Matcher m = CERT_PATTERN.matcher(content);
        int start = 0;
        for (; ; ) {
            if (!m.find(start)) {
                break;
            }

            ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
            ByteBuf der = Base64.decode(base64);
            base64.release();
            certs.add(der);

            start = m.end();
        }

        if (certs.isEmpty()) {
            throw new CertificateException("found no certificates in input stream");
        }

        return certs.toArray(new ByteBuf[certs.size()]);
    }

    static ByteBuf[] readCertificates(InputStream in) throws CertificateException {
        String content;
        try {
            content = readContent(in);
        } catch (IOException e) {
            throw new CertificateException("failed to read certificate input stream", e);
        }
        return readCertificatesFromStr(content);
    }
    private static String readContent(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[8192];
            for (; ; ) {
                int ret = in.read(buf);
                if (ret < 0) {
                    break;
                }
                out.write(buf, 0, ret);
            }
            return out.toString(CharsetUtil.US_ASCII.name());
        } finally {
            safeClose(out);
        }
    }

    private static void safeClose(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
        }
    }

    private static void safeClose(OutputStream out) {
        try {
            out.close();
        } catch (IOException e) {
        }
    }

    private SslUtil() {
    }
}

