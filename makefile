JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        bbst.java \
        RbstUtil.java 

default: classes
bbst   : classes
#	 mv Rbst.class bbst.class

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
