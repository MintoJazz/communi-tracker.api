package com.example.communitracker.core;

@SuppressWarnings("java:S2326")
public sealed interface Result<T> {
    record Success<T>(T data) implements Result<T> {}
    record Failure<T>(String errorMessage) implements Result<T> {}
}