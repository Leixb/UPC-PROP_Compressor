TARGET = target

CP = $(TARGET)/classes/
CP_TESTS = $(TARGET)/test-classes/
DIRS = $(CP) $(CP_TESTS)

JFLAGS = -g
JC = javac
JAVA = java
JAR = jar

MAIN = presentacio.Main
VERSION = 1.0-SNAPSHOT
JAR_FILE = $(TARGET)/ProjecteProp-$(VERSION).jar

TEST_JARS = libs/junit-jupiter-5.4.2.jar:libs/junit-jupiter-api-5.4.0.jar:libs/apiguardian-api-1.1.0.jar:libs/hamcrest-core-1.3.jar

JAVA_FILES = $(shell find src/main/ -type f -name '*.java')
JAVA_RESOURCES = $(wildcard src/main/resources/*)

TEST_FILES = $(shell find src/test/ -type f -name '*.java')
TEST_RESOURCES = $(wildcard src/test/resources/*)

.PHONY: all build run copy_java_resources copy_test_resources build_test run_test jar run_jar

# all: build run

build: copy_java_resources $(JAVA_FILES)
	@$(JC) $(JFLAGS) -d $(CP) $(JAVA_FILES)

run:
	@$(JAVA) -cp $(CP) $(MAIN)

dirs:
	@mkdir -p $(DIRS)

copy_java_resources: dirs $(JAVA_RESOURCES)
	-@cp $(JAVA_RESOURCES) $(CP)
copy_test_resources: dirs $(TEST_RESOURCES)
	-@cp $(TEST_RESOURCES) $(CP)

build_test: copy_test_resources
	@$(JC) -d $(CP_TESTS) -cp $(CP):$(TEST_JARS) $(TEST_FILES)

run_test: build_test
	@$(JAVA) -jar libs/junit-platform-console-standalone-1.5.2.jar -cp $(CP):$(CP_TESTS) --scan-class-path

jar: $(JAR_FILE)

$(JAR_FILE): $(JAVA_FILES) $(JAVA_RESOURCES)
	@make build
	@$(JAR) -cfe $(JAR_FILE) $(MAIN) -C $(CP) .

run_jar:
	@$(JAVA) -jar $(JAR_FILE)
