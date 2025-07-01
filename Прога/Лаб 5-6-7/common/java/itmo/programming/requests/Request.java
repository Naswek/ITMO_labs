package itmo.programming.requests;


/**
 * The interface Request.
 */
public sealed interface Request permits
        AddRequest,
        ClearRequest,
        CountGreaterThanCarRequest,
        CountLessThanSoundtrackNameRequest,
        FilterContainsNameRequest,
        HelpRequest,
        InfoRequest,
        RemoveByIdRequest,
        RemoveByIndexRequest,
        ShowRequest,
        UpdateRequest,
        SortRequest,
        RemoveFirstRequest,
        ValidaitionIndexRequest,
        ValidationIdRequest,
        AuthenticationRequest,
        UserObjectsRequest {
    /**
     * The type Message.
     */
    record Message(int id, Request request) {}
}
