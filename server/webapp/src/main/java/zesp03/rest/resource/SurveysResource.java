package zesp03.rest.resource;

import zesp03.data.MinmaxSurveyData;
import zesp03.dto.AverageSurveyDto;
import zesp03.dto.OriginalSurveyDto;
import zesp03.service.SurveyService;

import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;

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
            return new SurveyService()
                    .getOriginalSurveys(device, start, end)
                    .stream()
                    .map( ds -> {
                        OriginalSurveyDto dto = new OriginalSurveyDto();
                        dto.setDeviceId(device);
                        dto.setClientsSum(ds.getClientsSum());
                        dto.setTime(ds.getTimestamp());
                        return dto;
                    } )
                    .collect(Collectors.toList());
        }
        catch(zesp03.exception.NotFoundException exc) {
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
            final AverageSurveyDto result = new AverageSurveyDto();
            result.setDeviceId(device);
            result.setTimeStart(start);
            result.setTimeEnd(end);
            result.setAvgClients( new SurveyService().getAverageSurvey(device, start, end) );
            return result;
        }
        catch(zesp03.exception.NotFoundException exc) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("minmax")
    public MinmaxSurveyData getMinmaxSimple(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("end") int end) {
        if (start < 0 || end < 0 || end <= start)
            throw new BadRequestException();
        try {
            return new SurveyService().getMinmaxSimple(device, start, end);
        }
        catch(zesp03.exception.NotFoundException exc) {
            throw new NotFoundException();
        }
    }
}
