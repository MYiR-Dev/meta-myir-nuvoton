# MYIR - 2020   Alex .
DESCRIPTION = "Extra files for MYiR"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=309cc7bace8769cfabdd34577f654f8e"

SRC_URI += "file://startup.sh \
           file://startup.service \
           file://LICENSE"

S = "${WORKDIR}"

inherit systemd 

SYSTEMD_SERVICE_${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'startup.service', '', d)}"

do_install_append() {
	
	install -d ${D}${systemd_unitdir}/system
	install -d ${D}${sysconfdir}
	install -d ${D}${sysconfdir}/init.d

	install -m 0644 ${WORKDIR}/startup.service ${D}${systemd_system_unitdir}
	install -m 755 ${WORKDIR}/startup.sh ${D}${sysconfdir}/init.d/startup.sh
}

pkg_postinst_ontarget_${PN} () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		if [ -n "$D" ]; then
			OPTS="--root=$D"
		fi
		systemctl $OPTS enable startup.service
		localectl set-locale zh_CN.UTF-8
	fi
}

FILES_${PN}=" ${sysconfdir}   \
              ${sysconfdir}/system \
	  "
#For dev packages only
#INSANE_SKIP_${PN}-dev = "ldflags"
#INSANE_SKIP_${PN} = "${ERROR_QA} ${WARN_QA}"


