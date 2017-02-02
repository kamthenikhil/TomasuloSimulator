1. Instructions to run:

I have provided the executable jar for the Tomasulo Simulator which can be executed with the following command:
java -jar <Path to Tomasulo.jar>

The program will load the properties from the config file provided along with the Tomasulo.jar. Make sure the config file is placed in the same folder as the Tomasulo.jar. Appropriate comments have been added above every property in the config file.

IMPORTANT: Make sure you have java 8 installed on your system before you execute the jar.

2. Input file format:

The file containing the instructions should to be in a specific format. Following are the points to consider while writing an instruction file:

- Register names should start with small case r followed by a number e.g. r2

- Use following opcodes for instruction:
	o	LW for load
	o	SW for store
	o	ADD for addition
	o	SUB for subtraction
	o	MULT for multiplication
	o	DIV for division
	o	BNEZ for branch

- To represent a loop, the identifier for the loop with a colon should be included above the first instruction in the loop and the keyword END should be included after the last instruction in the loop. Sample input can be as follows:
	LOOP:
	ADD r1 r2 r3
	SUB r2 r3 r4
	MULT r3 r1 r5
	DIV r4 r3 r6
	LW r5 r4
	SW r5 r7
	BNEZ r7 r8 LOOP
	END
	ADD r1 r2 r3

- I have provided 3 sample instruction files along with the code.
