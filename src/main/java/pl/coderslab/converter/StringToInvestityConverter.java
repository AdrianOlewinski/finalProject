package pl.coderslab.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.coderslab.entity.Investity;

@Component
public class StringToInvestityConverter implements Converter<String, Investity> {

    @Override
    public Investity convert(String source){
        try{
            long investityId = Long.parseLong(source);

            Investity investity = new Investity();
            investity.setId(investityId);

            return investity;
        } catch (NumberFormatException e){

        }
        return null;
    }
}
