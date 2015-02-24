#ifndef SIMUALTION_H
#define SIMULATION_H

#include <stdint.h>

/// Type used for representation of the jDEECo node
typedef uint16_t NodeId;

/**
 * Starts the OMNeT simulation
 */
void simulate(const char * envName, const char * confFile);

#endif // SIMULATION_H
