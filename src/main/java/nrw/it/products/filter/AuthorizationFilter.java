package nrw.it.products.filter;


import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(2)
@Authenticated
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String headerValue = ctx.getHeaderString("Authorization");
        if (headerValue==null || headerValue.length()==0) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            System.out.println("Authorisierung fehlgeschlagen");
        } else {
            System.out.println("Authorisierung erfolgreich");
        }
    }
}
