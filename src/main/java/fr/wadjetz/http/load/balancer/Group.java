package fr.wadjetz.http.load.balancer;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<String> members = new ArrayList<>();
    private String lbMethod = "round-robin";
    private String domain;

    public Group(String name, List<String> members, String lbMethod, String domain) {
        this.name = name;
        this.members = members;
        this.lbMethod = lbMethod;
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLbMethod() {
        return lbMethod;
    }

    public void setLbMethod(String lbMethod) {
        this.lbMethod = lbMethod;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", members=" + members +
                ", lbMethod='" + lbMethod + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
