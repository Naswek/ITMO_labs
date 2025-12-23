package org.example.services;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.example.entity.ResultEntity;
import org.example.entity.UserEntity;
import org.example.repository.ResultRepository;
import org.example.utils.Validation;
import java.util.List;
import java.util.Map;

@Stateless
public class ResultService {

    @Inject
    private Validation validation;

    @Inject
    private ResultRepository resultRepository;

    @Inject
    private UserService userService;

    public void savePoints(Double rValue, Double xValue, Double yValue, UserEntity user) {
        final boolean hit = validation.checkArea(xValue, yValue, rValue);
        final ResultEntity result = createResult(xValue, yValue, rValue, hit);
        resultRepository.save(result, user);
    }

    public UserEntity getUser(Long userId) {
        if (userId == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Unauthorized"))
                    .build()
            );
        }

        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "User not found"))
                    .build()
            );
        }
        return user;
    }

    private ResultEntity createResult(Double x, Double y, Double r, boolean hit) {
        final ResultEntity result = new ResultEntity();
        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setHit(hit);
        return result;
    }

    private UserEntity getCurrentUser(Long userId) {
        return getUser(userId);
    }
}
