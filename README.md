# Lindenmayer System Interpreter

Language: [EN](README.md) | [FR](README.fr.md)


This is a L-System Interpreter wrote on Java. Visualisation in 2D and 3D is implemented.
Deterministic, stochastic and some contextual (with technical limits) systems are implemented.
This was a team project I worked on for a semester. For more details please see the report of the project

To create java doc use `ant doc`


## Usage
To compile and execute the project using **ANT**, use the following command:
> ant run -Dfile={filepath} -Dturtle={2d|3d} -Dtext={y|n}

For direct execution with **Java**, it is mandatory to use these options to allow access to graphical modules:
> java --add-exports=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar Project-Lindemeyer.jar {filepath} {2d|3d} {y|n}

If the 3rd argument is 'y', the fractal's text result will be saved in a .txt file.

## System Configuration
To generate a fractal, you must create a configuration file containing the following properties:
- **Axiom**: The initial state of the system.
- **Iteration**: The number of growth cycles.
- **Rule**: Production rules (symbol transformations).
- **Action**: Commands associated with the turtle's movements.

### Example Configuration File:
```
axiom = A
rule = A, ^\AB^\ABA+B^//ABA_B-//ABA+B/A+/

iteration = 3

action = B, move 50
action = +, left 90
action = -, right 90
action = ^, up 90
action = _, down 90
action = /, rollR 90
action = \, rollL 90
action = [, push
action = ], pop
```

## Stochastic Rules (Probabilities)
It is possible to define stochastic rules with different probabilities as follows:
```
rule = F, F[+F][-F]F:0.33
rule = F, F[+F]F:0.33
rule = F, F[-F]F:0.34
```
Syntax: rule = {predecessor}, {production}:{probability}
Note: The sum of probabilities for the same predecessor must be equal to 1.

## Contextual Rules 
It is possible to define contextual rules with different contexts.
```
rule = A<F>B, F+F
rule = X<F, F-F
rule = F>X, FF
```
Syntax: `rule = {left-context}<{predecessor}>{right-context}, {production}`

