package facades;

import controllers.json.JsonEx;
import controllers.json.view.ActionsView;
import models.NamedModel;
import models.Sensor;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Result;
import repository.SaveRepository;
import repository.SensorRepository;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;
import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;


public class SensorFacade {
    private SensorRepository sensorRepository;
    private final Validator validator;
    private final MessagesApi messagesApi;

    @Inject
    public SensorFacade(final SensorRepository sensorRepository,
                        final Validator validator,
                        final MessagesApi messagesApi) {
        this.sensorRepository = sensorRepository;
        this.validator = validator;
        this.messagesApi = messagesApi;
    }

    public CompletionStage<Result> save(Sensor sensor) {
        return sensorRepository.save(sensor);

//        .thenApplyAsync(sensor);
//                .thenApplyAsync(result -> {
//                    if (result.left.isPresent()) {
//                        return badRequest(Json.newObject().put("message", result.left.get()));
//                    } else {
//                        if (Objects.nonNull(id)) {
//                            return ok(JsonEx.toJson(ActionsView.UpdateView.class, result.right));
//                        } else {
//                            return ok(JsonEx.toJson(ActionsView.CreateView.class, result.right));
//                        }
//                    }
//                });
    }

    private <T extends NamedModel> CompletionStage<Result> save(T entity, String id, SaveRepository<T> repository) {

        if (Objects.nonNull(id)) {
            Integer newId = Integer.parseInt(id);
            entity.id = newId;
        } else {
            entity.id = null;
        }

        final Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            final List<String> messages =
                    violations.stream().map(v -> getMessage(v.getMessage())).collect(toList());
            return CompletableFuture.completedFuture(
                    badRequest(Json.newObject().putPOJO("messages", messages)));
        }

        return repository.save(entity)
                .thenApplyAsync(result -> {
                    if (result.left.isPresent()) {
                        return badRequest(Json.newObject().put("message", result.left.get()));
                    } else {
                        if (Objects.nonNull(id)) {
                            return ok(JsonEx.toJson(ActionsView.UpdateView.class, result.right));
                        } else {
                            return ok(JsonEx.toJson(ActionsView.CreateView.class, result.right));
                        }
                    }
                });
    }

    public CompletionStage<Sensor> getById(final String id) {
        return sensorRepository.getById(id);
    }

    private String getMessage(final String code, final Object... args) {
        return messagesApi.preferred(request()).at(code, args);
    }
}
