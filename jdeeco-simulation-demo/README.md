The support for visualization is turned on by uncommenting the "Visualization parameters" section in the *.ini file.
Visualization is based on the Google-Earth tool and requires special simulation schedulers providing "Hardware-in-the-loop" functionality through sockets.
At the moment two such schedulers are given cSocketRTScheduler (for real-time simulation events scheduling) and cSocketSequentialScheduler. Appropriate one needs to be set in the *.ini file.
In order to visualize your simulation please follow these steps.

- Start your simulation
- Wait until all modules are correctly initialized
- Open browser supporting the google-earth plugin (e.g. Chrome - make sure that plugin is installed in the browser)
- Type the following address "localhost:4242"
- The google-earth plugin should appear and after some time it should fly to the location where the simulation was set to.

Explanations:
Colorful circles arround nodes indicate estimated maximum range of the radio.
White lines rely on the above and connect those nodes which can communicate.