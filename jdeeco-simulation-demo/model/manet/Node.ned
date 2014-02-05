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

import org.mixim.modules.nic.Nic802154_TI_CC2420;
import org.mixim.modules.power.battery.BatteryStats;
import org.mixim.modules.power.battery.SimpleBattery;
import org.mixim.modules.nic.IWirelessNicUpperCtrl;
import org.mixim.base.modules.IBaseArp;
import org.mixim.base.modules.IBaseApplLayer;
import org.mixim.base.modules.IBaseNetwLayer;
import inet.linklayer.INic;

module Node
{
    parameters:
        string transportType = default("");    //type of the transport layer
        string networkType = default("BaseNetwLayer");       //type of the network layer
        string arpType = default("BaseArp");                 //type of address resolution module
        string mobilityType = default("StationaryMobility"); //type of the mobility module
        string manetNicType;
        string ipNicType;
        
        @display("bgb=158,152,white;i=device/palm;b=40,40,rect");
        @node();

	    submodules:
	    appl: JDEECoApplication {
            parameters:
                @display("p=70,30;i=app");
        }    
	            
        arp: <arpType> like IBaseArp {
                @display("p=140,100;i=block/network2");
        }
        manetNic: <manetNicType> like IWirelessNicUpperCtrl {
            parameters:
                @display("p=70,380;i=block/wrxtx,#008000");
        }
        ipNic: <ipNicType> like IWirelessNicUpperCtrl {
            parameters:
                @display("p=70,440;i=block/wrxtx,#008000");
        }
        // OSI network layer
        netwl: <networkType> like IBaseNetwLayer {
            parameters:
                @display("p=70,310;i=block/layer");
        }
        // OSI transport layer if defined
        tranl: <transportType> like IBaseNetwLayer if transportType != "" {
            parameters:
                @display("p=70,240;i=block/layer");
        }
        batteryStats: BatteryStats {
            @display("p=140,240;i=block/table,#FF8040");
        }
        battery: SimpleBattery {
            @display("p=116,36;i=block/plug,#FF8000");
        }
        mobility: <mobilityType> like inet.mobility.IMobility {
            parameters:
                @display("p=140,30;i=block/cogwheel");
        }
    connections allowunconnected:
        
        //# Connect IP branch
        
        ipNic.upperLayerOut --> netwl.lowerLayerIn;
        ipNic.upperLayerIn <-- netwl.lowerLayerOut;
        ipNic.upperControlOut --> { @display("ls=red;m=m,70,0,70,0"); } --> netwl.lowerControlIn;
        ipNic.upperControlIn <-- { @display("ls=red;m=m,70,0,70,0"); } <-- netwl.lowerControlOut;
   
        if (transportType != "") {
            //# transport layer exists, so we can connect netwl <--> tranl
            netwl.upperLayerOut --> tranl.lowerLayerIn;
            netwl.upperLayerIn <-- tranl.lowerLayerOut;
            netwl.upperControlOut --> tranl.lowerControlIn;
            netwl.upperControlIn <-- tranl.lowerControlOut;
            
            tranl.upperLayerOut --> appl.lowerIPLayerIn;
            tranl.upperLayerIn <-- appl.lowerIPLayerOut;
            tranl.upperControlOut --> { @display("ls=red;m=m,70,0,70,0"); } --> appl.lowerIPControlIn;
            tranl.upperControlIn <-- { @display("ls=red;m=m,70,0,70,0"); } <-- appl.lowerIPControlOut;
        } 
		if (transportType == "") {
            netwl.upperLayerOut --> appl.lowerIPLayerIn;
            netwl.upperLayerIn <-- appl.lowerIPLayerOut;
            netwl.upperControlOut --> { @display("ls=red;m=m,70,0,70,0"); } --> appl.lowerIPControlIn;
            netwl.upperControlIn <-- { @display("ls=red;m=m,70,0,70,0"); } <-- appl.lowerIPControlOut;
        }
        
        //# Connect MANET branch
        
        manetNic.upperLayerOut --> appl.lowerMANETLayerIn;
        manetNic.upperLayerIn <-- appl.lowerMANETLayerOut;
        manetNic.upperControlOut --> appl.lowerMANETControlIn;
        manetNic.upperControlIn <-- appl.lowerMANETControlOut;
}
