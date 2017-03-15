package zesp03.rest.resource;

import zesp03.dto.AverageSurveyDto;
import zesp03.dto.MinmaxSurveyDto;
import zesp03.dto.OriginalSurveyDto;
import zesp03.service.SurveyService;

import javax.ws.rs.*;
import java.util.List;

@Path("surveys")
@Produces("application/json")
public class SurveysResource {
    @GET
    @Path("original")
    public List<OriginalSurveyDto> getClientsOriginal(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("end") int end) {
        if (start < 0 || end < 0 || end < start)
            throw new BadRequestException();
        try {
            return new SurveyService().getOriginalSurveys(device, start, end);
        }
        catch(zesp03.common.NotFoundException exc) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("average")
    public AverageSurveyDto getAverageSurvey(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("end") int end) {
        if (start < 0 || end < 0 || end <= start)
            throw new BadRequestException();
        try {
            return new SurveyService().getAverageSurvey(device, start, end);
        }
        catch(zesp03.common.NotFoundException exc) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("minmax")
    public MinmaxSurveyDto getMinmaxSimple(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("end") int end) {
        if (start < 0 || end < 0 || end <= start)
            throw new BadRequestException();
        try {
            return new SurveyService().getMinmaxSimple(device, start, end);
        }
        catch(zesp03.common.NotFoundException exc) {
            throw new NotFoundException();
        }
    }
}
