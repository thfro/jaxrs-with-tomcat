package nrw.it.products.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidQueryParamExceptionMapper implements ExceptionMapper<InvalidQueryParamException> {
    @Override
    public Response toResponse(InvalidQueryParamException exception) {
        ApiError error = new ApiError(exception.getErrorCode(), exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
