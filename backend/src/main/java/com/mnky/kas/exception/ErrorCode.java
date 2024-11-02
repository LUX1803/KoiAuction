package com.mnky.kas.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USERNAME_ALREADY_EXISTS(400, "Username already exists", HttpStatus.BAD_REQUEST),
    INVALID_ERROR_KEY(400, "Invalid error key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USERNAME(400, "Username must be at least 2 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    INCORRECT_USERNAME_OR_PASSWORD(400, "Incorrect username or password", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(400, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(400, "Invalid phone number", HttpStatus.BAD_REQUEST),
    LOT_NOT_FOUND(404, "Lot not found", HttpStatus.NOT_FOUND),
    AUCTION_NOT_FOUND(404, "Auction not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Access denied", HttpStatus.FORBIDDEN),
    CREATION_FAILED(500, "Creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BID_SMALLER_THAN_STARTING_PRICE(400, "Bid smaller than starting price", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(400, "Insufficient balance", HttpStatus.BAD_REQUEST),
    NO_PARTICIPANTS(400, "No participants", HttpStatus.BAD_REQUEST),
    ALREADY_BIDDED(409, "Already bidded", HttpStatus.CONFLICT),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
