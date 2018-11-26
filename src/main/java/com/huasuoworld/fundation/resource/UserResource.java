package com.huasuoworld.fundation.resource;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.huasuoworld.fundation.bo.User;
import com.huasuoworld.fundation.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@RequestScoped
public class UserResource {

    private UserService service;

    @Inject
    public UserResource(UserService service) {
        this.service = service;
    }

    @GET
    @Path("selectById")
    @Produces(MediaType.APPLICATION_JSON)
    public User selectById(String id) {

        return service.selectById(id);
    }
}
