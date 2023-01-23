package com.kezbek.transaction.external;

public interface EnvironmentAdaptor <K, V> {
    V execute(K request);
}
