package zesp03.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.dto.ChangePasswordResultDto;
import zesp03.service.UserService;

import javax.ws.rs.*;

//TODO aktualizacja ciasteczek po zmianie hasła
@Path("change-password")
@Produces("application/json")
public class ChangePasswordResource {
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordResource.class);

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public ChangePasswordResultDto post(
            @FormParam("userId") long userId,
            @FormParam("old") String old,
            @FormParam("desired") String desired,
            @FormParam("repeat") String repeat) {
        try {
            String passtoken = new UserService().changePassword(userId, old, desired, repeat);
            ChangePasswordResultDto result = new ChangePasswordResultDto();
            result.setReason(null);
            result.setSuccess(true);
            result.setPassToken(passtoken);
            return result;
        }
        catch(zesp03.common.NotFoundException exc) {
            throw new NotFoundException();
        }
        catch(zesp03.common.AuthenticationException exc) {
            ChangePasswordResultDto result = new ChangePasswordResultDto();
            result.setReason(ChangePasswordResultDto.ErrorReason.INVALID_OLD_PASSWORD);
            result.setSuccess(false);
            result.setPassToken(null);
            return result;
        }
        catch(zesp03.common.RejectedValueException exc) {
            ChangePasswordResultDto result = new ChangePasswordResultDto();
            if( exc.getField().equals("desired") )
                result.setReason(ChangePasswordResultDto.ErrorReason.REJECTED_NEW_PASSWORD);
            else
                result.setReason(ChangePasswordResultDto.ErrorReason.PASSWORDS_DONT_MATCH);
            result.setSuccess(false);
            result.setPassToken(null);
            return result;
        }
    }
}
