package build.java;

import java.util.Arrays;

public class Cli {

    public static void main(String[] args) {
        System.out.println("args:: " + Arrays.toString(args));

        final var thisJar = Cli.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath();
    }
}

