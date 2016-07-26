//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
// 

package manet;

import inet.networklayer.autorouting.ipv4.FlatNetworkConfigurator;
import inet.nodes.inet.Router;
import inet.nodes.inet.StandardHost;
import inet.nodes.inet.WirelessHost;
import inet.world.radio.ChannelControl;
import org.mixim.base.connectionManager.ConnectionManager;
import org.mixim.base.modules.BaseWorldUtility;
import inet.nodes.inet.Router;
import inet.networklayer.autorouting.ipv4.IPv4NetworkConfigurator;

network MANET
{
    parameters:
        double playgroundSizeX @unit(m) = default(1000m); // x size of the area the nodes are in (in meters)
        double playgroundSizeY @unit(m) = default(1000m); // y size of the area the nodes are in (in meters)
        double playgroundSizeZ @unit(m) = default(1000m); // z size of the area the nodes are in (in meters)
        
        bool visualize = default(true);
        double playgroundLatitude = default(0.0); // geographic position of the playground's north-west corner
        double playgroundLongitude = default(0.0); // geographic position of the playground's north-west corner
        
        int numNodes;
        int numRouters = default(0);
        
        bool ethernet = default(false);
        
        @display("bgb=469,300,white;bgp=10,50");
    submodules:
        
        world: BaseWorldUtility {
            parameters:
                playgroundSizeX = playgroundSizeX;
                playgroundSizeY = playgroundSizeY;
                playgroundSizeZ = playgroundSizeZ;
                @display("p=37,25");
        }
        channel80211: ConnectionManager {
            parameters:
                @display("p=132,25");
        }
        channel802154: ConnectionManager {
            parameters:
                @display("p=264,25");
        }
        channelControl: ChannelControl {
            parameters:
            	@display("p=391,24");
        }
        
        node[numNodes]: Node {
        	useEthernet = ethernet;
        }
        router[numRouters]: Router {}
        
        switch: Switch {}
        
        configurator: IPv4NetworkConfigurator {
            @display("p=140,50");
        }
        
        //Visualization
        visualizationController: VisualizationController if visualize {
            @display("p=90,250");
        }
        kmlHttpServer: KmlHttpServer if visualize {
            @display("p=320,250;t=Run the simulation in Fast mode\\, then\npoint your browser to localhost:4242\nor open the provided KML file");
        }
    connections allowunconnected:
        //Connect routers
        
        for i=0..numRouters-1 {
        	router[i].ethg++ <--> {  datarate = 100Mbps; delay = 0.1us; } <--> switch.ethg++ if !ethernet;
        }
        // 4G:  6Mbps down, 1.5Mbps up, 150ms latency 
        // simulate 4G
        for i=0..numNodes-1 {
            node[i].ethg <--> {  datarate = 10Mbps; delay = 100000us; } <--> switch.ethg++ if ethernet;
        }
}
