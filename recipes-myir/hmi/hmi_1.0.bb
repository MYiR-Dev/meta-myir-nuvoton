SUMMARY = "MYIR HMI Demo Experience"
DESCRIPTION = "Launcher for MYIR HMI Demo"
#LICENSE = "GPL-2"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
#LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}/git"

SRCBRANCH = "MXAPP2-Qt5"

MYIR_HMI_SRC ?= "git://github.com/xmr123456/MXAPP2.git;protocol=https"

SRC_URI = " \
    ${MYIR_HMI_SRC};branch=${SRCBRANCH} \
           file://home/root/hmi.sh \
                   file://hmi.service \
                   file://usr/share/fonts/ttf/msyh.ttc \
                   file://usr/share/myir/ecg.dat \
                   file://usr/share/myir/resp.text \
                   file://usr/share/myir/myir.bmp \
                   file://usr/share/myir/myir.png \
                   file://usr/share/myir/myir.jpg \
                   file://usr/share/myir/song.mp3 \
                   file://usr/share/myir/song.wav \
                   file://0001-FEAT-Support-the-MYD-LMA35-Camera-function-is-delete.patch \
     "


SRCREV = "${AUTOREV}"

inherit systemd
#SRCREV_FORMAT = "myir-hmi-demos"
#SRCREV_myir-hmi-demos = "256bf6f0ae2a88836f7eec743c75ce0bcd89a4cc"
#SRC_URI[myir-hmi-demos.sha256sum] = "37abc8215473ccaed8afb7b515b9cf17ba6e25c4a49188b55762bd7c1aa58ed9"

inherit qmake5

# DEPENDS += " packagegroup-qt5-imx qtquickcontrols2 qtconnectivity qtgraphicaleffects qtsvg qtmultimedia "
DEPENDS += " qtquickcontrols2 qtconnectivity qtgraphicaleffects qtsvg qtmultimedia "
# RDEPENDS_${PN} += " weston bash qtgraphicaleffects-qmlplugins qtquickcontrols-qmlplugins qtsvg-plugins"
ROOT_HOME="/home/root"

do_install() {
	install -d -m 755 ${D}${ROOT_HOME}
        install -d -m 755 ${D}${datadir}
        install -d -m 755 ${D}${datadir}/myir
        install -d -m 755 ${D}${datadir}/myir/Video
        install -d -m 755 ${D}${datadir}/myir/Music
        install -d -m 755 ${D}${datadir}/myir/Capture
        install -d -m 755 ${D}${datadir}/fonts/ttf
        install -d -m 755 ${D}${systemd_system_unitdir}

        install ${WORKDIR}/build/mxapp2 ${D}${ROOT_HOME}/
	install -m 755 ${WORKDIR}${ROOT_HOME}/hmi.sh ${D}${ROOT_HOME}/hmi.sh
        install -m 755 ${WORKDIR}${datadir}/fonts/ttf/msyh.ttc ${D}${datadir}/fonts/ttf/msyh.ttc
        install -m 755 ${WORKDIR}${datadir}/myir/ecg.dat ${D}${datadir}/myir/ecg.dat
        install -m 755 ${WORKDIR}${datadir}/myir/resp.text ${D}${datadir}/myir/resp.text
        install -m 755 ${WORKDIR}${datadir}/myir/myir.bmp ${D}${datadir}/myir/Capture/myir.bmp
        install -m 755 ${WORKDIR}${datadir}/myir/myir.png ${D}${datadir}/myir/Capture/myir.png
        install -m 755 ${WORKDIR}${datadir}/myir/myir.jpg ${D}${datadir}/myir/Capture/myir.jpg
        install -m 755 ${WORKDIR}${datadir}/myir/song.mp3 ${D}${datadir}/myir/Music/song.mp3
        install -m 755 ${WORKDIR}${datadir}/myir/song.wav ${D}${datadir}/myir/Music/song.wav

        install -m 644 ${WORKDIR}/hmi.service ${D}${systemd_system_unitdir}/hmi.service
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "hmi.service"
SYSTEMD_AUTO_ENABLE_hmi = "enable"

FILES_${PN} += "/"
