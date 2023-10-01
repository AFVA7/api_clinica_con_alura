package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class HorarioDeFuncionamientoClinica implements ValidadorDeConsultas{
    public void validar(DatosAgendarConsulta datos){
        var domingo = DayOfWeek.SUNDAY.equals(datos.fecha().getDayOfWeek());
        var antesDeApertura = datos.fecha().getHour()<7;
        var despuesDECierre = datos.fecha().getHour()>19;
        if(domingo || antesDeApertura || despuesDECierre){
            throw new ValidationException("El horario de la clínica es de" +
                    "Lunes a Sábado de 7:00AM a 19:00 horas");
        }
    }
}
