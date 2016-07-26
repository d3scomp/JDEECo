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

import inet.nodes.inet.WirelessHost;
import org.mixim.modules.power.battery.BatteryStats;
import org.mixim.modules.power.battery.SimpleBattery;
import inet.applications.IPingApp;
import inet.applications.ISCTPApp;
import inet.applications.ITCPApp;
import inet.applications.IUDPApp;
import inet.transport.ISCTP;
import inet.transport.ITCP;
import inet.transport.IUDP;
import inet.util.PcapRecorder;
import inet.networklayer.ipv4.RoutingTable;
import inet.networklayer.common.InterfaceTable;
import inet.mobility.IMobility;
import inet.linklayer.IWirelessNic;
import inet.linklayer.IWiredNic;
import inet.linklayer.IExternalNic;
import inet.base.NotificationBoard;
import inet.nodes.inet.NetworkLayer;
import inet.transport.udp.UDP;

// This host is an example on how a host with INET network stack
// can be equipped with mobility- and wireless-modules from MiXiM. 
//
// It contains an INET network stack and a MiXiM 802.15.4 NIC (TI_CC2420)
// as well as a battery module from MiXiM which is used by the NIC.
//
// This host's NIC is able to operate in a multi-ConnectionManager
// network, i.e. more than one physical network in parallel (non-interfering).
// TODO: factor out battery into INET

module Node
{

     parameters:
        @display("i=device/pc2");
        @node;
        @labels(node,ethernet-node,wireless-node);
        
        //IP protoctol
        bool IPForward = default(false);  // disable routing by default
        string routingFile = default("");
        bool forwardMulticast = default(false);
        bool useEthernet = default(false);
        
        //NIC
        string nic802154Type = default("");
        string nic80211Type = default("");
        
        string mobilityType = default(numRadios > 0 ? "StationaryMobility" : "");
        
    gates:
     	input radio80211In @directIn;
     	input radio802154In @directIn;
        inout ethg @labels(EtherFrame-conn);
        inout direct[];
    submodules:

        //Application
        appl: JDEECoApplication {
            parameters:
                @display("p=70,30;i=app");
        }
        
        udp: UDP if nic80211Type != "" || useEthernet {
            parameters:
                @display("p=329,141");
        }
        
        // optional mobility module. Required only if wireless cards are present
        mobility: <mobilityType> like IMobility if mobilityType != "" {
            parameters:
                @display("p=53,111");
        }
        
        //Network layer
        networkLayer: NetworkLayer if nic80211Type != "" || useEthernet {
            parameters:
                proxyARP = true;
                @display("p=329,287;q=queue");
        }
        
        routingTable: RoutingTable {
            parameters:
                @display("p=53,225;is=s");
                IPForward = IPForward;
                forwardMulticast = forwardMulticast;
                routingFile = routingFile;
        }
        
        //linklayer
        interfaceTable: InterfaceTable {
            parameters:
                @display("p=53,287;is=s");
        } 
            
        //NIC
        nic80211: <nic80211Type> like IWirelessNic if nic80211Type != "" {
            parameters:
                @display("p=132,406,row,60;q=queue");
        }
        nic802154: <nic802154Type> like IWirelessNic if nic802154Type != "" {
            parameters:
                @display("p=132,406,row,60;q=queue");
        }
        
        eth: <default("EthernetInterface")> like IWiredNic if useEthernet {
            parameters:
                @display("p=368,406,row,60;q=txQueue");
        }
        
        //Battery
        batteryStats: BatteryStats {
            @display("p=60,106;i=block/circle");
        }
        battery: SimpleBattery {
            @display("p=60,176;i=block/control");
        }
                notificationBoard: NotificationBoard {
            parameters:
                @display("p=53,155;is=s");
        }
        
    connections allowunconnected:
 
        //# Connect IP branch
        if (useEthernet) {
            ethg <--> { @display("m=s"); } <--> eth.phys;
            eth.upperLayerOut --> networkLayer.ifIn++;
            eth.upperLayerIn <-- networkLayer.ifOut++;
        }
        if (!useEthernet && nic80211Type != "") {
            nic80211.upperLayerOut --> networkLayer.ifIn++;
            nic80211.upperLayerIn <-- networkLayer.ifOut++;
        }
        
        if (nic80211Type != "" || useEthernet) {
            udp.ipOut --> networkLayer.udpIn;
        	udp.ipIn <-- networkLayer.udpOut;
            udp.appOut++ --> appl.lowerLayerIn;
            udp.appIn++ <-- appl.lowerLayerOut;
        }

        //# Connect MANET branch

        if (nic802154Type != "") {
            nic802154.upperLayerOut --> appl.lower802154LayerIn;
            nic802154.upperLayerIn <-- appl.lower802154LayerOut;
        }
}

