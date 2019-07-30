package com.newland.paas.paastool.etcdtool.utils;

import java.util.*;
import java.util.concurrent.ExecutionException;

import com.coreos.jetcd.data.ByteSequence;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paastool.etcdtool.EtcdConnection;
import com.newland.paas.paastool.etcdtool.ToolDict;
import com.newland.paas.paastool.etcdtool.error.EtcdError;
import com.newland.paas.paastool.vo.PathNode;
import com.newland.paas.paastool.vo.Provider;
import com.newland.paas.paastool.vo.backend.Backend;
import com.newland.paas.paastool.vo.backend.CircuitBreaker;
import com.newland.paas.paastool.vo.backend.LoadBalancer;
import com.newland.paas.paastool.vo.backend.MaxConn;
import com.newland.paas.paastool.vo.backend.Server;
import com.newland.paas.paastool.vo.frontend.Frontend;
import com.newland.paas.paastool.vo.frontend.Route;

/**
 * Created with IntelliJ IDEA. Description: User: luolifeng Date: 2018-12-24 Time: 17:15
 */
public class ProviderOper {

    private static final Log log = LogFactory.getLogger(ProviderOper.class);
    EtcdConnection connection;

    public ProviderOper(EtcdConnection connection) {
        this.connection = connection;
    }

    public Provider getEtcdKey2Provider(String prefix) throws ExecutionException, InterruptedException {
        Provider provider = new Provider();
        provider.setBackends(getBackends(prefix));
        provider.setFrontends(getFrontends(prefix));
        return provider;
    }

    private Map<String, Backend> getBackends(String traefikPrefix) throws ExecutionException, InterruptedException {
        String prefix = traefikPrefix + "/backends/";
        Map<String, String> kvs = connection.getWithPrefix(prefix);
        Map<String, Backend> backends = new HashMap<>();

        for (String key : kvs.keySet()) {
            String[] keySplits = key.replace(prefix, "").split(ToolDict.PATH_SLASH);

            if (keySplits.length < 3) {
                continue;
            }
            String backendName = keySplits[0];
            Backend backend = null == backends.get(backendName) ? new Backend() : backends.get(backendName);
            backend.setName(keySplits[0]);
            // circuitbreaker
            if ("circuitbreaker".equals(keySplits[1]) && "expression".equals(keySplits[2])) {
                CircuitBreaker circuitBreaker = new CircuitBreaker();
                circuitBreaker.setExpression(kvs.get(key));
                backend.setCircuitBreaker(circuitBreaker);
            }
            // maxConn
            if ("maxConn".equals(keySplits[1])
                && ("amount".equals(keySplits[2]) || "extractorfunc".equals(keySplits[2]))) {
                MaxConn maxConn = backend.getMaxConn() == null ? new MaxConn() : backend.getMaxConn();
                if ("amount".equals(keySplits[2])) {
                    maxConn.setAmount(Integer.parseInt(kvs.get(key)));

                } else {
                    maxConn.setExtractorFunc(kvs.get(key));
                }
                backend.setMaxConn(maxConn);
            }
            // servers
            if ("servers".equals(keySplits[1]) && keySplits.length == 4
                && ("weight".equals(keySplits[3]) || "url".equals(keySplits[3]))) {
                Map<String, Server> servers = null != backend.getServers() ? backend.getServers() : new HashMap<>();
                Server server = null == servers.get(keySplits[2]) ? new Server() : servers.get(keySplits[2]);
                server.setName(keySplits[2]);
                if ("weight".equals(keySplits[3])) {
                    server.setWeight(Integer.parseInt(kvs.get(key)));
                }
                if ("url".equals(keySplits[3])) {
                    server.setUrl(kvs.get(key));
                }
                servers.put(keySplits[2], server);
                backend.setServers(servers);
            }
            // loadBalancer
            if ("loadBalancer".equals(keySplits[1]) && "method".equals(keySplits[2])) {
                LoadBalancer loadBalancer =
                    backend.getLoadBalancer() == null ? new LoadBalancer() : backend.getLoadBalancer();
                loadBalancer.setMethod(kvs.get(key));
                backend.setLoadBalancer(loadBalancer);
            }
            backends.put(backend.getName(), backend);

        }
        return backends;
    }

    private Map<String, Frontend> getFrontends(String traefikPrefix) throws ExecutionException, InterruptedException {
        String prefix = traefikPrefix + "/frontends/";
        Map<String, String> kvs = connection.getWithPrefix(prefix);
        Map<String, Frontend> frontends = new HashMap<>();

        for (String key : kvs.keySet()) {
            String[] keySplits = key.replace(prefix, "").split(ToolDict.PATH_SLASH);
            if (keySplits.length < 2) {
                continue;
            }
            String frontendName = keySplits[0];
            Frontend frontend = null == frontends.get(frontendName) ? new Frontend() : frontends.get(frontendName);
            frontend.setName(keySplits[0]);
            // backend
            if ("backend".equals(keySplits[1])) {
                frontend.setBackend(kvs.get(key));
            }
            // priority
            if ("priority".equals(keySplits[1])) {
                frontend.setPriority(Integer.parseInt(kvs.get(key)));
            }
            // routes
            if ("routes".equals(keySplits[1]) && keySplits.length == 4 && "rule".equals(keySplits[3])) {
                Map<String, Route> routes = null != frontend.getRoutes() ? frontend.getRoutes() : new HashMap<>();
                Route route = null == routes.get(keySplits[2]) ? new Route() : routes.get(keySplits[2]);
                route.setName(keySplits[2]);
                route.setRule(kvs.get(key));
                routes.put(keySplits[2], route);
                frontend.setRoutes(routes);
            }
            // entryPoints
            if ("entryPoints".equals(keySplits[1])) {
                String entryPoints = kvs.get(key);
                if (entryPoints != null) {
                    frontend.setEntryPoints(new ArrayList<>(Arrays.asList(entryPoints.split(ToolDict.SPLIT))));
                }
            }

            frontends.put(frontend.getName(), frontend);
        }
        return frontends;

    }


    private PathNode createFrontendsNode(Map<String, Frontend> frontendsMap) {

        if (frontendsMap == null || frontendsMap.isEmpty()) {
            return null;
        }
        PathNode frontends = new PathNode("frontends");
        for (String key : frontendsMap.keySet()) {
            if (key == null || frontendsMap.get(key) == null) {
                continue;
            }
            Frontend frontend = frontendsMap.get(key);
            PathNode frontendNode = new PathNode(key);
            frontends.getSubNodes().add(frontendNode);

            frontendNode.getSubNodes().add(new PathNode("priority", frontend.getPriority().toString()));
            frontendNode.getSubNodes().add(new PathNode("backend", frontend.getBackend()));

            if (frontend.getEntryPoints() != null && frontend.getEntryPoints().size() > 0) {
                String entrypoints = ",";
                for (String ep : frontend.getEntryPoints()) {
                    entrypoints = entrypoints + ep + ToolDict.SPLIT;
                }

                frontendNode.getSubNodes()
                    .add(new PathNode("entrypoints", entrypoints.substring(1, entrypoints.length() - 1)));
            }

            Map<String, Route> routes = frontend.getRoutes();
            if (routes != null && !routes.isEmpty()) {
                PathNode routesNode = new PathNode("routes");
                frontendNode.getSubNodes().add(routesNode);
                for (String routeKey : routes.keySet()) {
                    if (routeKey == null || routes.get(routeKey) == null) {
                        continue;
                    }
                    Route route = routes.get(routeKey);
                    PathNode routeNode = new PathNode(routeKey);
                    routesNode.getSubNodes().add(routeNode);
                    routeNode.getSubNodes().add(new PathNode("rule", route.getRule()));

                }
            }
        }

        return frontends;

    }

    private PathNode createBackendsNode(Map<String, Backend> backendsMap) {

        if (backendsMap == null || backendsMap.isEmpty()) {
            return null;
        }
        // backends
        PathNode backendsNode = new PathNode("backends");
        for (String key : backendsMap.keySet()) {
            if (key == null || backendsMap.get(key) == null) {
                continue;
            }
            // backend-x
            Backend backend = backendsMap.get(key);
            PathNode backendNode = new PathNode(key);
            backendsNode.getSubNodes().add(backendNode);

            // servers
            Map<String, Server> servers = backend.getServers();
            if (servers != null && !servers.isEmpty()) {
                PathNode serversNode = new PathNode("servers");
                backendNode.getSubNodes().add(serversNode);
                for (Iterator<String> iterator = servers.keySet().iterator(); iterator.hasNext(); ) {
                    String serverKey = iterator.next();
                    if (serverKey != null && servers.get(serverKey) != null) {
                        Server server = servers.get(serverKey);
                        PathNode serverNode = new PathNode(serverKey);
                        serversNode.getSubNodes().add(serverNode);
                        serverNode.getSubNodes().add(new PathNode("weight", server.getWeight().toString()));
                        serverNode.getSubNodes()
                                .add(new PathNode("url", server.getUrl() == null ? "http://127.0.0.1" : server.getUrl()));
                    }
                }
            }

            // loadBalancer
            LoadBalancer loadBalancer = backend.getLoadBalancer();
            if (loadBalancer != null) {
                PathNode lbNode = new PathNode("loadbalancer");
                backendNode.getSubNodes().add(lbNode);
                lbNode.getSubNodes()
                    .add(new PathNode("method", loadBalancer.getMethod() == null ? "wrr" : loadBalancer.getMethod()));

            }

            // maxConn
            MaxConn maxConnr = backend.getMaxConn();
            if (maxConnr != null) {
                PathNode maxConnrNode = new PathNode("maxconn");
                backendNode.getSubNodes().add(maxConnrNode);
                maxConnrNode.getSubNodes().add(new PathNode("extractorfunc", maxConnr.getExtractorFunc()));
                maxConnrNode.getSubNodes().add(new PathNode("amount", maxConnr.getAmount().toString()));
            }

            // circuitBreaker
            CircuitBreaker circuitBreaker = backend.getCircuitBreaker();
            if (circuitBreaker != null) {
                PathNode circuitBreakerNode = new PathNode("circuitbreaker");
                backendNode.getSubNodes().add(circuitBreakerNode);
                circuitBreakerNode.getSubNodes().add(new PathNode("expression", circuitBreaker.getExpression()));
            }
        }

        return backendsNode;

    }

    private PathNode createProviderNode(Provider provider, String prefix) {

        PathNode root = new PathNode(prefix);
        Map<String, Frontend> frontendsMap = provider.getFrontends();
        Map<String, Backend> backendsMap = provider.getBackends();
        PathNode fePathNode=createFrontendsNode(frontendsMap);

        if(fePathNode!=null){
            root.getSubNodes().add(fePathNode);
        }
        PathNode bkPathNode=createBackendsNode(backendsMap);
        if(bkPathNode!=null){
            root.getSubNodes().add(bkPathNode);
        }

        return root;
    }

    private void pathNode2Map(PathNode node, String key, HashMap<String, String> kv) {
        String newKey = key;
        if (node == null || node.getName() == null) {
            return;
        }
        newKey = newKey + node.getName();
        if (node.getValue() != null) {
            kv.put(newKey, node.getValue());
        }
        // if (newKey.split("/").length > 50) {
        // throw new Exception("健值深度过大！");
        //
        // }
        for (PathNode subNode : node.getSubNodes()) {
            pathNode2Map(subNode, newKey, kv);
        }

    }

    private void pathNode2Map(PathNode node, HashMap<String, String> kv) {
        pathNode2Map(node, "", kv);

    }

    private void putProvider(Provider provider, String traefikPrefix) throws ExecutionException, InterruptedException {

        PathNode node = createProviderNode(provider, traefikPrefix);
        HashMap<String, String> kvMap = new LinkedHashMap<>();
        pathNode2Map(node, kvMap);
        for (Iterator<String> iterator = kvMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            connection.putEtcdKey(key, kvMap.get(key));
        }

    }

    public Provider getProviderByFrwId(Long frwId) throws ExecutionException, InterruptedException, EtcdError {
        String alias = getAliasKey(frwId);
        String aliasValue = connection.get(alias);
        if (aliasValue == null) {
            throw new EtcdError(alias + "：键值不存在！");
        }
        return getEtcdKey2Provider(aliasValue);
    }

    public String getAliasKey(Long frwId) {
        return getFrwIdDir(frwId) + "/alias";
    }

    private String getFrwIdDir(Long frwId) {
        return "/paas-framework-traefik/" + frwId;
    }

    private String getTraefikConfKey(Long frwId) {
        return getFrwIdDir(frwId) + "/conf";
    }

    public void initTraefikConf(Long frwId) throws ExecutionException, InterruptedException {
        if(connection.get(this.getAliasKey(frwId))==null){
            connection.putEtcdKey(this.getAliasKey(frwId), this.getTraefikConfKey(frwId) + "/0");
        }

    }

    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public void deleteProvider(Long frwId) throws ExecutionException, InterruptedException {
        String frwIdDir = getFrwIdDir(frwId);
        connection.deleteEtcdKeyWithPrefix(frwIdDir);
    }

    public void putProvider(Provider provider, Long frwId) throws ExecutionException, InterruptedException, EtcdError {
        if (provider == null) {
            throw new EtcdError("：provider不能为空！");
        }
        if (provider.getFrontends() == null && provider.getBackends()==null) {
            throw new EtcdError("：provider不能为空对象！");
        }
        String alias = getAliasKey(frwId);
        String traefikConf = getTraefikConfKey(frwId);
        ByteSequence shareKey = null;
        Integer version;

        String s = connection.get(alias);
        if (s == null) {
            throw new EtcdError(alias + "：键值不存在！");
        }
        try {
            shareKey = connection.lock(alias);
            s = connection.get(alias);
            String[] pathKeys = s.split(ToolDict.PATH_SLASH);
            if (pathKeys.length != 5) {
                throw new EtcdError(alias + "：值的层级不正确！");
            }
            version = (Integer.parseInt(pathKeys[4])) % 3 + 1;
            String newPrefix = traefikConf + ToolDict.PATH_SLASH + version;
            // 清理新版目录
            connection.deleteEtcdKeyWithPrefix(newPrefix);
            // 插入新版
            putProvider(provider, newPrefix);

            connection.putEtcdKey(alias, newPrefix);
        } finally {
            connection.unLock(shareKey);

        }

    }

}
