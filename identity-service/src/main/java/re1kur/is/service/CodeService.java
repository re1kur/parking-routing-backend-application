package re1kur.is.service;

public interface CodeService {
    String generateCode(String id);

    void validateCode(String id, String expected);
}
