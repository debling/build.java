# build.java
> [!WARNING]
> This project is not in a production-ready state yet!

Because java build tools does not have to be miserable

# Usage this build system (or future usage, we are getting there)
1) Download and install this build system

2) Create a build.java inside your project

```java
import build.java.Builder;

void main() {
    var ws = Builder.makeWorkspace("my-project");
    ws.dependOn("com.googlecode.json-simple:json-simple:1.1.1")
    ws.addTree("src/"); // will add your .java files for compilation, and will bundle your resources
    ws.buildUberJar(); // or just buildJar() if you prefer to not bundle your deps inside the jar
}
```

Thanks to java 21, you don't need to declare a class, and all the boilerplate to
have a main method.

3. Run the build-java command

```sh
build-java
```

And done! Now you can run `java -jar ./my-project.jar`, build.java already
automatcly setup your main class;


# Main objectives of this project

- Compile and package it fast (unlike gradle);
- I know java, i like to program in java, not xml (looking at you ant/maven);
- Make it easy to extend the build, easy to bake file and doing custom steps (the complete oposite of maven).
- A usable report for unit tests in the terminal (i dont know why this one is hard, both gradle and maven are very sucky at this unless you spend a good amount of time configurating custom reporters)
- Provide a good user experiance for java developers, even for the ones that are afraid of the terminal


# Roadmap / goals
- [ ] basic build with maven like file tree
- [ ] download maven dependencies
    - [ ] Parallel download of pom and artifacts? (maven only does artifacts, pom are downloaded in a serial manner)
- [ ] a `doc` command that compile all the javadoc of the dependencies and servers
  them locally, kinda like `cargo doc` from rust
- [ ] A test command that runs junit tests and print the surfire report in a
nice way
- [ ] Multi module project support
- [ ] Pom.xml generation (sadly we need it for publishing libraries)
- [ ] Provide example and facility building JNI based libraries
- [ ] Lock file?


# Main inspirations for this project:
- [Clojure's tools.build](https://clojure.org/guides/tools_build)
- [Zig build system](https://ziglang.org/learn/build-system/)
- The Jai programing language
- [nob.h - NoBuild](https://github.com/tsoding/nob.h)
