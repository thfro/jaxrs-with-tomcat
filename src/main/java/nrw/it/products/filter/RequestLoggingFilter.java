package nrw.it.products.filter;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(1)
public class RequestLoggingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        System.out.println(ctx.getMethod() + " " + ctx.getUriInfo().getAbsolutePath());
        System.out.println(ctx.getHeaders());
    }
}
