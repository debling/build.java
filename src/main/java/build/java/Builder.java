package build.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


/**
 * Main interface for build programs to interact with the build system.
 */
public class Builder {
    public class Workspace {
        public final String projectName;

        public final List<Path> filesToCompile = new ArrayList<>();

        Workspace(String projectName) {
            this.projectName = projectName;
        }


        // TODO: work on the dependencies
        // in reality dependencies could be way more than just maven repo names
        // it could be other project in the file system
        // it could be just a jar
        // it could be in a private m2 repo, and that will require auth
        public void dependOn(String gradleNotation) {
            LOG.warning("NOOP: dependencies management for maven repositories is not implemented yet");
        }

        public void dependOn(Workspace other) {
            LOG.warning("NOOP: dependencies management for other workspaces is not implemented yet");
        }

        public void addFiles(String ...files) {
            for (String f : files) {
                final var p = Path.of(f).normalize();
                final var pFile = p.toFile();
                if (!pFile.exists()) {
                    LOG.severe("%s: The path `%s` does not exists".formatted(getSourceLocation(), p));
                    Builder.this.validConfig = false;
                    continue;
                }
                if (!Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS)) {
                    LOG.severe("%s: The path `%s` is not a regular file".formatted(getSourceLocation(), p));
                    Builder.this.validConfig = false;
                    continue;
                }
                if (!p.toString().endsWith(".java")) {
                    LOG.severe("%s: The path `%s` is not a java source file".formatted(getSourceLocation(), p));
                    Builder.this.validConfig = false;
                    continue;
                }
                this.filesToCompile.add(p);
            }
        }

        public void addJavaTree(String root) {
            final var srcRoot = Path.of(root).normalize();

            if (!Files.isDirectory(srcRoot)) {
                Builder.this.validConfig = false;
                LOG.severe(() ->
                    "The path `%s` supplied is not a valid directory".formatted(root)
                );
            }

            try {
                // PERF: evalute if its worth it to refactor this to send a
                // instance of FileVisitor instead of using streams. The
                // theory is that will reduce memtory footprint by not creating
                // a bunch fo Stream<T> objects
                final var javaFiles = Files.walk(srcRoot)
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
                this.filesToCompile.addAll(javaFiles);
            } catch (IOException ignored) {
                // TODO: to a better job at errror reporting here
                throw new RuntimeException("Failed to add source tree `%s`".formatted(srcRoot), ignored);
            }

        }


        @Override
        public String toString() {
            return "Workspace " + projectName 
                + "\n filesToCompile: \n" + Arrays.toString(this.filesToCompile.toArray());
        }

        

    }

    private final static Logger LOG = Logger.getLogger(Builder.class.getName());
    private final Path buildProgramPath;
    private final List<Workspace> workspaces = new ArrayList<>();

    private boolean validConfig = true;

    
    public Builder(Path buildProgramPath) {
        this.buildProgramPath = buildProgramPath;
    }

    public Workspace makeWorkspace(String projectName) {
        final var ws = new Workspace(projectName);
        this.workspaces.add(ws);
        return ws;
    }

    String getSourceLocation() {
        final var buildTrace = new Exception().getStackTrace()[2];
        final var fname = buildTrace.getFileName();
        assert this.buildProgramPath.toString().equals(buildTrace.getFileName());
        return "%s:%d".formatted(fname,  buildTrace.getLineNumber());
    }

    List<Workspace> getWorkspaces() {
        return Collections.unmodifiableList(this.workspaces);
    }

    boolean isValidConfig() {
        return this.validConfig;
    }
    
    @Override
    public String toString() {
        return buildProgramPath + "\n\n" + ", workspaces: \n" + workspaces.toString();
    }
}
