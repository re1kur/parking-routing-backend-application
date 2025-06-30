package re1kur.authz.authz;

import lombok.Value;

import java.net.URI;

@Value
public class ParsedURI {
    String service;
    String endpoint;

    public ParsedURI(URI uri) {
        service = uri.getHost();
        endpoint = uri.getPath();
    }
}
