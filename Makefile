TARGET = target

CP = $(TARGET)/classes/
CP_TESTS = $(TARGET)/test-classes/
CP_DRIVERS = $(TARGET)/driver-classes/

DIRS = $(CP) $(CP_TESTS) $(CP_DRIVERS)

JFLAGS = -g
JC = javac
JAVA = java
JAR = jar

MAIN = presentacio.Presentacio
MAIN_CLI = presentacio.Main
VERSION = 1.0-SNAPSHOT
JAR_FILE = $(TARGET)/ProjecteProp-$(VERSION).jar

TEST_JARS = libs/junit-jupiter-5.4.2.jar:libs/junit-jupiter-api-5.4.0.jar:libs/apiguardian-api-1.1.0.jar:libs/hamcrest-core-1.3.jar

UI_JARS = libs/forms_rt-7.0.3.jar

JAVA_FILES = $(shell find src/main/java -type f -name '*.java')
JAVA_RESOURCES = $(wildcard src/main/resources/*)

TEST_FILES = $(shell find src/test/java -type f -name '*.java')
TEST_RESOURCES = $(wildcard src/test/resources/*)

DRIVER_FILES = $(shell find src/test/drivers -type f -name '*.java')

.PHONY: all build run copy_java_resources copy_test_resources build_test run_test jar runjar

# all: build run
#

build: copy_java_resources $(JAVA_FILES)
	$(JC) $(JFLAGS) -d $(CP) -cp $(UI_JARS) $(JAVA_FILES)

clean:
	rm -r $(TARGET)

run:
	@$(JAVA) -cp $(CP):$(UI_JARS) $(MAIN)

run_console:
	@$(JAVA) -cp $(CP):$(UI_JARS) $(MAIN_CLI)

dirs:
	@mkdir -p $(DIRS)

copy_java_resources: dirs $(JAVA_RESOURCES)
	@if [ -n "$(JAVA_RESOURCES)" ]; then\
		cp $(JAVA_RESOURCES) $(CP); fi
copy_test_resources: dirs $(TEST_RESOURCES)
	@if [ -n "$(TEST_RESOURCES)" ]; then\
		cp $(TEST_RESOURCES) $(CP_TESTS); fi

build_test: copy_test_resources
	$(JC) -d $(CP_TESTS) -cp $(CP):$(TEST_JARS) $(TEST_FILES)

run_test:
	@$(JAVA) -jar libs/junit-platform-console-standalone-1.5.2.jar -cp $(CP):$(CP_TESTS) --scan-class-path

jar: $(JAR_FILE)

$(JAR_FILE): $(JAVA_FILES) $(JAVA_RESOURCES)
	make build
	$(JAR) -cfe $(JAR_FILE) $(MAIN) -C $(CP) .

runjar:
	@$(JAVA) -cp $(JAR_FILE):$(UI_JARS) $(MAIN)

build_drivers: dirs
	$(JC) -d $(CP_DRIVERS) -cp $(CP) $(DRIVER_FILES)

run_JPEG_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) JPEGDriver

run_LZ78_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) LZ78Driver

run_LZW_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) LZWDriver

run_LZSS_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) LZSSDriver

run_Statistics_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) StatisticsDriver

run_CtrlDomini_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) CtrlDominiDriver

run_IO_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) IODriver

run_BitSetL_driver:
	$(JAVA) -cp $(CP):$(CP_DRIVERS) BitSetLDriver
