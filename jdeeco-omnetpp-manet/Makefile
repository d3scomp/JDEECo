#
# OMNeT++/OMNEST Makefile for libmodel
#
# This file was generated with the command:
#  opp_makemake -f --deep --make-so -o model -O out -ID:\GitHub\JDEECo\jdeeco-simulation-integration -I../inet/src/linklayer/ieee80211/radio -I../inet/src/linklayer/ethernet -I../MiXiM/src/base/modules -I../inet/src/networklayer/common -I../inet/src -I../inet/src/networklayer/icmpv6 -I../inet/src/world/obstacles -I../MiXiM/src -I../inet/src/networklayer/xmipv6 -I../inet/src/networklayer/manetrouting/base -I../inet/src/networklayer/contract -I../MiXiM/src/base/utils -I../inet/src/mobility/models -I../inet/src/mobility -I../inet/src/networklayer/autorouting/ipv4 -I../MiXiM/src/base -I../inet/src/linklayer/ieee80211/mgmt -I../inet/src/util -I../inet/src/linklayer/common -I../inet/src/transport/contract -I../inet/src/status -I../inet/src/linklayer/radio/propagation -I../inet/src/linklayer/ieee80211/radio/errormodel -I../MiXiM/src/base/connectionManager -I../inet/src/linklayer/radio -I../inet/src/util/headerserializers/tcp -I../inet/src/networklayer/ipv4 -I../inet/src/util/headerserializers/ipv4 -I../inet/src/base -I../inet/src/util/headerserializers -I../inet/src/world/radio -I../inet/src/linklayer/ieee80211/mac -I../MiXiM/src/base/phyLayer -I../inet/src/networklayer/ipv6 -I../inet/src/transport/sctp -I../inet/src/util/headerserializers/udp -I../inet/src/networklayer/ipv6tunneling -I../inet/src/applications/pingapp -I../inet/src/battery/models -I../inet/src/util/headerserializers/sctp -I../inet/src/linklayer/contract -I../inet/src/networklayer/arp -I../inet/src/transport/tcp_common -I../inet/src/transport/udp -I../MiXiM/src/base/messages -LD:\GitHub\JDEECo\jdeeco-simulation-integration\Release -L../MiXiM/out/$(CONFIGNAME)/src -L../inet/out/$(CONFIGNAME)/src -lmixim -linet -lintegration -lmixim -linet -DMIXIM_INET=1 -DINET_IMPORT -KMIXIM_PROJ=../MiXiM -KINET_PROJ=../inet
#

# Name of target to be created (-o option)
TARGET = libmodel$(SHARED_LIB_SUFFIX)

# C++ include paths (with -I)
INCLUDE_PATH = \
    -ID:/GitHub/JDEECo/jdeeco-simulation-integration \
    -I../inet/src/linklayer/ieee80211/radio \
    -I../inet/src/linklayer/ethernet \
    -I../MiXiM/src/base/modules \
    -I../inet/src/networklayer/common \
    -I../inet/src \
    -I../inet/src/networklayer/icmpv6 \
    -I../inet/src/world/obstacles \
    -I../MiXiM/src \
    -I../inet/src/networklayer/xmipv6 \
    -I../inet/src/networklayer/manetrouting/base \
    -I../inet/src/networklayer/contract \
    -I../MiXiM/src/base/utils \
    -I../inet/src/mobility/models \
    -I../inet/src/mobility \
    -I../inet/src/networklayer/autorouting/ipv4 \
    -I../MiXiM/src/base \
    -I../inet/src/linklayer/ieee80211/mgmt \
    -I../inet/src/util \
    -I../inet/src/linklayer/common \
    -I../inet/src/transport/contract \
    -I../inet/src/status \
    -I../inet/src/linklayer/radio/propagation \
    -I../inet/src/linklayer/ieee80211/radio/errormodel \
    -I../MiXiM/src/base/connectionManager \
    -I../inet/src/linklayer/radio \
    -I../inet/src/util/headerserializers/tcp \
    -I../inet/src/networklayer/ipv4 \
    -I../inet/src/util/headerserializers/ipv4 \
    -I../inet/src/base \
    -I../inet/src/util/headerserializers \
    -I../inet/src/world/radio \
    -I../inet/src/linklayer/ieee80211/mac \
    -I../MiXiM/src/base/phyLayer \
    -I../inet/src/networklayer/ipv6 \
    -I../inet/src/transport/sctp \
    -I../inet/src/util/headerserializers/udp \
    -I../inet/src/networklayer/ipv6tunneling \
    -I../inet/src/applications/pingapp \
    -I../inet/src/battery/models \
    -I../inet/src/util/headerserializers/sctp \
    -I../inet/src/linklayer/contract \
    -I../inet/src/networklayer/arp \
    -I../inet/src/transport/tcp_common \
    -I../inet/src/transport/udp \
    -I../MiXiM/src/base/messages \
    -I.

# Additional object and library files to link with
EXTRA_OBJS =

# Additional libraries (-L, -l options)
LIBS = -LD:/GitHub/JDEECo/jdeeco-simulation-integration/Release -L../MiXiM/out/$(CONFIGNAME)/src -L../inet/out/$(CONFIGNAME)/src  -lmixim -linet -lintegration -lmixim -linet
LIBS += -Wl,-rpath,`abspath D:/GitHub/JDEECo/jdeeco-simulation-integration/Release` -Wl,-rpath,`abspath ../MiXiM/out/$(CONFIGNAME)/src` -Wl,-rpath,`abspath ../inet/out/$(CONFIGNAME)/src`

# Output directory
PROJECT_OUTPUT_DIR = out
PROJECTRELATIVE_PATH =
O = $(PROJECT_OUTPUT_DIR)/$(CONFIGNAME)/$(PROJECTRELATIVE_PATH)

# Object files for local .cc and .msg files
OBJS = \
    $O/CustomMobility.o \
    $O/jDEECoApplication.o \
    $O/KmlHttpServer.o \
    $O/KmlUtil.o \
    $O/SocketRealTimeScheduler.o \
    $O/SocketSequentialScheduler.o \
    $O/VisualizationController.o

# Message files
MSGFILES =

# Other makefile variables (-K)
MIXIM_PROJ=../MiXiM
INET_PROJ=../inet

#------------------------------------------------------------------------------

# Pull in OMNeT++ configuration (Makefile.inc or configuser.vc)

ifneq ("$(OMNETPP_CONFIGFILE)","")
CONFIGFILE = $(OMNETPP_CONFIGFILE)
else
ifneq ("$(OMNETPP_ROOT)","")
CONFIGFILE = $(OMNETPP_ROOT)/Makefile.inc
else
CONFIGFILE = $(shell opp_configfilepath)
endif
endif

ifeq ("$(wildcard $(CONFIGFILE))","")
$(error Config file '$(CONFIGFILE)' does not exist -- add the OMNeT++ bin directory to the path so that opp_configfilepath can be found, or set the OMNETPP_CONFIGFILE variable to point to Makefile.inc)
endif

include $(CONFIGFILE)

# Simulation kernel and user interface libraries
OMNETPP_LIB_SUBDIR = $(OMNETPP_LIB_DIR)/$(TOOLCHAIN_NAME)
OMNETPP_LIBS = -L"$(OMNETPP_LIB_SUBDIR)" -L"$(OMNETPP_LIB_DIR)" -loppenvir$D $(KERNEL_LIBS) $(SYS_LIBS)

COPTS = $(CFLAGS) -DMIXIM_INET=1 -DINET_IMPORT $(INCLUDE_PATH) -I$(OMNETPP_INCL_DIR)
MSGCOPTS = $(INCLUDE_PATH)

# we want to recompile everything if COPTS changes,
# so we store COPTS into $COPTS_FILE and have object
# files depend on it (except when "make depend" was called)
COPTS_FILE = $O/.last-copts
ifneq ($(MAKECMDGOALS),depend)
ifneq ("$(COPTS)","$(shell cat $(COPTS_FILE) 2>/dev/null || echo '')")
$(shell $(MKPATH) "$O" && echo "$(COPTS)" >$(COPTS_FILE))
endif
endif

#------------------------------------------------------------------------------
# User-supplied makefile fragment(s)
# >>>
# <<<
#------------------------------------------------------------------------------

# Main target
all: $O/$(TARGET)
	$(Q)$(LN) $O/$(TARGET) .

$O/$(TARGET): $(OBJS)  $(wildcard $(EXTRA_OBJS)) Makefile
	@$(MKPATH) $O
	@echo Creating shared library: $@
	$(Q)$(SHLIB_LD) -o $O/$(TARGET)  $(OBJS) $(EXTRA_OBJS) $(AS_NEEDED_OFF) $(WHOLE_ARCHIVE_ON) $(LIBS) $(WHOLE_ARCHIVE_OFF) $(OMNETPP_LIBS) $(LDFLAGS)
	$(Q)$(SHLIB_POSTPROCESS) $O/$(TARGET)

.PHONY: all clean cleanall depend msgheaders

.SUFFIXES: .cc

$O/%.o: %.cc $(COPTS_FILE)
	@$(MKPATH) $(dir $@)
	$(qecho) "$<"
	$(Q)$(CXX) -c $(COPTS) -o $@ $<

%_m.cc %_m.h: %.msg
	$(qecho) MSGC: $<
	$(Q)$(MSGC) -s _m.cc $(MSGCOPTS) $?

msgheaders: $(MSGFILES:.msg=_m.h)

clean:
	$(qecho) Cleaning...
	$(Q)-rm -rf $O
	$(Q)-rm -f model model.exe libmodel.so libmodel.a libmodel.dll libmodel.dylib
	$(Q)-rm -f ./*_m.cc ./*_m.h

cleanall: clean
	$(Q)-rm -rf $(PROJECT_OUTPUT_DIR)

depend:
	$(qecho) Creating dependencies...
	$(Q)$(MAKEDEPEND) $(INCLUDE_PATH) -f Makefile -P\$$O/ -- $(MSG_CC_FILES)  ./*.cc

# DO NOT DELETE THIS LINE -- make depend depends on it.
$O/CustomMobility.o: CustomMobility.cc \
	CustomMobility.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/BasicModule.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/Coord.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/INETDefs.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/INotifiable.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/ModuleAccess.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/NotificationBoard.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/NotifierConsts.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/IMobility.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/models/MobilityBase.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/models/StationaryMobility.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/util/FWMath.h
$O/KmlHttpServer.o: KmlHttpServer.cc \
	KmlHttpServer.h \
	SocketDataProvider.h
$O/KmlUtil.o: KmlUtil.cc \
	KmlUtil.h
$O/SocketRealTimeScheduler.o: SocketRealTimeScheduler.cc \
	SocketDataProvider.h \
	SocketRealTimeScheduler.h
$O/SocketSequentialScheduler.o: SocketSequentialScheduler.cc \
	SocketDataProvider.h \
	SocketSequentialScheduler.h
$O/VisualizationController.o: VisualizationController.cc \
	KmlHttpServer.h \
	KmlUtil.h \
	VisualizationController.h
$O/jDEECoApplication.o: jDEECoApplication.cc \
	CustomMobility.h \
	KmlHttpServer.h \
	KmlUtil.h \
	VisualizationController.h \
	jDEECoApplication.h \
	$(MIXIM_PROJ)/src/base/modules/BaseModule.h \
	$(MIXIM_PROJ)/src/base/utils/HostState.h \
	$(MIXIM_PROJ)/src/base/utils/MacToNetwControlInfo.h \
	$(MIXIM_PROJ)/src/base/utils/MiXiMDefs.h \
	$(MIXIM_PROJ)/src/base/utils/NetwToMacControlInfo.h \
	$(MIXIM_PROJ)/src/base/utils/SimpleAddress.h \
	$(MIXIM_PROJ)/src/base/utils/miximkerneldefs.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/BasicModule.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/Coord.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/INETDefs.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/INotifiable.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/ModuleAccess.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/NotificationBoard.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/base/NotifierConsts.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/linklayer/contract/MACAddress.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/IMobility.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/models/MobilityBase.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/mobility/models/StationaryMobility.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPProtocolId_m.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPv4Address.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPv4ControlInfo.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPv4ControlInfo_m.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPv6Address.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPvXAddress.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/networklayer/contract/IPvXAddressResolver.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/transport/contract/UDPControlInfo.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/transport/contract/UDPControlInfo_m.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/transport/contract/UDPSocket.h \
	D:/Workspaces/OMNet++/jDEECo-Workspace/inet/src/util/FWMath.h

