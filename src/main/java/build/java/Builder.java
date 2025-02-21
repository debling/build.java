package build.java;

import java.util.List;

public class Builder {
    
    public static List<String> compile(final String ...args) {
        var cmd = new String[args.length];
        cmd[0] = "echo";
        for (int i = 1; i < args.length; i++) {
            cmd[i] = args[i - 1];
        }

        try {
            Process process = new ProcessBuilder(cmd).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of("worked");
    }

    public static void buildJar(String outputName, final List<String> compilationUnits) {
        System.out.println("out: " + outputName);
        System.out.println("package: " + compilationUnits);
    }
}
