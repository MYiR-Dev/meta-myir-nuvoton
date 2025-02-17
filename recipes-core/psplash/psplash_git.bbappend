FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://psplash-basic.service \
    file://psplash-network.service \
    file://psplash-quit.service \
    file://0001-FEAT-Support-Myir.patch  \
"
SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' psplash-basic.service psplash-network.service psplash-quit.service', '', d)}"

do_install_append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/psplash-basic.service ${D}/${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/psplash-network.service ${D}/${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/psplash-quit.service ${D}/${systemd_unitdir}/system
    fi
}
