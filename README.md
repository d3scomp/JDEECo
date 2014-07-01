### Development of Smart Cyber-Physical Systems (CBSE 2014 Tutorial)
---
The project contains 4 examples:

**Example 0** - Hello world component. Periodically prints out “Hello world”. In parallel increments a counter at a double speed.

**Example 1** - Vehicle component. It randomly generates place to go. When it gets there, prints out a message and generates another random destination. Keeps doing this ad infinitum. In parallel, periodically prints out the status of the component

**Example 2** - Components as in Example 1. Components form ensembles. When more components come close, they all follow the destination of the component with the lowest ID.

**Example 3** - Vehicle component has a given destination to go to. When it gets close to a destination, it chooses a place close-by where it can park. To know where it can park, it (a) monitors free parking spaces – as it passes by them, and (b) exchanges the information about free parking spaces with vehicles close-by.

Each of the projects is an Eclipse project. To run it, simply import the project to Eclipse. The launch configuration is part of the project, thus a corresponding run target automatically appears in the Run configurations. Simply run the project by executing the run configuration.

The slides accompanying the examples are located in file [tutorial.pdf](https://github.com/d3scomp/cbse-tutorial/raw/master/tutorial.pdf).
