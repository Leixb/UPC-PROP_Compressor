JFLAGS = -g
CP = target/classes/
CP_TESTS = target/test-classes/
JC = javac
JAVA = java

TEST_JARS = libs/junit-jupiter-5.4.2.jar:libs/junit-jupiter-api-5.4.0.jar:libs/apiguardian-api-1.1.0.jar:libs/hamcrest-core-1.3.jar

MAIN = presentacio.Main

JAVA_FILES = $(shell find src/main/ -type f -name '*.java')
JAVA_RESOURCES = $(wildcard src/main/resources/*)

TEST_FILES = $(shell find src/test/ -type f -name '*.java')
TEST_RESOURCES = $(wildcard src/test/resources/*)

.PHONY: all build run copy_java_resources

# all: build run

build: copy_java_resources
	@$(JC) $(JFLAGS) -d $(CP) $(JAVA_FILES)

run:
	@$(JAVA) -cp $(CP) presentacio.Main

copy_java_resources:
	@cp $(JAVA_RESOURCES) $(CP)

build_test:
	@$(JC) -d $(CP_TESTS) -cp $(CP):$(TEST_JARS) $(TEST_FILES)

run_test: build_test
	@java -jar libs/junit-platform-console-standalone-1.5.2.jar -cp $(CP):$(CP_TESTS) --scan-class-path
