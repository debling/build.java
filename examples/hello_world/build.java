import build.java.Builder;

void main() {
    var compilationResult = Builder.compile("./HelloWorld.java");

    Builder.buildJar("hello.java", compilationResult);
}
