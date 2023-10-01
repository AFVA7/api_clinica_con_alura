package med.voll.api.infra.errors;

import org.springframework.context.annotation.Bean;

public class ValidacionDeIntegridad extends RuntimeException {
    public ValidacionDeIntegridad(String s) {
        super(s);
    }
}
