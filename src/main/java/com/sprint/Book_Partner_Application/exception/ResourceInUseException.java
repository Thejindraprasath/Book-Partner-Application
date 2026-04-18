package com.sprint.Book_Partner_Application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a resource cannot be deleted because other records depend on it.
 * Used across ALL modules.
 *
 * HTTP 409 Conflict
 *
 * Usage examples:
 *   throw new ResourceInUseException("Author", "409-56-7008", "title associations");
 *   throw new ResourceInUseException("Publisher", "1389", "active employees and titles");
 *   throw new ResourceInUseException("Store", "7066", "existing sales records");
 *   throw new ResourceInUseException("Title", "BU1032", "sales records");
 *   throw new ResourceInUseException("Job", "5", "assigned employees");
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInUseException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;
    private final String reason;

    public ResourceInUseException(String resourceName, Object resourceId, String reason) {
        super("Cannot delete " + resourceName + " '" + resourceId + "' — it is still referenced by " + reason + ". Remove those references first.");
        this.resourceName = resourceName;
        this.resourceId   = resourceId;
        this.reason       = reason;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId()   { return resourceId; }
    public String getReason()       { return reason; }
}