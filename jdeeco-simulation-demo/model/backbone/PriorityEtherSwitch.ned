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

import inet.linklayer.IMACRelayUnit;
import inet.linklayer.IEtherMAC;

//
// Model of an Ethernet switch.
//
// The duplexChannel attributes of the MACs must be set according to the
// medium connected to the port; if collisions are possible (it's a bus or hub)
// it must be set to false, otherwise it can be set to true.
// By default used half duples CSMA/CD mac
//
// This model does not contain the spanning tree algorithm.
//
module PriorityEtherSwitch
{
    parameters:
        @node();
        @labels(node,ethernet-node);
        @display("i=device/switch");
        bool csmacdSupport = default(false);  // by default use CSMA/CD
        string macType = default(csmacdSupport ? "EtherMAC" : "EtherMACFullDuplex"); // ~EtherMAC or ~EtherMACFullDuplex
        string relayUnitType = default("PriorityMACRelayUnitNP");
    gates:
        inout ethg[] @loose @labels(EtherFrame-conn);
    submodules:
        relayUnit: PriorityMACRelayUnitNP {
            parameters:
                @display("p=200,50");
            gates:
                lowerLayerIn[sizeof(ethg)];
                lowerLayerOut[sizeof(ethg)];
        }
        mac[sizeof(ethg)]: <macType> like IEtherMAC {
            parameters:
                promiscuous = true;
                queueModule = "";
                @display("p=70,150,row;q=txQueue");
        }
    connections:
        for i=0..sizeof(ethg)-1 {
            mac[i].upperLayerIn <-- relayUnit.lowerLayerOut[i];
            mac[i].upperLayerOut --> relayUnit.lowerLayerIn[i];
            mac[i].phys <--> ethg[i];
        }
}

