package fr.wadjetz.http.server;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HttpConfig {
    private String path;
    private Map<String, String> config = new HashMap<>();

    public HttpConfig(String path) {
        this.path = path;
    }

    public HttpConfig load() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    int intex = line.indexOf(":");

                    String key = line.substring(0, intex).trim();
                    String value = line.substring(intex + 1).trim();

                    config.put(key, value);
                }
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
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
}
