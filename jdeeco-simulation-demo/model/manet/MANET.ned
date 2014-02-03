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

import org.mixim.base.modules.BaseNetwork;

network MANET extends BaseNetwork
{
    parameters:
        int numNodes; // total number of hosts in the network
        
        bool visualize = default(false);
        double playgroundLatitude = default(0.0); // geographic position of the playground's north-west corner
        double playgroundLongitude = default(0.0); // geographic position of the playground's north-west corner
        
        
        @display("bgb=2000,2000;bgi=background/terrain,s");

    submodules:
        node[numNodes]: Node {}
        
        channelController: ChannelController if (visualize) {
            @display("p=90,250");
        }
        kmlHttpServer: KmlHttpServer if (visualize) {
            @display("p=320,250;t=Run the simulation in Fast mode\\, then\npoint your browser to localhost:4242\nor open the provided KML file");
        }
    connections allowunconnected:
    // all connections and gates are to be generated dynamically
}
