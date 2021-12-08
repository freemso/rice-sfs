package edu.rice.sfs.registry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

  private static final String FORMAT = "%s[%s] is not found.";

  public NotFoundException(String type, String id) {
    this(String.format(FORMAT, type, id));
  }

  public NotFoundException(String message) {
    super(message);
  }

  public static NotFoundException entityNotFound(String name) {
    return new NotFoundException("Entity", name);
  }

  public static NotFoundException featureNotFound(String name) {
    return new NotFoundException("Feature", name);
  }

  public static NotFoundException featureTableNotFound(String filterName) {
    return new NotFoundException("FeatureTable", filterName);
  }

  public static NotFoundException featureTableViewNotFound(String filterName) {
    return new NotFoundException("FeatureTableView", filterName);
  }
}
