/*
 * jDEECoConstants.h
 *
 *  Created on: 23 Jan 2014
 *      Author: Michal Kit <kit@d3s.mff.cuni.cz>
 */

#ifndef CONFIG_H_
#define CONFIG_H_

#ifdef _WIN32

#ifdef MAKEDLL
#define DLLEXPORT_OR_IMPORT __declspec(dllexport)
#else
#define DLLEXPORT_OR_IMPORT __declspec(dllimport)
#endif

#else

#define DLLEXPORT_OR_IMPORT

#endif

#endif /* CONFIG_H_ */
