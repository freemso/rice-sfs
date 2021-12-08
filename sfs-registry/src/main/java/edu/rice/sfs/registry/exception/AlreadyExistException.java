package edu.rice.sfs.registry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyExistException extends RuntimeException {

  private static final String FORMAT = "%s[%s] already exists.";

  public AlreadyExistException() {
    super();
  }

  public AlreadyExistException(String message) {
    super(message);
  }

  public AlreadyExistException(String type, String id) {
    this(String.format(FORMAT, type, id));
  }

  public AlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyExistException(String type, String id, Throwable cause) {
    this(String.format(FORMAT, type, id), cause);
  }

  public static AlreadyExistException entityAlreadyExists(String name) {
    return new AlreadyExistException("Entity", name);
  }

  public static AlreadyExistException featureAlreadyExists(String name) {
    return new AlreadyExistException("Feature", name);
  }

  public static AlreadyExistException featureTableAlreadyExists(String name) {
    return new AlreadyExistException("FeatureTable", name);
  }

  public static AlreadyExistException featureTableViewAlreadyExists(String name) {
    return new AlreadyExistException("FeatureTableView", name);
  }
}
