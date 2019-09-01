# sample assembler

inspired from https://github.com/DQNEO/HowToWriteACompiler

## usage

```bash
sbt reload
sbt assembly
echo "2" | java -jar target/scala-2.13/scc-assembly-0.1.jar | ./asrun.sh  #2
echo "-2" | java -jar target/scala-2.13/scc-assembly-0.1.jar | ./asrun.sh  #254
echo "21 * 2" | java -jar target/scala-2.13/scc-assembly-0.1.jar | ./asrun.sh  #42
```
