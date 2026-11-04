package org.bitwave.ve3.core.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourcePaths {
    private ResourcePaths() {}

    public static String toFilePath(String pathOrResource) {
        if (pathOrResource == null || pathOrResource.isBlank()) {
            throw new IllegalArgumentException("Resource path is empty");
        }

        String normalized = pathOrResource.replace('\\', '/');

        File file = new File(normalized);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        String resourcePath = normalized.startsWith("/") ? normalized.substring(1) : normalized;
        URL url = ResourcePaths.class.getClassLoader().getResource(resourcePath);
        if (url != null) {
            try {
                return Paths.get(url.toURI()).toString();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Bad resource URL for: " + pathOrResource, e);
            }
        }

        int buildResourcesIndex = normalized.indexOf("/build/resources/main/");
        if (buildResourcesIndex >= 0) {
            resourcePath = normalized.substring(buildResourcesIndex + "/build/resources/main/".length());
            url = ResourcePaths.class.getClassLoader().getResource(resourcePath);
            if (url != null) {
                try {
                    return Paths.get(url.toURI()).toString();
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("Bad resource URL for: " + pathOrResource, e);
                }
            }
        }

        throw new IllegalArgumentException("Resource not found: " + pathOrResource);
    }

    public static String toResourcePath(String pathOrResource) {
        if (pathOrResource == null || pathOrResource.isBlank()) {
            throw new IllegalArgumentException("Resource path is empty");
        }

        String normalized = pathOrResource.replace('\\', '/');
        String resourcePath = normalized;

        int buildResourcesIndex = normalized.indexOf("/build/resources/main/");
        if (buildResourcesIndex >= 0) {
            resourcePath = normalized.substring(buildResourcesIndex + "/build/resources/main/".length());
        } else {
            int srcResourcesIndex = normalized.indexOf("/src/main/resources/");
            if (srcResourcesIndex >= 0) {
                resourcePath = normalized.substring(srcResourcesIndex + "/src/main/resources/".length());
            }
        }

        return resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
    }
}