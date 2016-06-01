package fr.wadjetz.http.server;

import fr.wadjetz.http.load.balancer.Group;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HttpConfig {
    private String path;
    private Map<String, String> config = new HashMap<>();

    public HttpConfig(String path) {
        this.path = path;
    }

    public HttpConfig load() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("conf.properties"));
        for (String key : properties.stringPropertyNames()) {
            config.put(key, properties.getProperty(key));
        }
        return this;
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(config.get(key));
    }

    public List<VHost> getVHosts() {
        List<VHost> vhosts = new ArrayList<>();

        int i = 1;
        while(true) {
            final int intdex = i;

            Optional<VHost> maybeVhost = getString("vhost" + intdex)
                    .flatMap(vhost -> getString("vhost" + intdex + "Root")
                            .map(vhostRoot -> new VHost(vhost, vhostRoot)));

            if (maybeVhost.isPresent()) {
                vhosts.add(maybeVhost.get());
            } else {
                break;
            }
            i++;
        }

        return vhosts;
    }

    public List<Group> getGroups() {
        String[] groups = config.get("groups").split(",");
        return Arrays.stream(groups).map(s -> s.trim()).map(group -> {
            String name = config.get(group + ".name");
            List<String> members = Arrays.stream(config.get(group + ".members").split(","))
                    .map(s -> s.trim())
                    .collect(Collectors.toList());
            String lbMethod = config.get(group + ".lb.method");
            String domain = config.get(group + ".domain");
            return new Group(name, members, lbMethod, domain);
        }).collect(Collectors.toList());
    }
}
