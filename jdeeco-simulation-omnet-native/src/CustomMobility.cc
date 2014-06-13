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

#include "CustomMobility.h"

Define_Module(CustomMobility);

void CustomMobility::handleMessage(cMessage *msg)
{
    ASSERT(false);
}

void CustomMobility::setCurrentPosition(Coord coordinates)
{
    lastPosition.x = coordinates.x;
    lastPosition.y = coordinates.y;
    lastPosition.z = coordinates.z;
}
