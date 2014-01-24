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

import ned.DatarateChannel;

network Network
{
    parameters:
        int accessPointCount = default(3);
        int vehicleCount = default(1);
        @display("bgb=1458,800");
    types:
        channel C extends DatarateChannel
        {
            delay = 10us;
            datarate = 100Mbps;
        }
        channel B extends DatarateChannel
        {
            delay = 1us;
            datarate = 1Gbps;
        }
    submodules:
        ICS: PriorityEtherHost {
            parameters:
                @display("p=39,751");	
        }
        switch: PriorityEtherSwitch {
            @display("p=52,678");
            gates:
                ethg[accessPointCount + 1];
        }
        ap[accessPointCount]: PriorityEtherSwitch {
            parameters:
                relayUnitType = "PriorityMACRelayUnitNPAccessPoint";
            gates:
                ethg[vehicleCount+1];
        }
        v[vehicleCount]: PriorityEtherHost;
    connections allowunconnected:
        switch.ethg[0] <--> B <--> ICS.ethg;
        for i=0..accessPointCount-1 {
            switch.ethg[i+1] <--> B <--> ap[i].ethg[0];
        }
}
