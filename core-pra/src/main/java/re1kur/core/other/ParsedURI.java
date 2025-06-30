package re1kur.core.other;

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
