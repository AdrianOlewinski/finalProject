package pl.coderslab.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.coderslab.entity.User;

@Component
public class StringToUserConverter implements Converter<String, User> {

    @Override
    public User convert(String source){
        try{
            long userId = Long.parseLong(source);

            User user = new User();
            user.setId(userId);

            return user;
        } catch (NumberFormatException e){

        }
        return null;

    }
}
