package pl.coderslab.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.Supplier;

@Component
public class StringToSupplierConverter implements Converter<String, Supplier> {
    @Override
    public Supplier convert(String source){
        try{
            long supplierId = Long.parseLong(source);

            Supplier supplier = new Supplier();
            supplier.setId(supplierId);

            return supplier;
        } catch (NumberFormatException e){

        }
        return null;
    }
}
