package zesp03.rest.resource;

import zesp03.data.RangeSurveyData;
import zesp03.repository.RangeSurveyRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("range-survey")
public class RangeSurveyResource {
    @GET
    @Produces("application/json")
    public RangeSurveyData get(
            @QueryParam("id") long id,
            @QueryParam("start") long start,
            @QueryParam("end") long end) {
        return new RangeSurveyRepository().rangeSurvey(id, start, end);
    }
}
