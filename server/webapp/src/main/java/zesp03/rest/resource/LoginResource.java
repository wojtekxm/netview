package zesp03.rest.resource;

import zesp03.dto.LoginResultDto;
import zesp03.service.LoginService;

import javax.ws.rs.*;

@Path("login")
public class LoginResource {
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public LoginResultDto form(
            @FormParam("username") String userName,
            @FormParam("password") String password) {
        if(userName == null || password == null)
            throw new BadRequestException();
        LoginResultDto result = new LoginService().login(userName, password);
        //TODO set cookies ?
        return result;
    }
}
