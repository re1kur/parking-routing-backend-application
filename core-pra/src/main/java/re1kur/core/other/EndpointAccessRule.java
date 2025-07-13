package re1kur.core.other;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
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
