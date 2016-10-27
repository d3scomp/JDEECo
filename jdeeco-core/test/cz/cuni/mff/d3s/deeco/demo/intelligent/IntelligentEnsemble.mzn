% Two pendolinos crash, one at kilometer 50, one at kilometer 25.
% Both accidents are equally severe.
% 
% We have rescuers located along the track. The task is to send
% half of the rescuer components to one crash and the other half
% to the other accident, while minimizing the summed distance
% of all rescuers to their site.

% the component_count must be divisible by train_count!!!

int: component_count;
int: train_count;

set of int: COMPONENTS = 1..component_count;
set of int: TRAINS = 1..train_count;

array[COMPONENTS] of int: component_pos;
array[TRAINS] of int: train_pos;

array[COMPONENTS] of var TRAINS: component_train;
array[COMPONENTS] of var int: component_distance;
var int: distance_sum_sqr;

% teams have equal size
constraint forall(t in TRAINS)(sum(c in COMPONENTS)(if component_train[c] = t then 1 else 0 endif) = component_count div train_count);

% distance
constraint forall(c in COMPONENTS)(component_distance[c] = abs(component_pos[c] - train_pos[component_train[c]]));
constraint distance_sum_sqr = sum(c in COMPONENTS)(component_distance[c] * component_distance[c]);

solve minimize distance_sum_sqr;