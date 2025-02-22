package build.java;

import java.util.Arrays;
import java.util.spi.ToolProvider;
import javax.tools.JavaCompiler;

public class Cli {

    public static void main(String[] args) {
        System.out.println("args:: " + Arrays.toString(args));

        final var thisJar = Cli.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath();

        final var compiler = ToolProvider.findFirst("javac");

        var c = compiler.orElseThrow(() -> new TodoException("""
            Well, if gettiing the javacprovider fails its bcs the user doesnt have a
                jdk installed, he must be running on jre, we n
            eed a nice way to point him to install id
            """)));

        System.out.println(c.getClass().getCanonicalName());
    }
}

