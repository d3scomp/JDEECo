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

import org.mixim.base.modules.IBaseApplLayer;
//
// TODO auto-generated module
//
simple JDEECoApplication like IBaseApplLayer
{
    parameters:
        int headerLength = 0;
        string id = default("not-assigned");
        
        //Google-Earth relevant paramters
        string color = default(""); // the color of the trail and range indicator in hex BBGGRR format or "" for random color
        string modelURL = default(""); // the URL of the 3D model (COLLADA file format) to be used for the node
        double modelScale = default(1.0); // allows scaling of the used model
        bool showTxRange = default(true);  // whether to show the transmission range around the nodes
        double txRange @unit("m") = default(250m);  // the transmission range of the mobile node's radio
    gates:
        input lowerLayerIn; // from network layer
        output lowerLayerOut; // to network layer
        input lowerControlIn; // control from network layer
        output lowerControlOut; // control to network layer
}
