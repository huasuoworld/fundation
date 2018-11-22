package com.huasuoworld.fundation.resource;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.huasuoworld.fundation.service.NextValueSource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@RequestScoped
public class GetNextValueResource {

        private NextValueSource source;

        @Inject
        public GetNextValueResource(NextValueSource source) {
            this.source = source;
        }

        @GET
        @Path("getNext")
        @Produces(MediaType.TEXT_PLAIN)
        public String getNext() {
            return Integer.toString(source.nextValue());
        }
}
