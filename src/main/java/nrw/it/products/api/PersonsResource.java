package nrw.it.products.api;

import nrw.it.products.errors.InvalidQueryParamException;
import nrw.it.products.model.Person;
import nrw.it.products.services.PersonService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("persons")
@ApplicationScoped
public class PersonsResource {

    @Inject
    PersonService personService;

    // GET /persons?lastname=Maus&firstName=xxx&minAge=20&maxAge=60
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons(@BeanParam SearchParams params) {
        Collection<Person> result = personService.getAllPersons();

        if (params.lastName!=null) {
            if (params.lastName.trim().length()==0) {
                throw new InvalidQueryParamException(42, "Der Query-Parameter 'lastname' darf nicht leer sein");
            }

            result = result.stream().filter(p -> p.getLastName().equals(params.lastName)).collect(Collectors.toList());
        }

        return Response.ok().entity(result).build();
    }

    // POST /persons
    // Im Request Body => Person im JSON-Format enthalten sein
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPerson(@Valid Person person) {
        Long personId = personService.addPerson(person);
        URI personURI =
            UriBuilder.fromUri("http://localhost:8080/persons/{id}")
                      .resolveTemplate("id", personId)
                      .build();
        return Response.created(personURI).build();
    }


    // PUT http://localhost:8080/persons/{personId}
    @PUT
    @Path("{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("personId") Long id, @Valid Person updatedPerson) {
        Person existingPerson = personService.getById(id);

        existingPerson.setFirstName(updatedPerson.getFirstName());
        existingPerson.setLastName(updatedPerson.getLastName());
        existingPerson.setAge(updatedPerson.getAge());
        existingPerson.setBeruf(updatedPerson.getBeruf());

        return Response.ok().build();
    }


    // DELETE /persons/{id}
    @DELETE
    @Path("{personId}")
    public Response deletePerson(@PathParam("personId") Long id) {
        try {
            personService.deletePerson(id);
            return Response.ok().build();

        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                           .header("MeinResponseHeader", "TestWert")  // Beispiel für das Setzen eines Response Headers
                           .entity("Fehler: " + ex.getMessage())
                           .build();
        }
    }

    ////////////////// DOWNLOAD VON DOKUMENTEN //////////////////////////////////////

    @GET
    @Path("{personId}/document/{documentId}")
    public Response downloadDocument(@PathParam("personId") String personId, @PathParam("documentId") String documentId) {

        InputStream is = personService.downloadDocumentForPerson(personId, documentId);

        int BUFFER_SIZE = 1024; // Puffergröße sinnvoll wählen
        StreamingOutput stream = outputStream -> {
            byte[] b = new byte[BUFFER_SIZE];
            int n;
            while ((n = is.read(b)) != -1) {
                outputStream.write(b, 0, n);
                outputStream.flush();
            }
            outputStream.close();
            is.close();
        };

        Response.ResponseBuilder responseBuilder = Response.ok(stream, MediaType.APPLICATION_XML);
        return responseBuilder.build();

        /*
        // Dateiname und MIME Type des Dokuments müsste man ermitteln, falls das notwendig ist

        String fileName = "Document.pdf";
        // falls Dokument im Browser dargestellt werden soll, benötigt man den Header Content-Disposition
        if (!"application/pdf".equalsIgnoreCase(mimeType)) {
            responseBuilder = responseBuilder.header
                ("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        }
         */
    }
}