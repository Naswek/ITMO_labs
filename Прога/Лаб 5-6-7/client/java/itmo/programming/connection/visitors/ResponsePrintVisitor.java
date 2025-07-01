package itmo.programming.connection.visitors;

import itmo.programming.PrettyColor;
import itmo.programming.message.OutputMessage;
import itmo.programming.responses.AuthenticationResponse;
import itmo.programming.responses.MessageResponse;
import itmo.programming.responses.ResponseVisitor;
import itmo.programming.responses.ValidationResponse;


/**
 * The type Response print visitor.
 */
public class ResponsePrintVisitor implements ResponseVisitor {

    @Override
    public OutputMessage visit(MessageResponse response) {
        return new OutputMessage(PrettyColor.yellow("Ответ сервера: \n") + response.message());
    }

    @Override
    public OutputMessage visit(ValidationResponse response) {
        return new OutputMessage("Объект с параметром в вашей коллекции: " + response.parameter() + " не существует");
    }

    @Override
    public OutputMessage visit(AuthenticationResponse response) {
        return new OutputMessage(response.message(), response.isValid(), response.login());
    }
}
