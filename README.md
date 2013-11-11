Tool for generating dependency graph of Eclipse projects.

This is a simple scala script for generating a project dependency graph of Eclipse projects.

The main method takes to arguments

1. The starting directory to begin the scanning for .classpath files.
2. Output directory where the graphviz dot file will be generated.


To generate a pdf file use graphviz, to parse the out file. An example could be

```
scala src/GraphGenerator.scala ~/workspace /tmp/test.dot

dot /tmp/test.dot -Tpdf -O
```