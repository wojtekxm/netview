package zesp03.rest.resource;

import zesp03.data.RangeSurveyData;
import zesp03.repository.RangeSurveyRepository;

import javax.ws.rs.*;

@Path("range-survey")
public class RangeSurveyResource {
    @GET
    @Produces("application/json")
    public RangeSurveyData get(
            @QueryParam("id") long id,
            @QueryParam("start") long start,
            @QueryParam("end") long end) {
        if(start > end)
            throw new BadRequestException("start > end");
        RangeSurveyData result = new RangeSurveyRepository().rangeSurvey(id, start, end);
        if(result == null)
            throw new NotFoundException();
        return result;
    }
}
