JDK_HOME ?= /c/Progra~2/Java/jdk8
OMNETPP_ROOT ?= /d/omnetpp-4.3.1
INET_DIR ?= ${OMNETPP_ROOT}/../inet
MIXIM_DIR ?= ${OMNETPP_ROOT}/../mixim

OMNETPP_SRC_DIR ?= ${OMNETPP_ROOT}/src
OMNETPP_INCL_DIR ?= ${OMNETPP_INCL_DIR}/include


CONFIGFILE = ${OMNETPP_ROOT}/Makefile.inc
include $(CONFIGFILE)

LDFLAGS += -Wl,--allow-multiple-definition,--kill-at

all: checkmakefiles
	cd src && $(MAKE) LDFLAGS="$(LDFLAGS)"

clean: checkmakefiles
	cd src && $(MAKE) clean

cleanall: checkmakefiles
	cd src && $(MAKE) clean
	rm -f src/Makefile
	rm -rf out/

# Create makefiles that allows building mixim without a separate INET installation.
makefiles:
	cd src && ${OMNETPP_ROOT}/bin/opp_makemake -f --deep --make-so -o jdeeco-omnetpp -O out \
	-I${JDK_HOME}/include \
	-I${JDK_HOME}/include/win32 \
	-I${OMNETPP_SRC_DIR}/common -I${OMNETPP_INCL_DIR}/platdep -I${OMNETPP_SRC_DIR}/envir \
	-I${MIXIM_DIR}/src \
	-I${MIXIM_DIR}/src/base \
	-I${MIXIM_DIR}/src/base/modules \
	-I${MIXIM_DIR}/src/base/utils \
	-I${MIXIM_DIR}/src/base/connectionManager \
	-I${MIXIM_DIR}/src/base/phyLayer \
	-I${MIXIM_DIR}/src/base/messages \
	-I${INET_DIR}/src/linklayer/ieee80211/radio \
	-I${INET_DIR}/src/linklayer/ethernet \
	-I${INET_DIR}/src/networklayer/common \
	-I${INET_DIR}/src \
	-I${INET_DIR}/src/networklayer/icmpv6 \
	-I${INET_DIR}/src/world/obstacles \
	-I${INET_DIR}/src/networklayer/xmipv6 \
	-I${INET_DIR}/src/networklayer/manetrouting/base \
	-I${INET_DIR}/src/networklayer/contract \
	-I${INET_DIR}/src/mobility/models \
	-I${INET_DIR}/src/mobility \
	-I${INET_DIR}/src/networklayer/autorouting/ipv4 \
	-I${INET_DIR}/src/linklayer/ieee80211/mgmt \
	-I${INET_DIR}/src/util \
	-I${INET_DIR}/src/linklayer/common \
	-I${INET_DIR}/src/transport/contract \
	-I${INET_DIR}/src/status \
	-I${INET_DIR}/src/linklayer/radio/propagation \
	-I${INET_DIR}/src/linklayer/ieee80211/radio/errormodel \
	-I${INET_DIR}/src/linklayer/radio \
	-I${INET_DIR}/src/util/headerserializers/tcp \
	-I${INET_DIR}/src/networklayer/ipv4 \
	-I${INET_DIR}/src/util/headerserializers/ipv4 \
	-I${INET_DIR}/src/base \
	-I${INET_DIR}/src/util/headerserializers \
	-I${INET_DIR}/src/world/radio \
	-I${INET_DIR}/src/linklayer/ieee80211/mac \
	-I${INET_DIR}/src/networklayer/ipv6 \
	-I${INET_DIR}/src/transport/sctp \
	-I${INET_DIR}/src/util/headerserializers/udp \
	-I${INET_DIR}/src/networklayer/ipv6tunneling \
	-I${INET_DIR}/src/applications/pingapp \
	-I${INET_DIR}/src/battery/models \
	-I${INET_DIR}/src/util/headerserializers/sctp \
	-I${INET_DIR}/src/linklayer/contract \
	-I${INET_DIR}/src/networklayer/arp \
	-I${INET_DIR}/src/transport/tcp_common \
	-I${INET_DIR}/src/transport/udp \
	-I${INET_DIR}/src/mobility/static \
	-I${INET_DIR}/src/mobility/common \
	-I${INET_DIR}/src/mobility/contract \
	-L${MIXIM_DIR}/out/$(CONFIGNAME)/src \
	-L${INET_DIR}/out/$(CONFIGNAME)/src \
	-loppcommond -lmixim -linet -DMIXIM_INET=1 -DINET_IMPORT -KMIXIM_PROJ=${MIXIM_DIR} -KINET_PROJ=${INET_DIR} -KV=1

checkmakefiles:
	@if [ ! -f src/Makefile ]; then \
	echo; \
	echo '=========================================================='; \
	echo 'src/Makefile does not exist. Please use "make makefiles"'; \
	echo 'to generate it!'; \
	echo '=========================================================='; \
	echo; \
	exit 1; \
	fi

