package manet;

import org.mixim.modules.node.WirelessNodeBatteryPlusTran;

module Host802154 extends WirelessNodeBatteryPlusTran
{
    parameters:
        arpType       = "ArpHost";
        nicType       = "Nic802154_TI_CC2420";
        transportType = "Aggregation";
}
