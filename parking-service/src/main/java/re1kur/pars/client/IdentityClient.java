package re1kur.pars.client;

import java.util.UUID;

public interface IdentityClient {
    boolean isExistsById(UUID ownerId);
}
