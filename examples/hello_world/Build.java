import build.java.Builder;


public class Build {

    public void build(Builder b) {
        final var ws = b.makeWorkspace("hello-project");
        ws.addFiles("./HelloWorld.java");
    }
}
