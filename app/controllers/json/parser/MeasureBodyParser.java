package controllers.json.parser;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import models.Measure;
import play.libs.F;
import play.libs.streams.Accumulator;
import play.mvc.BodyParser;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.Executor;

/**
 * Book entity body parser.
 */
public class MeasureBodyParser implements BodyParser<Measure> {

    private final BodyParser.Json jsonParser;
    private final Executor executor;

    @Inject
    public MeasureBodyParser(final BodyParser.Json jsonParser, final Executor executor) {
        this.jsonParser = jsonParser;
        this.executor = executor;
    }

    @Override
    public Accumulator<ByteString, F.Either<Result, Measure>> apply(final RequestHeader request) {
        final Accumulator<ByteString, F.Either<Result, JsonNode>> jsonAccumulator = jsonParser.apply(request);
        return jsonAccumulator.map(resultOrJson -> {
            if (resultOrJson.left.isPresent()) {
                return F.Either.Left(resultOrJson.left.get());
            } else {
                //noinspection ConstantConditions
                final JsonNode json = resultOrJson.right.get();
                try {
                    final Measure measure = play.libs.Json.fromJson(json, Measure.class);
                    return F.Either.Right(measure);
                } catch (Exception e) {
                    return F.Either.Left(Results.badRequest(
                            play.libs.Json.newObject().putPOJO(
                                    "messages",
                                    new String[]{"Unable to read Measure from json"}
                            )));
                }
            }
        }, executor);
    }
}
