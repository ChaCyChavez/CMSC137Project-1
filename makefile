build:
	javac -d bin -sourcepath src src/*.java
	jar cfev Knock-Out-v.2.0.jar Main -C bin .

debug-build:
	javac -d bin -sourcepath src src/*.java -g
	jar cfev Knock-Out-v.2.0.jar Main -C bin .
