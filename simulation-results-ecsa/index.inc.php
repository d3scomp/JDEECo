<h1 id="experiments">Experiments</h1>
<p>
The sources necessary for simulation and evaluation of the results are available <a href="https://github.com/d3scomp/JDEECo/tree/simulation">here</a>.
</p>
<p>
We have simulated several different scenarios (on different scales and using several randomly generated variants). Two of them are elaborated below.
The objective of the scenarios is to illustrate the benefit performance gain by employing communication boundary, which limits data sharing strictly to the regions outlined by the communication boundary. 
All scenarios are based on monitoring how fast do get FireFighter (FF) Leaders notified about FF Members of their team that get into a danger situation (teams are deployed in dedicated spatial areas).
</p>
<p>
The technical parameters of all the simulations are:
</p>
<ul>
	<li>Simulation run is 60 seconds</li>
	<li>Every member gets into danger between 15s and 30s (and stays there)</li>
	<li>Areas have a rectangular shape of fixed dimensions of 100 by 100 meters</li>
	<li>2 Leaders per team in each area.</li>
	<li>Evenly distributed nodes (on average 1 node per each 20x20m region)</li>
	<li>Wireless communication range is 25 meters (emulating the indoor condition of the scenarios)</li>
	<li>The size of a single replica is 128 bytes = 1 packet (including both the application data and metadata such as hop count, sender, version number, etc.)</li>
	<li>The Ethernet is set to 10Mbps bandwidth and 100ms latency</li>
	<li>Knowledge publishing period is 2 seconds</li>
	<li>Ethernet available to 25% of nodes (randomly selected)</li>
	<li>Max random broadcast (MANET) gossip delay is 1s</li>
	<li>Constant direct (Ethernet) gossip delay is 400ms</li>
	<li>Number of peers selected for direct (Ethernet) gossip step is 2.</li>
</ul>


<h1 id="case_1">Case 1</h1>
The scenario consists of several deployed firefighter teams that partially overlap in terms of the radio signal coverage (on average 20 nodes per team, 2 leaders per team). Each team uses the other teams’ mem-bers as relays for knowledge dissemination in the overlapping areas to ensure the necessary wireless coverage. 

<p>
<figure>
  <figcaption>Example of a configuration of the Case 1 scenario (4 overlapping areas / 20 FFs per team/ 85 nodes total).</figcaption>
  <img src=files/example-c.png width="75%" />
</figure>
</p>



<h2>Results</h2>

<p>
<figure>
  <figcaption>Time for discovering a team Member in danger by a corresponding Leader.</figcaption>
  <img src=files/response-times-c.png width="50%" />
</figure>
</p>

<p>
<figure>
  <figcaption>Number of replicas dissemniated within the whole system.</figcaption>
  <img src=files/message-counts-c.png width="50%" />
</figure>
</p>

<p>
<figure>
  <figcaption>Number of unnecessary disseminations that were prevented due to communicaiton boundary (when enabled).</figcaption>
  <img src=files/boundary-hits-c.png width="50%" />
</figure>
</p>

<h2>Raw data</h2>

<table class="tcompact">
	<tr><th>Simulation inputs</th><td><a href="files/inputs-c.zip">inputs.zip</a></td></tr>
	<tr><th>Raw results</th><td><a href="files/results-c.zip">results.zip</a></td></tr>
</table>
<p>&nbsp;</p>

<h1 id="case_2">Case 2</h1>
The scenario includes a single FireFighter (FF) team deployed over two areas interconnected via Ethernet (25 FFs per area, 2 leaders per area). The areas are surrounded by external components. The boundary condition of  FF ensemble prevents FF knowledge leaving the two buildings. The boundary condition of the surrounding components prevents the data from entering the two buildings (i.e., the two boundary conditions are complementary).

<figure>
  <figcaption>Example of a configuration of the Case 2 scenario (50 FFs / 41 others / 91 nodes total).</figcaption>
  <img src=files/example-a.png width="75%" />
</figure>


<h2>Results</h2>

<figure>
  <figcaption>Time for discovering a team Member in danger by a corresponding Leader.</figcaption>
  <img src=files/response-times-a.png width="50%" />
</figure>

<figure>
  <figcaption>Number of replicas dissemniated within the whole system.</figcaption>
  <img src=files/message-counts-a.png width="50%" />
</figure>

<figure>
  <figcaption>Number of unnecessary disseminations that were prevented due to communicaiton boundary (when enabled).</figcaption>
  <img src=files/boundary-hits-a.png width="50%" />
</figure>



<h2>Raw data</h2>

<table class="tcompact">
	<tr><th>Simulation inputs</th><td><a href="files/inputs-a.zip">inputs.zip</a></td></tr>
	<tr><th>Raw results</th><td><a href="files/results-a.zip">results.zip</a></td></tr>
</table>

