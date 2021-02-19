package nrw.it.products.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Provider
public class ProcessingTimeFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // filter for requests
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LocalDateTime startTime = LocalDateTime.now();
        requestContext.setProperty("startTime", startTime);
        System.out.println("Start Zeit: " + startTime);
    }

    // filter for responses
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LocalDateTime startTime = (LocalDateTime) requestContext.getProperty("startTime");
        LocalDateTime endTime = LocalDateTime.now();

        long millis = Duration.between(startTime, endTime).toMillis();
        System.out.println("End-Zeit: " + endTime);
        System.out.println("Benötigte Zeit: " + millis + " ms");
    }
}
