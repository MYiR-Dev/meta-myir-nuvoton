SUMMARY = "Temp Ctrl"
DESCRIPTION = "Temperature Control"
LICENSE = "GPL-2"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}"

SRC_URI = " \
     file://licenses/GPL-2 \
     file://temp_ctrl \
     file://temp_ctrl.service \
     "
inherit systemd

do_install() {
	install -d -m 755 ${D}/usr/sbin
        install -d -m 755 ${D}${systemd_system_unitdir}

        install ${WORKDIR}/temp_ctrl ${D}/usr/sbin
        install -m 644 ${WORKDIR}/temp_ctrl.service ${D}${systemd_system_unitdir}/temp_ctrl.service
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "temp_ctrl.service"
SYSTEMD_AUTO_ENABLE_hmi = "enable"

FILES_${PN} += "/"
