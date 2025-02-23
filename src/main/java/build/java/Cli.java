package build.java;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

@SuppressWarnings("PMD.UseUtilityClass")
public class Cli {
    final private static Path BUILD_CACHE_PATH = Path.of(".build-cache");

    public static void main(String[] args) {
        final Path buildScriptPath;
        if (args.length > 0) {
            buildScriptPath = Path.of(args[0]);
        } else {
            buildScriptPath = Path.of(".", "build.java");
        }

        final var thisJar = Cli.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath();

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
         throw new TODO("""
            Well, if gettiing the javacprovider fails its bcs the user doesnt have a
            jdk installed, he must be running on jre,
            we need a nice way to point him to install id
            """);
        }

        try (var fileManager = compiler.getStandardFileManager(null, null, null)) {
            final var buildCompUnit = fileManager.getJavaFileObjects(buildScriptPath);
            final var compTask = compiler.getTask(
                null,
                fileManager,
                null,
                List.of(
                    "-Xlint",
                    "--enable-preview",
                    "--source=23",
                    "-d", BUILD_CACHE_PATH.toString()),
                null,
                buildCompUnit
            );
            if (compTask.call()) {
                final var buildScriptFilename = buildScriptPath.getFileName().toString();

                final var buildScriptClassName = buildScriptFilename.substring(0, buildScriptFilename.lastIndexOf("."));

                // TODO: load from the file system, with the URLClassLoader, if buildScript does not
                // need rebuild
                final var loader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
                try {
                    final Class<?> buildScriptClazz = loader.loadClass(buildScriptClassName);
                    final var ctor = buildScriptClazz.getDeclaredConstructor();
                    final var buildProgram = ctor.newInstance();
                    final var buildMethod = buildScriptClazz.getMethod("build", Builder.class);
                    final var b = new Builder(buildScriptPath);
                    buildMethod.invoke(buildProgram, b);


                    for (var ws : b.getWorkspaces()) {
                        final var paths = ws.filesToCompile.toArray(new Path[0]);

                        final var compUnits = fileManager.getJavaFileObjects(paths);

                        final var task = compiler.getTask(
                            null,
                            fileManager,
                            null,
                            List.of(
                                "-Xlint",
                                "-d", "test-target"
                            ),
                            null,
                            compUnits
                        );

                        task.call();

                    }
                    System.out.println(b);
                } catch (Exception e) {
                    throw new TODO("""
                        We do expect the class to have the same name, lets instruct the user
                        about the missmatch!
                        Theres also the case of the user putting some pakcage declaration on it,
                        theres two approach, we can try to get the package name and load it,
                        or we can conplain to the user.
                        For now i think the load the classna
                        """, e);
                }
            } else {
                System.out.println("compilation error");
            }

        } catch (IOException ignored) {}

    }
}

