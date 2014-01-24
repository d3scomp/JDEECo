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

package backbone;

import inet.linklayer.IEtherMAC;
import inet.linklayer.IOutputQueue;

module PriorityEtherHost
{
	parameters:
        bool csmacdSupport = default(true);  // by default use CSMA/CD
        bool staticConnections = default(true);
        string macType = default(csmacdSupport ? "EtherMAC" : "EtherMACFullDuplex"); // ~EtherMAC or ~EtherMACFullDuplex
        @node();
        @labels(node,ethernet-node);
        @display("i=device/pc2");
    gates:
        inout ethg @labels(EtherFrame-conn);
    submodules:
        app: PriorityTrafficGenerator {
            @display("p=155,59");
        }
        encap: PriorityFrameEncap {
            @display("p=155,146");
        }
        queue: DropTailPriorityQueue {
            parameters:
                @display("p=87,207;q=l2queue");
        }
        mac: <macType> like IEtherMAC {
            queueModule = "queue";
            @display("p=155,272;q=txQueue");
        }
    connections:
        app.out --> encap.upperLayerIn;
        app.in <-- encap.upperLayerOut;

        encap.lowerLayerOut --> queue.in;
        queue.out --> mac.upperLayerIn;
        mac.upperLayerOut --> encap.lowerLayerIn;

        mac.phys <--> ethg;
}
