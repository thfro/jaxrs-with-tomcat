package nrw.it.products.api;

import nrw.it.products.filter.Authenticated;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("hello")
@RequestScoped
@Authenticated
public class HelloResource {

    @GET
    @Produces("text/plain")
    public Response sayHello() {
        return Response.ok().entity("Hello").build();
    }
}
