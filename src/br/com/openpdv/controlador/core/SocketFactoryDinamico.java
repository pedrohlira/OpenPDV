package br.com.openpdv.controlador.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.log4j.Logger;

public class SocketFactoryDinamico implements ProtocolSocketFactory {

    private Logger log;
    private SSLContext ssl = null;
    private X509Certificate certificate;
    private PrivateKey privateKey;
    private String fileCacerts;

    public SocketFactoryDinamico(X509Certificate certificate, PrivateKey privateKey, String fileCacerts) {
        log = Logger.getLogger(SocketFactoryDinamico.class);
        this.certificate = certificate;
        this.privateKey = privateKey;
        this.fileCacerts = fileCacerts;
    }

    private SSLContext getSSLContext() {
        if (ssl == null) {
            try {
                KeyManager[] keyManagers = createKeyManagers();
                TrustManager[] trustManagers = createTrustManagers();
                ssl = SSLContext.getInstance("TLS");
                ssl.init(keyManagers, trustManagers, null);
            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | KeyManagementException ex) {
                log.error("Erro no certificado", ex);
            }
        }
        return ssl;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        }

        Socket socket = socketfactory.createSocket();
        SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
        SocketAddress remoteaddr = new InetSocketAddress(host, port);
        socket.bind(localaddr);
        socket.connect(remoteaddr, timeout);

        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public KeyManager[] createKeyManagers() {
        HSKeyManager keyManager = new HSKeyManager(certificate, privateKey);
        return new KeyManager[]{keyManager};
    }

    public TrustManager[] createTrustManagers() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore trustStore = KeyStore.getInstance("JKS");

        trustStore.load(new FileInputStream(fileCacerts), "changeit".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    private class HSKeyManager implements X509KeyManager {

        private X509Certificate certificate;
        private PrivateKey privateKey;

        public HSKeyManager(X509Certificate certificate, PrivateKey privateKey) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        @Override
        public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
            return certificate.getIssuerDN().getName();
        }

        @Override
        public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
            return null;
        }

        @Override
        public X509Certificate[] getCertificateChain(String arg0) {
            return new X509Certificate[]{certificate};
        }

        @Override
        public String[] getClientAliases(String arg0, Principal[] arg1) {
            return new String[]{certificate.getIssuerDN().getName()};
        }

        @Override
        public PrivateKey getPrivateKey(String arg0) {
            return privateKey;
        }

        @Override
        public String[] getServerAliases(String arg0, Principal[] arg1) {
            return null;
        }
    }
}
