package com.github.thedeathlycow.scorchful.config.schema;

import java.io.IOException;

public interface ConfigUpdater {
    void run() throws IOException;
}