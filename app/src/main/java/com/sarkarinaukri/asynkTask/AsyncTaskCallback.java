package com.sarkarinaukri.asynkTask;

/**
 * Created by AppsMediaz Technologies on 7/26/2017.
 */
public interface AsyncTaskCallback<T> {
    void execute(T data);
}
