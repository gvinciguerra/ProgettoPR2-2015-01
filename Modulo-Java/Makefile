# Tutti i file sorgente da compilare
SOURCE_FILES = DeletedTweet.java Tweet.java myTw.java DeletedUser.java \
			   SimpleTw.java User.java Main.java Tag.java
EXCEPTIONS_FILES = EmptyMsgException.java UnauthorizedUserException.java \
				   MsgException.java WrongCodeException.java \
				   UnauthorizedAccessException.java
TEST_FILES = myTwTest.java

# La destinazione di tutti i file .class
CLASSES_PATH = classes

# Opzioni compilatore
JOPTS = -sourcepath src -classpath lib/*:classes: -d $(CLASSES_PATH)

# Regola per compilare i file sorgenti .java
$(CLASSES_PATH)/%.class: src/%.java
	javac $(JOPTS) $<

# Regola per compilare i SOURCE_FILES e gli EXCEPTIONS_FILES
sources: $(addprefix $(CLASSES_PATH)/, $(SOURCE_FILES:.java=.class)) \
 		 $(addprefix $(CLASSES_PATH)/exceptions/, $(EXCEPTIONS_FILES:.java=.class))

# Regola per creare ed eseguire il test
test: sources $(addprefix $(CLASSES_PATH)/tests/, $(TEST_FILES:.java=.class))
	java -classpath lib/*:classes org.junit.runner.JUnitCore $(TEST_FILES:.java=)

default: sources

clean:
	find $(CLASSES_PATH) -name "*.class" -print -exec rm {} \;