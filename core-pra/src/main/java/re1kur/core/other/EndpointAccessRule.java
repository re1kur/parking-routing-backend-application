package re1kur.core.other;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EndpointAccessRule {
    private final String methodName;
    private final List<String> roles = new ArrayList<>();

    public EndpointAccessRule(String methodName) {
        this.methodName = methodName;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
}
